package com.magmanics.licensing.ui.content.activation

import org.slf4j.LoggerFactory
import scala.math._
import com.vaadin.ui.Table
import com.magmanics.licensing.service.model.Activation

/**
 * @author jbaxter - 22/04/11
 */

class ActivationDetailTable extends Table {

  val log = LoggerFactory.getLogger(classOf[ActivationDetailTable])

  addContainerProperty("name", classOf[String], null)
  addContainerProperty("value", classOf[String], null)

  setSizeFull()
  setColumnHeader("name", "Name")
  setColumnHeader("value", "Value")

  def setActivation(activation: Activation) {
    log.debug("Model clicked: {}", activation)
    activation.extraInfo.foreach(info => {
      val item = addItem(info._1)
      item.getItemProperty("name").setValue(info._1)
      item.getItemProperty("value").setValue(info._2)
    })
    requestRepaint()
    setPageLength(min(size, 8))
  }
}