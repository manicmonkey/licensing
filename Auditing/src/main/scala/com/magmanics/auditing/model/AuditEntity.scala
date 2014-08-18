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

package com.magmanics.auditing.model

import java.util.Date
import javax.persistence._

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 20-Sep-2010
 */
@Entity
@Table(name = "audits")
@NamedQueries(Array(
  new NamedQuery (name = "Audit.GetAuditCodes", query = "SELECT DISTINCT a.auditCode FROM AuditEntity a GROUP BY a.auditCode ORDER BY a.auditCode ASC"),
  new NamedQuery (name = "Audit.GetDistinctUsernames", query = "SELECT DISTINCT a.username FROM AuditEntity a GROUP BY a.username ORDER BY a.username ASC")
))
class AuditEntity {
  
  @Id
  @GeneratedValue
  var id: Long = _

  @Temporal(TemporalType.TIMESTAMP)
  var created: Date = _

  @Column(nullable = false)
  var username: String = _

  @Column(nullable = false)
  var auditCode: String = _

  @Column(nullable = false, length = 2000)
  var auditMessage: String = _
}