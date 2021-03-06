package com.magmanics.vaadin.component

import java.util

import com.magmanics.vaadin.ValueChangeListener
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.server.Resource
import com.vaadin.ui.Table.{Align, HeaderClickEvent, HeaderClickListener}
import com.vaadin.ui.{CheckBox, Table}

import scala.collection.JavaConverters._

/**
 * @author James Baxter - 05/09/2014.
 */
trait TableWithCheckboxes extends Table {

  def itemRows: Seq[(Array[_ <: AnyRef], _ <: Any)]

  addContainerProperty("checkbox", classOf[CheckBox], new CheckBox(), "", null, null)

  addHeaderClickListener(new HeaderClickListener {
    override def headerClick(p1: HeaderClickEvent) {
      if ("checkbox".equals(p1.getPropertyId)) {
        val allChecked = checkboxes.values.forall(c => c.getValue)
        checkboxes.values.foreach(_.setValue(!allChecked))
      }
    }
  })

  addValueChangeListener(new ValueChangeListener[java.util.Set[AnyRef]](o => {
    val selectedRows = getValue.asInstanceOf[java.util.Set[AnyRef]]
    checkboxes.keySet.foreach(i => checkboxes(i).setValue(selectedRows.contains(i)))
  }))

  def containerProperties: Seq[(Any, Class[_], Any, String, Resource, Align)]

  containerProperties.foreach(i => addContainerProperty(i._1, i._2, i._3, i._4, i._5, i._6))

  val checkboxes: Map[Any, CheckBox] = itemRows.map(item => {
    val checkBox = new CheckBox
    addItem(checkBox +: item._1, item._2)
    item._2 -> checkBox
  }).toMap

  checkboxes.keySet.foreach(i => {
    val checkbox = checkboxes(i)
    checkbox.setImmediate(true)
    checkbox.addValueChangeListener(new Property.ValueChangeListener {
      def valueChange(event: ValueChangeEvent) {
        checkbox.getValue.asInstanceOf[Boolean] match {
          case true => select(i)
          case false => unselect(i)
        }
      }
    })
  })
}
