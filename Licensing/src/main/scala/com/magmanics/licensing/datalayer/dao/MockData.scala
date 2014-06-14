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

import com.magmanics.licensing.datalayer.model._
import com.magmanics.licensing.service.model.ActivationType
import java.util.{Calendar, Date, UUID}
import com.magmanics.auditing.model.Audit._
import com.magmanics.auditing.model.AuditCode._
import com.magmanics.auditing.model.{Audit, AuditCode, AuditCircumflex}

/**
 * @author jbaxter - 12-Jun-2010
 */
object MockData {

  def getUUID: String = {
    UUID.randomUUID.toString
  }

  def createProduct(name: String, description: String, enabled: Boolean) = {
    val product = new ProductCircumflex
    product.name := name
    product.description := description
    product.enabled := enabled
    product.save
    product
  }

  def insert {
    val product = createProduct("JFinder", "Picks up files, strips hash commands and uploads to PDM", true)
    buildListProductOption(product, "Printers", List("10", "20", "40", "80", "160", "320"), "20")
    buildRadioProductOption(product, "PDF Signing", false)
    buildRadioProductOption(product, "Schedule and Sort", true)

    val product2 = createProduct("PDM", "EFS Archive solution", true)
    buildTextProductOption(product2, "Internal reference", "EFS_XXX")
    buildRadioProductOption(product2, "Doc import", true)
    buildListProductOption(product2, "Users", List("1", "2", "5", "10", "25", "50", "100"), "10")

    createProduct("V4", "Product Suite", false)

    val customer = createCustomer("Edwards")
    val customer2 = createCustomer("WD40")
    createCustomer("Imperial Tobacco")
    createCustomer("Wincanton")
    createCustomer("DHL")
    createCustomer("Odwalla")
    createCustomer("Cambridge Assessment")
    createCustomer("Medibureau")
    createCustomer("Investec")
    createCustomer("Special Metals")
    createCustomer("Jordans")
    createCustomer("Metro Bank")
    createCustomer("OU", false)
    createCustomer("Hendersons", false)
    createCustomer("Gates", false)

    val licenceConfiguration = buildConfiguration(true, 1, product, customer, "jbaxter")
    val licenceConfiguration2 = buildConfiguration(true, 1, product2, customer2, "matth")
    val licenceConfiguration3 = buildConfiguration(true, 2, product2, customer2, "lees")

    val activation = buildActivation("jbaxter", "100608", ActivationType.NEW, licenceConfiguration)
    val activation2 = buildActivation("jbaxter", "110715", ActivationType.UPGRADE, licenceConfiguration)
    addActivationInfo(activation, Map("hostname" -> "dev0", "memory" -> "8GB", "diskspace" -> "2TB"))
    addActivationInfo(activation2, Map("hostname" -> "devi8", "memory" -> "3.25GB", "operating.system" -> "Windows 7 x86"))

    addAudits
  }

  private def addActivationInfo(activation: ActivationCircumflex, info: Map[String, String]) {
    info.foreach(pair => {
      val activationInfo = new ActivationInfoCircumflex
      activationInfo.key := pair._1
      activationInfo.value := pair._2
      activationInfo.activation := activation
      activationInfo.save
    })
  }

  private def createCustomer(customerName: String, enabled: Boolean = true): CustomerCircumflex = {
    val customer = new CustomerCircumflex
    customer.name := customerName
    customer.enabled := enabled
    customer.save
    customer
  }

  private def buildConfiguration(enabled: Boolean, maxActivations: Int, product: ProductCircumflex, customer: CustomerCircumflex, username: String): ConfigurationCircumflex = {
    val licenceConfiguration = new ConfigurationCircumflex
    licenceConfiguration.enabled := enabled
    licenceConfiguration.serial := getUUID
    licenceConfiguration.maxActivations := maxActivations
    licenceConfiguration.product := product
    licenceConfiguration.customer := customer
    licenceConfiguration.user := username
    licenceConfiguration.save

    product.getOptions.foreach(c => {
      val po = new ConfigurationOptionCircumflex
      po.key := c.getLabel
      po.value := c.getDefault.toString
      po.configuration := licenceConfiguration
      po.save
    })

    licenceConfiguration
  }

  private def buildActivation(machineIdentifier: String, productVersion: String, activationType: ActivationType.Value, licenceConfiguration: ConfigurationCircumflex) = {
    val licenceActivation = new ActivationCircumflex
    licenceActivation.machineIdentifier := machineIdentifier
    licenceActivation.productVersion := productVersion
    licenceActivation.activationType := activationType.toString
    licenceActivation.configuration := licenceConfiguration
    licenceActivation.save
    licenceActivation
  }

  private def buildTextProductOption(product: ProductCircumflex, name: String, default: String) {
    val textOption = new TextProductOptionCircumflex
    textOption.name := name
    textOption.default := default
    textOption.product := product
    textOption.save
  }

  private def buildRadioProductOption(product: ProductCircumflex, name: String, default: Boolean) {
    val radioOption = new RadioProductOptionCircumflex
    radioOption.name := name
    radioOption.default := default
    radioOption.product := product
    radioOption.save
  }

  private def buildListProductOption(product: ProductCircumflex, name: String, options: List[String], default: String) {
    val listOption = new ListProductOptionCircumflex
    listOption.name := name
    listOption.default := default
    listOption.product := product
    listOption.save

    addListElements(listOption, options)
  }

  private def addListElements(listOption: ListProductOptionCircumflex, listElements: List[String]) {
    listElements.foreach(new ListProductOptionValueCircumflex(_, listOption).save)
  }

  private def addAudits {
    (0 until 1000).flatMap(i => {
      val cal = Calendar.getInstance()
      cal.add(Calendar.HOUR, i * 2)
      List(
        Audit(cal.getTime, "jbaxter", AuditCode("audit.application.login"), "User 'jbaxter' logged in"),
        Audit(cal.getTime, "rduke", AuditCode("audit.configuration.get"), "User 'rduke' created a document with cuk 'fear-and-loathing'"),
        Audit(cal.getTime, "paulh", AuditCode("audit.customer.create"), "User 'paulh' modified the search 'Invoice Search'"),
        Audit(cal.getTime, "geoffvl", AuditCode("audit.product.activation"), "User 'geoffvl' activated the product 'Value Adjustment'"),
        Audit(cal.getTime, "mdamon", AuditCode("audit.products.getEnabled"), "User 'mdamon' retrieved all enabled products"),
        Audit(cal.getTime, "hunterst", AuditCode("audit.product.update"), "User 'hunterst' updated the product 'PO Approval'")
      )
    }).foreach(a => {
      val audit = new AuditCircumflex
      audit.auditCode := a.auditCode.value
      audit.auditMessage := a.auditMessage
      audit.created := a.created
      audit.username := a.username
      audit.save
    })
  }
}