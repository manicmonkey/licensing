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

package com.magmanics.auditing.service

import com.magmanics.auditing.dao.AuditDao
import com.magmanics.auditing.model.{Audit, AuditCode}
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.{Propagation, Transactional}

/**
 * AuditService allows for persisting [[com.magmanics.auditing.model.Audit Audits]] and retrieving Audits for review.
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 18-Sep-2010
 */
trait AuditService {
  /**
   * Persist the given Audit
   */
  def create(audit: Audit)

  /**
   * @return a list of the distinct usernames for which there are audit entries
   */
  def getUsernames: Seq[String]

  /**
   * @return a sequence of the distinct audit codes for which there are audit entries
   */
  def getAuditCodes: Seq[AuditCode]

  /**
   * @return a sequence of [[com.magmanics.auditing.model.Audit Audits]] which conform to the given search criteria
   */
  def getAuditMessages(auditSearch: AuditSearchDto): Seq[Audit]
}

class AuditServiceImpl extends AuditService {

  val log = LoggerFactory.getLogger(classOf[AuditService])

  var auditDao: AuditDao = _

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def create(audit: Audit) {
    log.info("Auditing: {}", audit)
    auditDao.create(audit)
  }

  def getAuditCodes = {
    log.info("Getting distinct AuditCodes")
    auditDao.getAuditCodes
  }

  def getUsernames = {
    log.info("Getting distinct usernames")
    auditDao.getUsernames
  }

  def getAuditMessages(auditSearch: AuditSearchDto) = {
    log.info("Getting Audits with search criteria: {}", auditSearch)
    auditDao.getAuditMessages(auditSearch)
  }

  def setAuditDao(auditDao: AuditDao) {
    this.auditDao = auditDao
  }
}