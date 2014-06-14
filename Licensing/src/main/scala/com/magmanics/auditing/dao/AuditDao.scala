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

import org.slf4j.LoggerFactory
import com.magmanics.auditing.service.AuditSearchDto
import com.vaadin.ui.Select
import com.magmanics.licensing.datalayer.model.ActivationCircumflex
import java.util.{Date, Calendar}
import com.magmanics.auditing.model.{Audit, AuditCode, AuditCircumflex}

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
  def getAuditCodes(): Seq[AuditCode]

  /**
   * Get existing usernames
   */
  def getUsernames(): Seq[String]

  /**
   * Search for audits meeting defined criteria
   */
  def getAuditMessages(auditSearch: AuditSearchDto): Seq[Audit]
}

object ImplicitCircumflexModelConversion {
  implicit def auditCircumflexSeqToAuditSeq(audits: Seq[AuditCircumflex]): Seq[Audit] = {
    audits.map(auditCircumflexToAudit(_))
  }
  implicit def auditCircumflexToAudit(audit: AuditCircumflex): Audit = {
    Audit(audit.created.apply(), audit.username.apply(), AuditCode(audit.auditCode.apply()), audit.auditMessage.apply())
  }
}

class AuditDaoCircumflex extends AuditDao {

  val log = LoggerFactory.getLogger(classOf[AuditDaoCircumflex])

  import ru.circumflex.orm._
  import ImplicitCircumflexModelConversion._

  override def create(audit: Audit) {

    log.debug("Creating Audit({})", audit)
    val a = new AuditCircumflex
    a.created := audit.created
    a.username := audit.username
    a.auditCode := audit.auditCode.value
    a.auditMessage := audit.auditMessage
    a.save
  }

  override def getAuditCodes() = {
    log.debug("Getting distinct AuditCodes")
    val a = AuditCircumflex AS "a"
    (SELECT (a.auditCode) FROM (a) GROUP_BY (a.auditCode)).list().map(AuditCode(_))
  }

  override def getUsernames() = {
    log.debug("Getting known usernames")
    val a = AuditCircumflex AS "a"
    (SELECT (a.username) FROM (a) GROUP_BY (a.username) ORDER_BY (a.username ASC)).list()
  }

  override def getAuditMessages(auditSearch: AuditSearchDto) = {
    log.debug("Getting Audits with search criteria: {}", auditSearch)
    if (auditSearch.text == "") {
      auditMessageQuery(auditSearch.fromDate, auditSearch.toDate, auditSearch.users, auditSearch.auditCodes)
    } else {
      auditMessageQuery(auditSearch.fromDate, auditSearch.toDate, auditSearch.users, auditSearch.auditCodes, auditSearch.text)
    }
  }

  private def auditMessageQuery(from: Date, to: Date, usernames: Seq[String], auditCodes: Seq[String]): Seq[AuditCircumflex] = {
    val a = AuditCircumflex AS "a"
    (SELECT (a.*) FROM a WHERE ((a.created GT from) AND (a.created LT to) AND (a.username IN usernames) AND (a.auditCode IN auditCodes))).list()
  }

  private def auditMessageQuery(from: Date, to: Date, usernames: Seq[String], auditCodes: Seq[String], filter: String): Seq[AuditCircumflex] = {
    val a = AuditCircumflex AS "a"
    (SELECT (a.*) FROM a WHERE ((a.created GT from) AND (a.created LT to) AND (a.username IN usernames) AND (a.auditCode IN auditCodes) AND (a.auditMessage LIKE "%" + filter + "%"))).list()
  }
}
