package com.magmanics.licensing.ui.content.customer

import com.magmanics.licensing.model.Customer
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.AbstractSelect.ItemCaptionMode
import com.vaadin.ui.Table

import scala.collection.JavaConverters._

/**
 * Displays a list of customers by name
 *
 * @author James Baxter - 10/09/2014.
 */
class CustomerTable(customers: Set[Customer]) extends Table {

  setSelectable(true)
//  setMultiSelect(true)
  setImmediate(true)
  setPageLength(5)

  val container = new BeanItemContainer[Customer](classOf[Customer], customers.toList.sortBy(_.name).asJava)
  setContainerDataSource(container)

  setItemCaptionMode(ItemCaptionMode.PROPERTY)
  setVisibleColumns("name")
  setColumnHeaders("Name")

  setSelectable(true)
  setNullSelectionAllowed(true)

  def setCustomers(customers: Set[Customer]) = {
    container.removeAllItems()
    customers.toList.sortBy(_.name).foreach(container.addBean)
  }

  def getSelected: Customer = {
    getValue.asInstanceOf[Customer]
  }
}
