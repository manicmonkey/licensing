package com.magmanics.licensing.service

import model.{Product, Customer, Configuration}
import org.scalatest.GivenWhenThen
import com.magmanics.licensing.TransactionalSpringBasedSuite
import ru.circumflex.orm._
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.util.ContextInitializer
import org.slf4j.impl.StaticLoggerBinder
import ch.qos.logback.classic.LoggerContext

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 15-Aug-2010
 */
class ConfigurationRepositoryIntegrationTest extends TransactionalSpringBasedSuite with GivenWhenThen {

  val log = LoggerFactory.getLogger(classOf[ConfigurationRepositoryIntegrationTest])

  def contextLocation = "spring-integration-test.xml"

  lazy val configurationRepository = context.getBean(classOf[ConfigurationRepository])

  //test data
  var customer: Customer = _
  var customer2: Customer = _
  var product: Product = _

  override protected def beforeAll() = {
    super.beforeAll
    customer = context.getBean(classOf[CustomerRepository]).create(Customer(name = "DHL"))
    customer2 = context.getBean(classOf[CustomerRepository]).create(Customer(name = "Odwalla"))
    product = context.getBean(classOf[ProductRepository]).create(Product(name = "JFinder V3"))
    tx.commit
  }

  feature("Configurations can be listed by customer") {
    scenario("There are configurations for a customer") {
      given("some existing configurations")
      val configuration = configurationRepository.create(Configuration("lees", product, customer))
      val configuration2 = configurationRepository.create(Configuration("mattf", product, customer2))

      when("the list of customers is acquired")
      val configurations = configurationRepository.get(customer)

      then("only the relevant configurations will be found")
      assert(configurations.nonEmpty)
      assert(configurations.size == 1)
      assert(configurations.contains(configuration))
    }
    scenario("There are no configurations for a user") {
      given("there are some activations for a customer")
      val configuration = configurationRepository.create(Configuration("shaunl", product, customer2))
      val configuration2 = configurationRepository.create(Configuration("andrewc", product, customer2))

      when("the list of configurations for another customer is acquired")
      val configurations = configurationRepository.get(customer)

      then("the list will not contain any of the defined configurations")
      assert(configurations.isEmpty)
    }
  }

  feature("Configurations can be created") {
    scenario("Created with option values specified") {
      given("a new configuration with options")
      val configuration = Configuration(user = "jbaxter", options = Map(("maximum.users", "10"), ("minimum.software.version", "2.0")), productId = product.id.get, customerId = customer.id.get)

      when("the configuration is saved")
      val savedConfiguration = configurationRepository.create(configuration)

      then("it should be retrievable and still have the given options")
      val retrievedConfiguration = configurationRepository.get(customer).find(_.id.get == savedConfiguration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.options.nonEmpty)
      assert(retrievedConfiguration.get.options.size == 2)
      assert(retrievedConfiguration.get.options.isDefinedAt("maximum.users"))
      assert(retrievedConfiguration.get.options.isDefinedAt("minimum.software.version"))
    }
    scenario("Created without options values specified") {
      given("a new configuration without options")
      val configuration = Configuration("jbaxter", product, customer)

      when("the configuration is saved")
      val savedConfiguration = configurationRepository.create(configuration)

      then("it should be retrievable and still have no options")
      val retrievedConfiguration = configurationRepository.get(customer).find(_.id.get == savedConfiguration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.options.isEmpty)
    }
  }

  feature("Configurations can be updated in limited ways") {
    scenario("Changing the maximum number of activations") {
      given("an existing configuration")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", maxActivations = 2, productId = product.id.get, customerId = customer.id.get))

      when("the maximum number of activations is changed and saved")
      configurationRepository.update(configuration copy (maxActivations = 3))

      then("the retrieved configuration will show the change is persistent")
      val retrievedConfiguration = configurationRepository.get(customer).find(_.id.get == configuration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.maxActivations == 3)
    }
    scenario("Disabling a configuration") {
      given("an existing enabled configuration")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", enabled = true, productId = product.id.get, customerId = customer.id.get))

      when("the configuration is disabled and saved")
      configurationRepository.update(configuration copy (enabled = false))

      then("the retrieved configuration will still be disabled")
      val retrievedConfiguration = configurationRepository.get(customer).find(_.id.get == configuration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.enabled == false)
    }
    scenario("Enabling a configuration") {
      given("an existing disabled configuration")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", enabled = false, productId = product.id.get, customerId = customer.id.get))

      when("the configuration is enabled and saved")
      configurationRepository.update(configuration copy (enabled = true))

      then("the retrieved configuration will still be enabled")
      val retrievedConfiguration = configurationRepository.get(customer).find(_.id.get == configuration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.enabled == true)
    }
  }
}