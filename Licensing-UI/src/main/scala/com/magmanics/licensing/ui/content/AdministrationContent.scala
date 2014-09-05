/**
 * Magmanics Licensing. This web application allows for centralized control
 * of client application activation, with optional configuration parameters
 * to control licensable features, and storage of supplementary information
 * about the client machine. Client applications may interface with this
 * central server (for activation) using libraries licenced under an
 * alternative licence.
 *
 * Copyright (C) 2010 James Baxter <j.w.baxter(at)gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmanics.licensing.ui.content

import com.magmanics.licensing.client.{CustomerClient, UserClient}
import com.magmanics.licensing.ui.content.customer.CustomerSelectionTable
import com.magmanics.licensing.ui.content.user.{PermissionsSelectionTable, UserSelectionComboBox}
import com.vaadin.ui._

/**
 * @author jbaxter - 13/04/11
 */
class AdministrationContent extends MainContent {

  /**
   * Admin access for...
   * Customers
   * Products
   * Users
   * Auditing
   *
   * todo...
   * create users screen
   * security is working
   * need to expose user information from userendpoint - roles enabled
   * list of users?
   * access to customers (doesn't appear in domain model at moment?)
   */

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

object AdministrationContent {
  val name = "admin"
}