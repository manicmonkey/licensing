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

import com.magmanics.licensing.client.{ProductClient, ConfigurationClient}
import com.magmanics.licensing.ui.content.activation.{ActivationDetailTable, ActivationOverviewTable}
import com.magmanics.licensing.ui.content.configuration.{ConfigurationDetailTable, ConfigurationInfo, ConfigurationOverviewTable}
import com.magmanics.licensing.ui.content.customer.CustomerDropdown
import com.magmanics.vaadin.component.{FullWidth, HtmlLabel, UndefinedWidth}
import com.vaadin.ui._

/**
 * @author jbaxter - 06/04/11
 */
class LicenceManagementContent extends MainContent {

  val configurationClient = ConfigurationClient.client
  val productClient = ProductClient.client

  val configurationOverviewTable = new ConfigurationOverviewTable
  val configurationDetailTable = new ConfigurationDetailTable
  val activationOverviewTable = new ActivationOverviewTable
  val activationDetailTable = new ActivationDetailTable

  val customerDropdown = new CustomerDropdown

  customerDropdown.onCustomerChanged(customer => {
    val configurations = configurationClient.getByCustomer(customer.name)
    configurationOverviewTable.setConfigurations(
      configurations.map(c => {
        val product = productClient.get(c.productId).getOrElse(throw new IllegalStateException("Unknown productId " + c.productId + " in configuration: " + c))
        ConfigurationInfo(c.id.get, product.name, c.created, c.user, c.activations.length.toString, c.serial.get)
      }))
  })

  configurationOverviewTable.onConfigurationChanged(configuration => {
    val c = configurationClient.get(configuration.configurationId)
    configurationDetailTable.setConfiguration(c)
    activationOverviewTable.setActivations(c.activations)
  })
  
  activationOverviewTable.onActivationChanged(activationDetailTable.setActivation)

  addComponent(new HorizontalLayout {
    setSpacing(true)
    addComponent(customerDropdown)
    addComponent(new Button("Create new licence configuration"))
  })
  addComponent(new HtmlLabel("<hr/>"))
  addComponent(new HorizontalLayout {
    setSizeUndefined()
    setSpacing(true)
    addComponent(new VerticalLayout {
      setWidth(null)
      setSpacing(true)
      addComponent(new Button("Disable configuration") with UndefinedWidth)
      addComponent(new Button("Increase activations") with FullWidth)
      addComponent(new Button("Decrease activations") with FullWidth)}
    )
    addComponent(new GridLayout(2, 7) {
      setSizeUndefined()
      setSpacing(true)
      addComponent(new HtmlLabel("<h2>Licence Configurations</h2>") with UndefinedWidth, 0, 0)
      addComponent(new HtmlLabel("<h3>Overview</h3>") with UndefinedWidth, 0, 1)
      addComponent(configurationOverviewTable, 0, 2)
      addComponent(new HtmlLabel("<h3>Product options</h3>") with UndefinedWidth, 1, 1)
      addComponent(configurationDetailTable, 1, 2)
      addComponent(new HtmlLabel("<hr/>"), 0, 3, 1, 3)
      addComponent(new HtmlLabel("<h2>Licence Activations</h2>") with UndefinedWidth, 0, 4)
      addComponent(new HtmlLabel("<h3>Overview</h3>") with UndefinedWidth, 0, 5)
      addComponent(activationOverviewTable, 0, 6)
      addComponent(new HtmlLabel("<h3>Activation info</h3>") with UndefinedWidth, 1, 5)
      addComponent(activationDetailTable, 1, 6)
    })
  })
}