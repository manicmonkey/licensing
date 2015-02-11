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

import com.magmanics.licensing.client.ClientFactory
import com.magmanics.vaadin.component.TableWithCheckboxes
import com.magmanics.vaadin.spring.{VaadinApplicationObjectSupport, VaadinComponent}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConversions._

/**
 * @author jbaxter - 19/06/11
 */
@VaadinComponent
class AuditCodeSelectionTable @Autowired() (messageSource: VaadinApplicationObjectSupport) extends TableWithCheckboxes {

  val log = LoggerFactory.getLogger(classOf[AuditCodeSelectionTable])

  setSelectable(true)
  setMultiSelect(true)
  setImmediate(true)
  setPageLength(5)

  override def containerProperties = List(("auditcode", classOf[String], "", "Audit code", null, null))

  override def itemRows = ClientFactory.getAuditClient.getAuditCodes.map(u => (Array(messageSource.getMessage(u.value)), u.value))

  //select all
  setValue(getItemIds())

  def getAuditCodes = {
    val codes = getValue.asInstanceOf[java.util.Set[String]]
    codes.toSeq.sortBy(s => s)
  }
}