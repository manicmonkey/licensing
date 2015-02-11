package com.magmanics.licensing

import java.util.{UUID, Calendar}

import com.magmanics.auditing.model.{AuditCode, Audit}
import com.magmanics.licensing.model.{Product => LicencedProduct, _}
import com.magmanics.licensing.client._
import com.magmanics.licensing.model._

import scala.collection.immutable

/**
 * @author James - 03/02/2015.
 */
object InsertMockData extends App {

  val productClient = ClientFactory.getProductClient
  val customerClient = ClientFactory.getCustomerClient
  val configurationClient = ClientFactory.getConfigurationClient
  val auditClient = ClientFactory.getAuditClient

  val options = Set[ProductOption[_]](buildListProductOption("Printers", Set("10", "20", "40", "80", "160", "320"), "20"), buildRadioProductOption("PDF Signing", default = false), buildRadioProductOption("Schedule and Sort", default = true))
  val product = createProduct("JFinder", "Picks up files, strips hash commands and uploads to PDM", options = options, enabled = true)

  val options2 = Set[ProductOption[_]](buildTextProductOption("Internal reference", "MAG_XXX"), buildRadioProductOption("Doc import", default = true), buildListProductOption("Users", Set("1", "2", "5", "10", "25", "50", "100"), "10"))
  val product2 = createProduct("PDM", "Magmanics Archive solution", options = options2, enabled = true)

  createProduct("V4", "Product Suite", enabled = false)

  val customer = createCustomer("Google")
  val customer2 = createCustomer("Netflix")
  createCustomer("Apple")
  createCustomer("Amazon")
  createCustomer("Blockchain")
  createCustomer("Coursera")
  createCustomer("feedly")
  createCustomer("Square")
  createCustomer("Tesla")
  createCustomer("Atlassian")
  createCustomer("Jetbrains")
  createCustomer("LastPass")
  createCustomer("Remember The Milk", enabled = false)
  createCustomer("Mercedes", enabled = false)

  val licenceConfiguration = buildConfiguration(true, 2, product, customer, "jbaxter") //todo fix users
  val licenceConfiguration2 = buildConfiguration(true, 1, product2, customer2, "matth")
  val licenceConfiguration3 = buildConfiguration(true, 2, product2, customer2, "lees", Map("Users" -> "5"))

  buildActivation("eagle_eye_1", "100608", licenceConfiguration, Map("hostname" -> "eagle_eye_1", "memory" -> "3.25GB", "operating.system" -> "Windows 7 x86"))
  buildActivation("eagle_eye_1", "110715", licenceConfiguration, Map("hostname" -> "eagle_eye_1", "memory" -> "8GB", "diskspace" -> "2TB", "operating.system" -> "Windows 8.1"))
  buildActivation("movies_store", "110715", licenceConfiguration2, Map("hostname" -> "movies_store", "memory" -> "4GB", "diskspace" -> "16TB", "operating.system" -> "Windows 8"))
  configurationClient.update(licenceConfiguration)
  configurationClient.update(licenceConfiguration2)

  addAudits()

  private def getUUID = UUID.randomUUID.toString

  private def createProduct(name: String, description: String, options: Set[ProductOption[_]] = Set(), enabled: Boolean): LicencedProduct = {
    val product = new LicencedProduct(name = name, description = description, options = options, enabled = enabled)
    productClient.create(product)
  }

  private def buildTextProductOption(name: String, default: String) = {
    new TextOption(name = name, default = default)
  }

  private def buildRadioProductOption(name: String, default: Boolean) = {
    new BoolOption(name = name, default = default)
  }

  private def buildListProductOption(name: String, options: Set[String], default: String) = {
    new ListOption(name = name, default = default, values = options)
  }

  private def createCustomer(customerName: String, enabled: Boolean = true): Customer = {
    val customer = new Customer(name = customerName, enabled = enabled)
    customerClient.create(customer)
  }

  private def buildConfiguration(enabled: Boolean, maxActivations: Int, product: LicencedProduct, customer: Customer, username: String, options: Map[String, String] = immutable.Map.empty): Configuration = {
    val licenceConfiguration = new Configuration(enabled = enabled, maxActivations = maxActivations,
      productId =  product.id.get, customerId = customer.id.get, user = username, serial = Some(getUUID),
      options = product.options.map(c => {
        c.name -> options.getOrElse(c.name, c.default.toString)
      }).toMap)
    configurationClient.create(licenceConfiguration)
  }

  private def buildActivation(machineIdentifier: String, productVersion: String, licenceConfiguration: Configuration, info: Map[String, String]) = {
    licenceConfiguration.addActivation(machineIdentifier = machineIdentifier, productVersion = productVersion, extraInfo = info)
  }

  private def addAudits() {
    (0 until 50).flatMap(i => {
      val cal = Calendar.getInstance()
      cal.add(Calendar.HOUR, i * 2)
      List(
        Audit("daffy_duck", AuditCode("audit.application.login"), "User 'daffy_duck' logged in", cal.getTime),
        Audit("old_mac_donald", AuditCode("audit.configuration.get"), "User 'old_mac_donald' got a configuration"),
        Audit("daffy_duck", AuditCode("audit.customer.create"), "User 'daffy_duck' modified the customer 'Netflix'"),
        Audit("daffy_duck", AuditCode("audit.product.activation"), "User 'daffy_duck' activated the product 'PDM'"),
        Audit("santa", AuditCode("audit.products.getEnabled"), "User 'santa' retrieved all enabled products"),
        Audit("old_mac_donald", AuditCode("audit.product.update"), "User 'old_mac_donald' updated the product 'JFinder'")
      )
    }).foreach(auditClient.create)
  }
}
