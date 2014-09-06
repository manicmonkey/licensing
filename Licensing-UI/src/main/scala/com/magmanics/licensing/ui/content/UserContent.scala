package com.magmanics.licensing.ui.content

import com.magmanics.licensing.client.{CustomerClient, UserClient}
import com.magmanics.licensing.ui.content.customer.CustomerSelectionTable
import com.magmanics.licensing.ui.content.user.{PermissionsSelectionTable, UserSelectionComboBox}
import com.vaadin.ui.{HorizontalLayout, CheckBox, Label, VerticalLayout}

/**
 * @author James Baxter - 06/09/2014.
 */
class UserContent extends MainContent {

  //define and wire up basic controls
  private val userSelectionComboBox = new UserSelectionComboBox(UserClient.client.getUsers)
  private val permissionSelectionTable = new PermissionsSelectionTable
  private val customerSelectionTable = new CustomerSelectionTable(CustomerClient.client.get())

  userSelectionComboBox.onUserChanged(u => {
    permissionSelectionTable.setPermissions(u.permissions)
    customerSelectionTable.setCustomers(u.customers)
  })

  //place controls in panels
  private val userSelectionPanel = new VerticalLayout(
    new Label("Users"),
    userSelectionComboBox
  )
  private val permissionSelectionPanel = new VerticalLayout(
    new Label("Permissions"),
    permissionSelectionTable
  )
  private val customerSelectionPanel = new VerticalLayout(
    new Label("Customer access"),
    customerSelectionTable,
    new CheckBox("Automatic access to new customers", false)
  )
  customerSelectionPanel.setSpacing(true)

  //place panels in page layout
  private val layout = new HorizontalLayout(
    userSelectionPanel,
    permissionSelectionPanel,
    customerSelectionPanel
  )
  layout.setSpacing(true)
  addComponent(layout)
}

object UserContent {
  val name = "users"
}
