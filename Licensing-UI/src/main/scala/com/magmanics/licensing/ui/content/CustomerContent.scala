package com.magmanics.licensing.ui.content

import com.magmanics.licensing.client.CustomerClient
import com.magmanics.licensing.ui.content.customer.CustomerTable
import com.magmanics.vaadin.component.HtmlLabel
import com.vaadin.ui._

/**
 * @author James Baxter - 07/09/2014.
 */
class CustomerContent extends MainContent {

  lazy val enabledCustomers = CustomerClient.client.getEnabled(true)
  lazy val customers = CustomerClient.client.get()

  val enabledCustomersLayout = new VerticalLayout(
    new Label("Enabled customers"),
    new CustomerTable(enabledCustomers)
  )
  enabledCustomersLayout.setSpacing(true)

  val disabledCustomersLayout = new VerticalLayout(
    new Label("Disabled customers"),
    new CustomerTable(customers.diff(enabledCustomers))
  )
  disabledCustomersLayout.setSpacing(true)

  val buttonsLayout = new VerticalLayout(
    new Button("Disable >"),
    new Button("< Enable")
  )
  buttonsLayout.setSpacing(true)

  val customerCreationLayout = new HorizontalLayout(
    new Label("Add new customer"),
    new TextField()
  )
  customerCreationLayout.setSpacing(true)

  val modificationLayout = new HorizontalLayout(
    enabledCustomersLayout,
    buttonsLayout,
    disabledCustomersLayout
  )
  modificationLayout.setSpacing(true)

  addComponent(customerCreationLayout)
  addComponent(new HtmlLabel("<hr/>"))
  addComponent(modificationLayout)
  setSpacing(true)
}

object CustomerContent {
  val name = "customers"
}