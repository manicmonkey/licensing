package com.magmanics.licensing.ui.content.activation

import org.slf4j.LoggerFactory
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.AbstractComponent._
import com.vaadin.ui.AbstractSelect._
import com.vaadin.ui.Table._
import scala.math._
import com.vaadin.data.Property
import reflect.BeanInfo
import java.util.Date
import com.vaadin.ui.{Table, AbstractSelect}
import com.magmanics.licensing.service.model.Activation

/**
 * @author jbaxter - 22/04/11
 */

class ActivationOverviewTable extends Table {

  val log = LoggerFactory.getLogger(classOf[ActivationOverviewTable])

  val container = new BeanItemContainer[Activation](classOf[Activation])
  setContainerDataSource(container)

  setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY)
  setVisibleColumns(Array("created", "machineIdentifier", "productVersion", "activationType"))
  setColumnHeaders(Array("Created", "Machine Identifier", "Product version", "Activation type"))

  setSelectable(true)
  setNullSelectionAllowed(false)

  setImmediate(true)

  def setActivations(activations: Seq[Activation]) {

    container.removeAllItems()

    activations.foreach(container.addBean(_))

    if (container.size > 0) {
      select(firstItemId)
      setPageLength(min(container.size, 4))
    }
  }

  def addListener(activationSelectionListener: Activation => Unit) {
    addListener(new Property.ValueChangeListener() {
      override def valueChange(event: Property.ValueChangeEvent) {
        val activation = event.getProperty.getValue.asInstanceOf[Activation]
        assert(activation != null, "Null selection not allowed")
        activationSelectionListener(activation)
      }
    })
  }
}