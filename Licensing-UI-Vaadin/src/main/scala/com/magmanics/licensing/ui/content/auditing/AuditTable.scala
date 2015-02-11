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

package com.magmanics.licensing.ui.content.auditing

import java.text.SimpleDateFormat

import com.magmanics.auditing.model.Audit
import com.magmanics.vaadin.spring.{VaadinApplicationObjectSupport, VaadinComponent}
import com.vaadin.data.util.IndexedContainer
import com.vaadin.ui._
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author jbaxter - 19/06/11
 */
@VaadinComponent
class AuditTable @Autowired() (messageSource: VaadinApplicationObjectSupport) extends Table {

  val log = LoggerFactory.getLogger(classOf[AuditTable])

  val dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

  setContainerDataSource(new IndexedContainer())
  addContainerProperty("created", classOf[String], null, "Created", null, null)
  addContainerProperty("username", classOf[String], null, "User", null, null)
  addContainerProperty("auditCode", classOf[String], null, "Audit Code", null, null)
  addContainerProperty("auditMessage", classOf[String], null, "Audit Message", null, null)

  setPageLength(20)

  def setAudits(audits: Seq[Audit]) {
    getContainerDataSource.removeAllItems()
    audits.foreach(a => {
      val auditCode = messageSource.getMessage(a.auditCode.value)
      addItem(Array(dateFormat.format(a.created), a.username, auditCode, a.auditMessage), a)
    })
  }
}