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

package com.magmanics.auditing.dao

import javax.persistence.{EntityManager, PersistenceContext}

import com.magmanics.auditing.model.{Audit, AuditCode, AuditEntity}
import com.magmanics.auditing.service.AuditSearchDto
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
 * Dao for {@link com.magmanics.auditing.model.Audit Audit}s
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 20-Sep-2010
 */
trait AuditDao {
  /**
   * Persist a new Audit
   */
  def create(audit: Audit)

  /**
   * Get known audit codes
   */
  def getAuditCodes: Seq[AuditCode]

  /**
   * Get existing usernames
   */
  def getUsernames: Seq[String]

  /**
   * Search for audits meeting defined criteria
   */
  def getAuditMessages(auditSearch: AuditSearchDto): Seq[Audit]
}

object ImplicitEntityModelConversion {
  implicit def auditEntitySeqToAuditSeq(audits: Seq[AuditEntity]): Seq[Audit] = {
    audits.map(auditEntityToAudit)
  }
  implicit def auditEntityToAudit(audit: AuditEntity): Audit = {
    Audit(audit.username, AuditCode(audit.auditCode), audit.auditMessage, audit.created)
  }
}

class AuditDaoJPA extends AuditDao {

  import com.magmanics.auditing.dao.ImplicitEntityModelConversion._

  val log = LoggerFactory.getLogger(classOf[AuditDaoJPA])

  @PersistenceContext
  var em: EntityManager = _

  override def create(audit: Audit) {

    log.debug("Creating Audit({})", audit)
    val a = new AuditEntity
    a.created = audit.created
    a.username = audit.username
    a.auditCode = audit.auditCode.value
    a.auditMessage = audit.auditMessage

    if (a.auditMessage != null && a.auditMessage.length > 2000) {
      log.warn("Truncating auditMessage (longer than 2000 chars): {}", audit)
      a.auditMessage = a.auditMessage.substring(0, 2000)
    }

    em.persist(a)
  }

  override def getAuditCodes(): Seq[AuditCode] = {
    log.debug("Getting distinct AuditCodes")
    val query = em.createNamedQuery[String]("Audit.GetAuditCodes", classOf[String])
    val auditCodes = query.getResultList.asScala
    auditCodes.map(AuditCode)
  }

  override def getUsernames() = {
    log.debug("Getting known usernames")
    val query = em.createNamedQuery[String]("Audit.GetDistinctUsernames", classOf[String])
    query.getResultList.asScala
  }

  override def getAuditMessages(auditSearch: AuditSearchDto) = {
    log.debug("Getting Audits with search criteria: {}", auditSearch)
    val queryString =
      if (auditSearch.text == "")
        "SELECT a FROM AuditEntity a WHERE a.created > :from AND a.created < :to AND a.username IN :users AND a.auditCode IN :auditCodes"
      else
        "SELECT a FROM AuditEntity a WHERE a.created > :from AND a.created < :to AND a.username IN :users AND a.auditCode IN :auditCodes AND a.auditMessage LIKE :text"

    val query = em.createQuery[AuditEntity](queryString, classOf[AuditEntity])
    query.setParameter("from", auditSearch.fromDate)
    query.setParameter("to", auditSearch.toDate)
    query.setParameter("users", auditSearch.users.asJava)
    query.setParameter("auditCodes", auditSearch.auditCodes.asJava)
    if (auditSearch.text != "")
      query.setParameter("text", "%" + auditSearch.text + "%")
    query.getResultList.asScala
  }
}
