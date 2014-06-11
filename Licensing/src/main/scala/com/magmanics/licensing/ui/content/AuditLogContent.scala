package com.magmanics.licensing.ui.content

import auditing.{AuditTable, AuditCodeSelectionTable}
import user.UserSelectionTable
import com.vaadin.ui.Button.ClickListener
import com.magmanics.vaadin.spring.VaadinComponent
import org.springframework.beans.factory.annotation.Autowired
import com.magmanics.auditing.service.{AuditService, AuditSearchDto}
import com.vaadin.data.util.ObjectProperty
import org.joda.time.DateMidnight
import java.util.Date
import org.slf4j.LoggerFactory
import com.vaadin.ui._
import com.magmanics.vaadin.component.{UndefinedWidth, HtmlLabel}

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