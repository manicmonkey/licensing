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
package com.magmanics.licensing.service

import com.magmanics.licensing.TransactionalSpringBasedSuite
import com.magmanics.licensing.model.{Configuration, Customer, Product}
import org.scalatest.GivenWhenThen
import org.slf4j.LoggerFactory

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 15-Aug-2010
 */
class ConfigurationRepositoryIntegrationTest extends TransactionalSpringBasedSuite with GivenWhenThen {

  val log = LoggerFactory.getLogger(classOf[ConfigurationRepositoryIntegrationTest])

  def contextLocation = "spring/integration-test.xml"

  lazy val configurationRepository = context.getBean(classOf[ConfigurationRepository])

  //test data
  var customer: Customer = _
  var customer2: Customer = _
  var product: Product = _

  override protected def beforeAll() = {
    super.beforeAll()
    customer = context.getBean(classOf[CustomerRepository]).create(Customer(name = "DHL"))
    customer2 = context.getBean(classOf[CustomerRepository]).create(Customer(name = "Odwalla"))
    product = context.getBean(classOf[ProductRepository]).create(Product(name = "JFinder V3"))
  }

  feature("Configurations can be listed by customer") {
    scenario("There are configurations for a customer") {
      Given("some existing configurations")
      val configuration = configurationRepository.create(Configuration("lees", product, customer))
      configurationRepository.create(Configuration("mattf", product, customer2))

      When("the list of customers is acquired")
      val configurations = configurationRepository.getByCustomer(customer.name)

      Then("only the relevant configurations will be found")
      assert(configurations.nonEmpty)
      assert(configurations.size == 1)
      assert(configurations.contains(configuration))
    }
    scenario("There are no configurations for a user") {
      Given("there are some activations for a customer")
      configurationRepository.create(Configuration("shaunl", product, customer2))
      configurationRepository.create(Configuration("andrewc", product, customer2))

      When("the list of configurations for another customer is acquired")
      val configurations = configurationRepository.getByCustomer(customer.name)

      Then("the list will not contain any of the defined configurations")
      assert(configurations.isEmpty)
    }
  }

  feature("Configurations can be created") {
    scenario("Created with option values specified") {
      Given("a new configuration with options")
      val configuration = Configuration(user = "jbaxter", options = Map(("maximum.users", "10"), ("minimum.software.version", "2.0")), productId = product.id.get, customerId = customer.id.get)

      When("the configuration is saved")
      val savedConfiguration = configurationRepository.create(configuration)

      Then("it should be retrievable and still have the given options")
      val retrievedConfiguration = configurationRepository.getByCustomer(customer.name).find(_.id.get == savedConfiguration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.options.nonEmpty)
      assert(retrievedConfiguration.get.options.size == 2)
      assert(retrievedConfiguration.get.options.isDefinedAt("maximum.users"))
      assert(retrievedConfiguration.get.options.isDefinedAt("minimum.software.version"))
    }
    scenario("Created without options values specified") {
      Given("a new configuration without options")
      val configuration = Configuration("jbaxter", product, customer)

      When("the configuration is saved")
      val savedConfiguration = configurationRepository.create(configuration)

      Then("it should be retrievable and still have no options")
      val retrievedConfiguration = configurationRepository.getByCustomer(customer.name).find(_.id.get == savedConfiguration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.options.isEmpty)
    }
  }

  feature("Configurations can be updated in limited ways") {
    scenario("Changing the maximum number of activations") {
      Given("an existing configuration")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", maxActivations = 2, productId = product.id.get, customerId = customer.id.get))

      When("the maximum number of activations is changed and saved")
      configurationRepository.update(configuration copy (maxActivations = 3))

      Then("the retrieved configuration will show the change is persistent")
      val retrievedConfiguration = configurationRepository.getByCustomer(customer.name).find(_.id.get == configuration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.maxActivations == 3)
    }
    scenario("Disabling a configuration") {
      Given("an existing enabled configuration")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", enabled = true, productId = product.id.get, customerId = customer.id.get))

      When("the configuration is disabled and saved")
      configurationRepository.update(configuration copy (enabled = false))

      Then("the retrieved configuration will still be disabled")
      val retrievedConfiguration = configurationRepository.getByCustomer(customer.name).find(_.id.get == configuration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(!retrievedConfiguration.get.enabled)
    }
    scenario("Enabling a configuration") {
      Given("an existing disabled configuration")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", enabled = false, productId = product.id.get, customerId = customer.id.get))

      When("the configuration is enabled and saved")
      configurationRepository.update(configuration copy (enabled = true))

      Then("the retrieved configuration will still be enabled")
      assert(configurationRepository.get(configuration.id.get).enabled)
    }
  }
}