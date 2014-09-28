package com.magmanics.licensing.ui.content.product

import com.magmanics.vaadin.component.HtmlLabel
import com.magmanics.vaadin.{ValueChangeListener, ClickHandler}
import com.vaadin.ui._
import scala.collection.JavaConverters._

/**
 * @author James Baxter - 28/09/2014.
 */
class ProductOptionDialog(option: Option[ProductOptionViewModel]) extends Window {

  val layout = new VerticalLayout()
  val title = new HtmlLabel("<h3>Product Option</h3>")
  val name = new TextField("Name")
  val types = new ComboBox("Type", List("Text", "Radio", "List").asJava)
  val defaultText = new TextField("Default")
  val defaultRadio = new CheckBox("Default")
  val buttons = new HorizontalLayout()
  val reset = new Button("Reset")
  val save = new Button("Save")

  layout.addComponent(title)
  layout.addComponent(name)
  layout.addComponent(types)
  layout.addComponent(defaultText)
  layout.addComponent(defaultRadio)
  layout.addComponent(buttons)
  layout.setMargin(true)
  layout.setSpacing(true)
  buttons.addComponent(reset)
  buttons.addComponent(save)
  buttons.setMargin(true)
  buttons.setSpacing(true)

  setModal(true)
  setResizable(false)
  setContent(layout)

  initializeState()
  showFieldsForType()

  types.setNullSelectionAllowed(false)
  types.addValueChangeListener(new ValueChangeListener[String](_ => showFieldsForType()))

  reset.addClickListener(new ClickHandler(_ => {
    initializeState()
  }))

  def initializeState() {
    option match {
      case Some(o) =>
        name.setValue(o.name)
        types.setValue(o.`type`)
        o.`type` match {
          case "Text" | "List" => defaultText.setValue(o.default)
          case "Radio" => defaultRadio.setValue(java.lang.Boolean.valueOf(o.default))
        }
      case None =>
        name.setValue("")
        types.setValue("")
        defaultText.setValue("")
        defaultRadio.setValue(false)
    }
  }

  def showFieldsForType() {
    Option(types.getValue) match {
      case Some("Text") =>
        defaultText.setVisible(true)
        defaultRadio.setVisible(false)
      case Some("Radio") =>
        defaultText.setVisible(false)
        defaultRadio.setVisible(true)
      case Some("List") =>
        defaultText.setVisible(true)
        defaultRadio.setVisible(false)
      case _ =>
        defaultText.setVisible(false)
        defaultRadio.setVisible(false)
    }
  }
}
