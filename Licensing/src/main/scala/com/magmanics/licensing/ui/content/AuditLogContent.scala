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

package com.magmanics.licensing.ui.content

import java.util.Date

import com.magmanics.auditing.service.{AuditSearchDto, AuditService}
import com.magmanics.licensing.ui.content.auditing.{AuditCodeSelectionTable, AuditTable}
import com.magmanics.licensing.ui.content.user.UserSelectionTable
import com.magmanics.vaadin.component.{HtmlLabel, UndefinedWidth}
import com.magmanics.vaadin.spring.VaadinComponent
import com.vaadin.data.util.ObjectProperty
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui._
import org.joda.time.DateMidnight
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author jbaxter - 17/06/11
 */
@VaadinComponent
class AuditLogContent @Autowired() (auditTable: AuditTable, auditService: AuditService, userSelectionTable: UserSelectionTable, auditCodeSelectionTable: AuditCodeSelectionTable) extends MainContent {

  val log = LoggerFactory.getLogger(classOf[AuditLogContent])

  val fromDate = new PopupDateField(new ObjectProperty[Date](new DateMidnight().minusMonths(1).toDate))
  val toDate = new PopupDateField(new ObjectProperty[Date](new Date))
  val textSearchField = new TextField()

  fromDate.setDateFormat("dd/MMM/yy")
  toDate.setDateFormat("dd/MMM/yy")
  fromDate.setResolution(DateField.RESOLUTION_DAY)
  toDate.setResolution(DateField.RESOLUTION_DAY)

  val filterButton = new Button("Filter")
  filterButton.addListener(new ClickListener() {
    def buttonClick(event: Button#ClickEvent) {
      updateAudits()
    }
  })

  //lay it all out
  addComponent(new HtmlLabel("<h3>Filtering</h3>"))
  addComponent(new HorizontalLayout {
    setSpacing(true)
    addComponent(new GridLayout(2, 2) {
      addComponent(new Label("From:") with UndefinedWidth, 0, 0)
      addComponent(fromDate, 1, 0)
      addComponent(new Label("To:") with UndefinedWidth, 0, 1)
      addComponent(toDate, 1, 1)
    })
    addComponent(new Label("Users:") with UndefinedWidth)
    addComponent(userSelectionTable)
    addComponent(new Label("Audit codes:") with UndefinedWidth)
    addComponent(auditCodeSelectionTable)
    addComponent(new Label("Message:") with UndefinedWidth)
    addComponent(textSearchField)
  })
  addComponent(new HorizontalLayout {
    setWidth("100%")
    addComponent(filterButton)
    setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT)
  })
  addComponent(new HtmlLabel("<hr/>"))
  addComponent(new HtmlLabel("<h3>Logs</h3>"))
  addComponent(new HorizontalLayout {
    addComponent(auditTable)
  })

  def updateAudits() {
    val searchDto = AuditSearchDto(
      fromDate.getValue.asInstanceOf[Date],
      toDate.getValue.asInstanceOf[Date],
      userSelectionTable.getUsers(),
      auditCodeSelectionTable.getAuditCodes(),
      textSearchField.getValue.asInstanceOf[String])
    val audits = auditService.getAuditMessages(searchDto)
    auditTable.setAudits(audits)
  }

  //let's get the audit table populated with the values we have displayed
  updateAudits()
}