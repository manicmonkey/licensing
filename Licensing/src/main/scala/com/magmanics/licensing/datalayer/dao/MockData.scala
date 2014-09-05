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

package com.magmanics.licensing.datalayer.dao

import java.util.{Calendar, UUID}

import com.magmanics.auditing.dao.AuditDao
import com.magmanics.auditing.model.{Audit, AuditCode}
import com.magmanics.licensing.model.{Product => LicencedProduct, _}
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.immutable

/**
 * @author jbaxter - 12-Jun-2010
 */
class MockData {

  @Autowired
  var productDao: ProductDao = _

  @Autowired
  var customerDao: CustomerDao = _

  @Autowired
  var configurationDao: ConfigurationDao = _

  @Autowired
  var auditDao: AuditDao = _
  
  def getUUID = UUID.randomUUID.toString

  def insert {
    val options = List(buildListProductOption("Printers", List("10", "20", "40", "80", "160", "320"), "20"), buildRadioProductOption("PDF Signing", default = false), buildRadioProductOption("Schedule and Sort", default = true))
    val product = createProduct("JFinder", "Picks up files, strips hash commands and uploads to PDM", options = options, enabled = true)

    val options2 = List(buildTextProductOption("Internal reference", "MAG_XXX"), buildRadioProductOption("Doc import", default = true), buildListProductOption("Users", List("1", "2", "5", "10", "25", "50", "100"), "10"))
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

    buildActivation("daffy_duck", "100608", licenceConfiguration, Map("hostname" -> "devi8", "memory" -> "3.25GB", "operating.system" -> "Windows 7 x86"))
    buildActivation("daffy_duck", "110715", licenceConfiguration, Map("hostname" -> "dev0", "memory" -> "8GB", "diskspace" -> "2TB"))
    configurationDao.update(licenceConfiguration)

    addAudits()
  }

  def createProduct(name: String, description: String, options: Seq[ProductOption[_]] = List(), enabled: Boolean): LicencedProduct = {
    val product = new LicencedProduct(name = name, description = description, options = options, enabled = enabled)
    productDao.create(product)
  }

  private def buildTextProductOption(name: String, default: String) = {
    new TextOption(name = name, default = default)
  }

  private def buildRadioProductOption(name: String, default: Boolean) = {
    new BoolOption(name = name, default = default)
  }

  private def buildListProductOption(name: String, options: List[String], default: String) = {
    new ListOption(name = name, default = default, values = options)
  }

  private def createCustomer(customerName: String, enabled: Boolean = true): Customer = {
    val customer = new Customer(name = customerName, enabled = enabled)
    customerDao.create(customer)
  }

  private def buildConfiguration(enabled: Boolean, maxActivations: Int, product: LicencedProduct, customer: Customer, username: String, options: Map[String, String] = immutable.Map.empty): Configuration = {
    val licenceConfiguration = new Configuration(enabled = enabled, maxActivations = maxActivations,
      productId =  product.id.get, customerId = customer.id.get, user = username, serial = Some(getUUID),
      options = product.options.map(c => {
        c.name -> options.getOrElse(c.name, c.default.toString)
      }).toMap)
    configurationDao.create(licenceConfiguration)
  }

  private def buildActivation(machineIdentifier: String, productVersion: String, licenceConfiguration: Configuration, info: Map[String, String]) = {
    licenceConfiguration.addActivation(machineIdentifier = machineIdentifier, productVersion = productVersion, extraInfo = info)
  }

  private def addAudits() {
    (0 until 300).flatMap(i => {
      val cal = Calendar.getInstance()
      cal.add(Calendar.HOUR, i * 2)
      List(
        Audit("daffy_duck", AuditCode("audit.application.login"), "User 'daffy_duck' logged in", cal.getTime),
        Audit("old_mac_donald", AuditCode("audit.configuration.get"), "User 'old_mac_donald' created a document with cuk 'fear-and-loathing'"),
        Audit("daffy_duck", AuditCode("audit.customer.create"), "User 'daffy_duck' modified the search 'Invoice Search'"),
        Audit("daffy_duck", AuditCode("audit.product.activation"), "User 'daffy_duck' activated the product 'Value Adjustment'"),
        Audit("santa", AuditCode("audit.products.getEnabled"), "User 'santa' retrieved all enabled products"),
        Audit("old_mac_donald", AuditCode("audit.product.update"), "User 'old_mac_donald' updated the product 'PO Approval'")
      )
    }).foreach(auditDao.create)
  }
}