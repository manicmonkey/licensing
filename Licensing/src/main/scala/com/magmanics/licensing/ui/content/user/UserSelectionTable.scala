package com.magmanics.licensing.ui.content.user

import scala.collection.JavaConversions._
import com.magmanics.auditing.service.AuditService
import com.magmanics.vaadin.spring.VaadinComponent
import org.springframework.beans.factory.annotation.Autowired
import com.magmanics.vaadin.ValueChangeListener
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.ui.{CheckBox, Table}
import com.vaadin.terminal.Resource

trait TableWithCheckboxes extends Table {

  def itemRows: Seq[(Array[_ <: AnyRef], _ <: Any)]

  addContainerProperty("checkbox", classOf[CheckBox], new CheckBox(), "", null, null)
  
  def containerProperties: Seq[(Any, Class[_], Any, String, Resource, String)]
  containerProperties.foreach(i => addContainerProperty(i._1, i._2, i._3, i._4, i._5, i._6))

  val checkboxes: Map[Any, CheckBox] = itemRows.map(item => {
    val checkBox = new CheckBox
    addItem(checkBox +: item._1, item._2)
    item._2 -> checkBox
  }).toMap
  
  addListener(new ValueChangeListener[java.util.Set[AnyRef]](o => {
    val selectedRows = getValue.asInstanceOf[java.util.Set[AnyRef]]
    checkboxes.keySet.foreach(i => checkboxes(i).setValue(selectedRows.contains(i)))
  }))

  checkboxes.keySet.foreach(i => {
    val checkbox = checkboxes(i)
    checkbox.setImmediate(true)
    checkbox.addListener(new Property.ValueChangeListener {
      def valueChange(event: ValueChangeEvent) {
        checkbox.getValue.asInstanceOf[Boolean] match {
          case true => select(i)
          case false => unselect(i)
        }
      }
    })
  })
}

/**
 * @author jbaxter - 18/06/11
 */
@VaadinComponent
class UserSelectionTable @Autowired() (auditService: AuditService) extends TableWithCheckboxes {

  setSelectable(true)
  setMultiSelect(true)
  setImmediate(true)
  setPageLength(5)

  override def containerProperties = List(("username", classOf[String], "", "Username", null, null))

  override def itemRows = auditService.getUsernames().map(u => Array(u) -> u)

  //select all
  setValue(getItemIds)
  
  def setUsers(usernames: Seq[String]) {
    usernames.foreach(u => addItem(Array(new CheckBox, u), u))
  }

  def getUsers() = {
    val users = getValue.asInstanceOf[java.util.Set[String]]
    users.toSeq.sortBy(s => s)
  }
}