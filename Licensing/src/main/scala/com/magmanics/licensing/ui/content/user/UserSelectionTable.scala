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

package com.magmanics.licensing.ui.content.user

import com.magmanics.auditing.service.AuditService
import com.magmanics.vaadin.ValueChangeListener
import com.magmanics.vaadin.spring.VaadinComponent
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.terminal.Resource
import com.vaadin.ui.{CheckBox, Table}
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConversions._

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