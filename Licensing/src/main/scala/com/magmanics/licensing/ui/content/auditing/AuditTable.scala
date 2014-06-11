package com.magmanics.licensing.ui.content.auditing

import com.magmanics.auditing.model.Audit
import org.slf4j.LoggerFactory
import com.vaadin.ui._
import java.text.SimpleDateFormat
import com.vaadin.data.util.IndexedContainer
import org.springframework.beans.factory.annotation.Autowired
import com.magmanics.vaadin.spring.{VaadinApplicationObjectSupport, VaadinComponent}

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