package com.magmanics.licensing.ui.content.auditing

import scala.collection.JavaConversions._
import com.magmanics.auditing.service.AuditService
import org.springframework.beans.factory.annotation.Autowired
import com.magmanics.vaadin.spring.{VaadinApplicationObjectSupport, VaadinComponent}
import org.slf4j.LoggerFactory
import com.magmanics.licensing.ui.content.user.TableWithCheckboxes

/**
 * @author jbaxter - 19/06/11
 */
@VaadinComponent
class AuditCodeSelectionTable @Autowired() (auditService: AuditService, messageSource: VaadinApplicationObjectSupport) extends TableWithCheckboxes {

  val log = LoggerFactory.getLogger(classOf[AuditCodeSelectionTable])

  setSelectable(true)
  setMultiSelect(true)
  setImmediate(true)
  setPageLength(5)

  override def containerProperties = List(("auditcode", classOf[String], "", "Audit code", null, null))

  override def itemRows = auditService.getAuditCodes().map(u => (Array(messageSource.getMessage(u.value)), u.value))

  //select all
  setValue(getItemIds)

  def getAuditCodes() = {
    val codes = getValue.asInstanceOf[java.util.Set[String]]
    codes.toSeq.sortBy(s => s)
  }
}