package com.magmanics.licensing.ui.content

import com.magmanics.licensing.client.CustomerClient
import com.magmanics.licensing.ui.content.customer.CustomerTable
import com.magmanics.vaadin.ClickHandler
import com.magmanics.vaadin.component.{HtmlLabel, UndefinedWidth}
import com.vaadin.ui._

/**
 * @author James Baxter - 07/09/2014.
 */
class CustomerContent extends MainContent {

  val client = CustomerClient.client

  val customerCreationLayout = new HorizontalLayout(
    new Label("Add new customer"),
    new TextField(),
    new Button("Add")
  )
  customerCreationLayout.setSpacing(true)

  val enabledCustomersTable = new CustomerTable(client.getEnabled(enabled = true))
  val disableButton = new Button("Disable >")
  val enableButton = new Button("< Enable")
  val disabledCustomersTable = new CustomerTable(client.getEnabled(enabled = false))

  disableButton.addClickListener(new ClickHandler(_ => {
    Option(enabledCustomersTable.getSelected).foreach(c => {
      c.enabled = false
      client.update(c)
      updateTables()
    })
  }))
  enableButton.addClickListener(new ClickHandler(_ => {
    Option(disabledCustomersTable.getSelected).foreach(c => {
      c.enabled = true
      client.update(c)
      updateTables()
    })
  }))

  private def updateTables() {
    enabledCustomersTable.setCustomers(client.getEnabled(enabled = true))
    disabledCustomersTable.setCustomers(client.getEnabled(enabled = false))
  }

  val modificationLayout = new GridLayout(3, 3)
  modificationLayout.setSpacing(true)
  modificationLayout.addComponent(new Label("Enabled customers") with UndefinedWidth, 0, 0)
  modificationLayout.addComponent(enabledCustomersTable, 0, 1, 0, 2)
  modificationLayout.addComponent(disableButton, 1, 1)
  modificationLayout.addComponent(enableButton, 1, 2)
  modificationLayout.addComponent(new Label("Disabled customers"), 2, 0)
  modificationLayout.addComponent(disabledCustomersTable, 2, 1, 2, 2)

  addComponent(customerCreationLayout)
  addComponent(new HtmlLabel("<hr/>"))
  addComponent(modificationLayout)
  setSpacing(true)
}

object CustomerContent {
  val name = "customers"
}