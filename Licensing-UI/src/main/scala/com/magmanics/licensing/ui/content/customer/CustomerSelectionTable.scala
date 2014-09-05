package com.magmanics.licensing.ui.content.customer

import com.magmanics.licensing.model.Customer
import com.magmanics.vaadin.component.TableWithCheckboxes

import scala.collection.JavaConverters._

/**
 * @author James Baxter - 05/09/2014.
 */
class CustomerSelectionTable(customers: Seq[Customer]) extends TableWithCheckboxes {

  setSelectable(true)
  setMultiSelect(true)
  setImmediate(true)
  setPageLength(5)

  override def containerProperties = List(
    ("customer", classOf[String], "", "Customer", null, null)
  )

  override def itemRows = customers.sortBy(_.name).map(c => Array(c.name) -> c.name)

  def setCustomers(customerIds: Set[Long]) = {
    val newValue = customerIds
      .map(id => customers.find(_.id.get == id).getOrElse(throw new RuntimeException("Could not find customer with id: " + id)))
      .map(_.name)
      .asJava
    setValue(newValue)
  }
}
