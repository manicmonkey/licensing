package com.magmanics.licensing.ui.content

import com.magmanics.licensing.ui.content.product.{ProductOptionViewModel, ProductOptionDialog, ProductOptionsTable, ProductComboBox}
import com.magmanics.vaadin.{ValueChangeListener, ClickHandler}
import com.magmanics.vaadin.component.HtmlLabel
import com.vaadin.ui._

/**
 * @author James Baxter - 07/09/2014.
 */
class ProductContent extends MainContent {

  private val newProductButton = new Button("New product")
  private val productDropdown = new ProductComboBox()

  private val productName = new TextField()
  private val productEnabled = new CheckBox()
  private val optionsTable = new ProductOptionsTable()
  private val newOptionButton = new Button("New")
  newOptionButton.setEnabled(false)
  private val modifyOptionButton = new Button("Modify")
  modifyOptionButton.setEnabled(false)

  private val resetButton = new Button("Reset")
  private val saveButton = new Button("Save")

  private val optionLayout = new GridLayout(3, 3)
  optionLayout.addComponent(new Label("Name"), 0, 0)
  optionLayout.addComponent(productName, 1, 0)
  optionLayout.addComponent(new Label("Enabled"), 0, 1)
  optionLayout.addComponent(productEnabled, 1, 1)
  optionLayout.addComponent(new Label("Options"), 0, 2)
  optionLayout.addComponent(optionsTable, 1, 2)
  private val optionModifications = new VerticalLayout(newOptionButton, modifyOptionButton)
  optionModifications.setSpacing(true)
  optionLayout.addComponent(optionModifications, 2, 2)
  optionLayout.setSpacing(true)

  private val topRow = new HorizontalLayout(newProductButton, productDropdown)
  topRow.setSpacing(true)

  private val saveOptions = new HorizontalLayout(resetButton, saveButton)
  saveOptions.setSpacing(true)

  addComponents(
    topRow,
    new HtmlLabel("<hr/>"),
    optionLayout,
    saveOptions
  )
  setSpacing(true)
  
  productDropdown.onProductChanged(p => {
    productName.setValue(p.name)
    productEnabled.setValue(p.enabled)
    optionsTable.setProductOptions(p.options)
    newOptionButton.setEnabled(true)
  })

  optionsTable.addValueChangeListener(new ValueChangeListener[ProductOptionViewModel](o => modifyOptionButton.setEnabled(o.nonEmpty)))

  newOptionButton.addClickListener(new ClickHandler(_ => {
    val dialog = new ProductOptionDialog(None)
    dialog.center()
    getUI.addWindow(dialog)
  }))

  modifyOptionButton.addClickListener(new ClickHandler(_ => {
    val dialog = new ProductOptionDialog(Some(optionsTable.getSelected))
    dialog.center()
    getUI.addWindow(dialog)
  }))
}

object ProductContent {
  val name = "products"
}
