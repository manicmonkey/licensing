package com.magmanics.licensing.ui.content

import java.util.Date

import com.magmanics.licensing.service.ConfigurationRepository
import com.magmanics.licensing.service.model.{Activation, ActivationType, Configuration, Customer}
import com.magmanics.licensing.ui.component.{FullWidth, HtmlLabel, UndefinedWidth}
import com.magmanics.licensing.ui.content.activation.{ActivationDetailTable, ActivationOverviewTable}
import com.magmanics.licensing.ui.content.configuration.{ConfigurationDetailTable, ConfigurationInfo, ConfigurationOverviewTable}
import com.vaadin.ui._

/**
 * @author jbaxter - 06/04/11
 */
class LicenceManagementContent extends MainContent {

  val configurationRepository = new MockConfigurationRepository

  val configurationOverviewTable = new ConfigurationOverviewTable
  val configurationDetailTable = new ConfigurationDetailTable
  val activationOverviewTable = new ActivationOverviewTable
  val activationDetailTable = new ActivationDetailTable

  addComponent(new HorizontalLayout {
    setSpacing(true)
    addComponent(new ComboBox() {
      setInputPrompt("Select a customer...")
    })
    addComponent(new Button("Create new licence configuraton"))
  })
  addComponent(new HtmlLabel("<hr/>"))
  addComponent(new HorizontalLayout {
    setSizeUndefined()
    setSpacing(true)
    addComponent(new Panel with UndefinedWidth {
      getContent.setWidth(null)
      addComponent(new Button("Disable configuration") with UndefinedWidth)
      addComponent(new Button("Increase activations") with FullWidth)
      addComponent(new Button("Decrease activations") with FullWidth)
    })
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

  configurationOverviewTable.setConfigurations(List(ConfigurationInfo(1, "Ubuntu 10.10", new Date, "jbaxter", "1/2", "gew7yuhb3736yyby73rh8yugbh`"), ConfigurationInfo(1, "Ubuntu 11.04", new Date, "jbaxter", "1/1", "47fr8e473yedf43e")))
//  configurationOverviewTable.setConfigurations(List(ConfigurationInfo(1, "Ubuntu 10.10", new Date, "jbaxter", "1/2", "gew7yuhb3736yyby73rh8yugbhyugyuhbf7f76g67gf76fg76yt5rfd65dy56td65fy6t5fd6tdtrdjhf"), ConfigurationInfo(1, "Ubuntu 11.04", new Date, "jbaxter", "1/1", "47fr8e473yedf43e")))
//  configurationDetailTable.setConfiguration(new Configuration(user = "jbaxter", productId = 1, customerId = 1, options = Map("Samba" -> "enabled", "UPnP" -> "enabled", "EXT3" -> "disabled", "Maximum user accounts" -> "9999")))
  //  activationOverviewTable.setActivations()
  //  activationDetailTable.setActivation()
  override def attach() {
//    configurationOverviewTable.setConfigurations(configurationRepository.get(Customer(name = "Google")))
  }
}

class MockConfigurationRepository extends ConfigurationRepository {

  def get(serial: String) = null

  def get(customer: Customer): Seq[Configuration] = {
    List(new Configuration(id = Some(1), user = "jbaxter", productId = 1, customerId = 1, created = new Date, serial = Some("gew7yuhb3736yyby73rh8yugbh"), options = Map("Samba" -> "enabled", "UPnP" -> "enabled", "EXT3" -> "disabled", "Maximum user accounts" -> "9999"), enabled = true, maxActivations = 2, activations = List(Activation(id = Some(1), created = new Date, machineIdentifier = "devi8", productVersion = "6.921.5", activationType = ActivationType.NEW, extraInfo = Map("Operating system" -> "Ubuntu 10.10", "RAM" -> "12GB")))))
  }

  def get(id: Long) = null

  def update(configuration: Configuration) {}

  def create(configuration: Configuration) = null
}