package com.magmanics.licensing.ui.content.product

import com.magmanics.licensing.client.{ClientFactory, ProductClient}
import com.magmanics.licensing.model.Product
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.AbstractSelect.ItemCaptionMode
import com.vaadin.ui.ComboBox

/**
 * @author James Baxter - 23/09/2014.
 */
class ProductComboBox extends ComboBox {
  setInputPrompt("Select a product...")
  val container = new BeanItemContainer[Product](classOf[Product])
  setContainerDataSource(container)
  setItemCaptionMode(ItemCaptionMode.PROPERTY)
  setItemCaptionPropertyId("name")
  setNullSelectionAllowed(false)
  //      setTextInputAllowed(false)

  val products = ClientFactory.getProductClient.get()
  products.sortBy(_.name).foreach(container.addBean)

  def onProductChanged(handler: Product => Unit) {
    addValueChangeListener(new ValueChangeListener {
      override def valueChange(event: ValueChangeEvent) {
        handler(event.getProperty.getValue.asInstanceOf[Product])
      }
    })
  }
}
