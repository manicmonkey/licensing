/**
 * Magmanics Licensing. This web application allows for centralized control
 * of client application activation, with optional configuration parameters
 * to control licensable features, and storage of supplementary information
 * about the client machine. Client applications may interface with this
 * central server (for activation) using libraries licenced under an
 * alternative licence.
 *
 * Copyright (C) 2010 James Baxter <j.w.baxter(at)gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmanics.auditing

import model.{Audit, AuditCode}
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation._
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import com.magmanics.auditing.service.AuditService
import org.springframework.security.core.userdetails.User
import java.util.Arrays

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 18 -Sep-2010
 */
@Aspect
class AuditAdvice {

  val log = LoggerFactory.getLogger(classOf[AuditAdvice])

  var auditService: AuditService = _

  @Pointcut("execution(* com.magmanics.licensing.service..*.*(..))")
  private def serviceLayer() {}

  //todo dont want to audit methods called by auditable methods? would have to use @before or @around

  @AfterReturning(pointcut = "serviceLayer() && @annotation(auditable)", returning = "retVal")
  def logSuccess(jp: JoinPoint, auditable: Auditable, retVal: Any): Any = {
    createAudit(AuditCode(auditable.value), "Called with (" + Arrays.asList(jp.getArgs.toArray: _*) + "), returning (" + retVal + ")")
  }

  @AfterThrowing(pointcut = "serviceLayer() && @annotation(auditable)", throwing = "ex")
  def logThrown(jp: JoinPoint, auditable: Auditable, ex: Exception): Any = {
    createAudit(AuditCode(auditable.value), "Called with (" + Arrays.asList(jp.getArgs.toArray: _*) + "), threw (" + ex + ")")
  }

  private def createAudit(auditCode: AuditCode, auditMessage: String) {

    val authentication = SecurityContextHolder.getContext.getAuthentication
    val username = if (authentication == null) {
                     "unknown"
                   } else {
                     val principal = authentication.getPrincipal
                     principal match {
                       case p: User => p.getUsername
                       case _ => principal.toString
                     }
                   }

    try {
      auditService.create(Audit(username = username, auditCode = auditCode, auditMessage = auditMessage))
    } catch {
      case e: Exception => log.error("Exception attempting to store Audit", e)
    }
  }

  def setAuditService(auditService: AuditService) {
    this.auditService = auditService
  }
}