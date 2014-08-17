package com.magmanics.licensing.ui.content.customer

import com.magmanics.licensing.client.CustomerClient
import com.magmanics.licensing.service.model.Customer
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.AbstractSelect.ItemCaptionMode
import com.vaadin.ui.ComboBox

/**
 * @author James Baxter - 17/08/2014.
 */
class CustomerDropdown extends ComboBox {
  setInputPrompt("Select a customer...")
  val container = new BeanItemContainer[Customer](classOf[Customer])
  setContainerDataSource(container)
  setItemCaptionMode(ItemCaptionMode.PROPERTY)
  setItemCaptionPropertyId("name")
  setNullSelectionAllowed(false)
  //      setTextInputAllowed(false)

  val customers = CustomerClient.client.get()
  customers.foreach(container.addBean)

  def onCustomerChanged(handler: Customer => Unit) {
    addValueChangeListener(new ValueChangeListener {
      override def valueChange(event: ValueChangeEvent) {
        handler(event.getProperty.getValue.asInstanceOf[Customer])
      }
    })
  }
}
