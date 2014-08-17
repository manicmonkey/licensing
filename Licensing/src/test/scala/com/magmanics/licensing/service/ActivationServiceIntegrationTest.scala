package com.magmanics.licensing.service

import com.magmanics.licensing.TransactionalSpringBasedSuite
import com.magmanics.licensing.service.exception._
import com.magmanics.licensing.service.model.{Configuration, Customer, Product}
import org.scalatest.GivenWhenThen
/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 15 -Aug-2010
 */
class ActivationServiceIntegrationTest extends TransactionalSpringBasedSuite with GivenWhenThen {

  def contextLocation = "spring/integration-test.xml"

  lazy val activationService = context.getBean(classOf[ActivationService])
  lazy val configurationRepository = context.getBean(classOf[ConfigurationRepository])

  //test data
  var customer: Customer = _
  var product: Product = _
  var configuration: Configuration = _

  override protected def beforeAll() = {
    super.beforeAll
    customer = context.getBean(classOf[CustomerRepository]).create(Customer(name = "DHL"))
    product = context.getBean(classOf[ProductRepository]).create(Product(name = "JFinder V3"))
    configuration = configurationRepository.create(Configuration("jbaxter", product, customer))
  }

  feature("Configurations can be activated") {
    scenario("Activating with activations remaining") {
      Given("a configuration with activations remaining")
      val configuration = configurationRepository.create(Configuration("jbaxter", product, customer))

      When("an activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      Then("it should activate successfully")
      activationService.activate(licenceActivationRequest)
    }
    scenario("Attempting to activate a new machine when the limit has been reached") {
      Given("a configuration with no activations remaining")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", maxActivations = 1, productId = product.id.get, customerId = customer.id.get))
      activationService.activate(ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606"))

      When("an activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev1", productVersion = "110606")

      Then("an error will be thrown when attempting activation")
      intercept[NoActivationsLeftException] {
        activationService.activate(licenceActivationRequest)
      }
    }
    scenario("Attempting to activate an existing machine when the limit has been reached") {
      Given("a configuration with no activations remaining")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", maxActivations = 1, productId = product.id.get, customerId = customer.id.get))
      activationService.activate(ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606"))

      When("an upgrade activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      Then("an error will be thrown when attempting activation")
      activationService.activate(licenceActivationRequest)
    }
    scenario("Attempting to activate with an unknown serial number") {
      Given("a new activation request with an unknown serial")
      val licenceActivationRequest = ActivationRequest(serial = "unknown-serial", machineIdentifier = "dev0", productVersion = "110606")

      When("the activation request is received")
      Then("an error will be thrown")
      intercept[NoSuchLicenceException] {
        activationService.activate(licenceActivationRequest)
      }
    }
    scenario("Attempting to activate when the configuration is disabled") {
      Given("a disabled configuration")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", enabled = false, productId = product.id.get, customerId = customer.id.get))

      When("an upgrade activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      Then("an error will be thrown when attempting activation")
      intercept[ConfigurationDisabledException] {
        activationService.activate(licenceActivationRequest)
      }
    }
    scenario("Attempting to activate when the customer is disabled") {
      Given("a configuration with a disabled customer")
      val configuration = configurationRepository.create(Configuration("jbaxter", product, customer))
      context.getBean(classOf[CustomerRepository]).update(customer copy (enabled = false))

      When("an upgrade activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      Then("an error will be thrown when attempting activation")
      intercept[CustomerDisabledException] {
        activationService.activate(licenceActivationRequest)
      }
    }
    scenario("Attempting to activate when the product is disabled") {
      Given("a configuration with a disabled product")
      val configuration = configurationRepository.create(Configuration("jbaxter", product, customer))
      context.getBean(classOf[ProductRepository]).update(product copy (enabled = false))

      When("an upgrade activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      Then("an error will be thrown when attempting activation")
      intercept[ProductDisabledException] {
        activationService.activate(licenceActivationRequest)
      }
    }
  }

  feature("Activation requests can contain additional information about the client") {
    scenario("The activation request contains additional info") {
      Given("an activation with additional information")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606", extraInfo = Map("ram" -> "1024"))

      When("it is activated")
      activationService.activate(licenceActivationRequest)

      Then("the additional information should be retrievable")
      val retrievedConfiguration = configurationRepository.getByCustomer(customer.name).find(_.id.get == configuration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.activations.size == 1)
      assert(retrievedConfiguration.get.activations.head.extraInfo.contains("ram"))
      assert(retrievedConfiguration.get.activations.head.extraInfo("ram") === "1024")
    }
    scenario("The activation request does not contain additional info") {
      Given("an activation without additional information")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      When("it is activated")
      activationService.activate(licenceActivationRequest)

      Then("the saved activation should reflect that")
      val retrievedConfiguration = configurationRepository.getByCustomer(customer.name).find(_.id.get == configuration.id.get)
      assert(retrievedConfiguration.isDefined)
      assert(retrievedConfiguration.get.activations.size === 1)
      assert(retrievedConfiguration.get.activations.head.extraInfo.isEmpty)
    }
  }

  feature("The activation response may contain configuration options") {
    scenario("The configuration does not contain options") {
      Given("a configuration without options")
      val configuration = configurationRepository.create(Configuration("jbaxter", product, customer))

      When("an activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      Then("the activation response will not contain any options")
      val activationResponse = activationService.activate(licenceActivationRequest)
      assert(activationResponse.productOptions.isEmpty)
    }
    scenario("The configuration contains options") {
      Given("a configuration with options")
      val configuration = configurationRepository.create(Configuration(user = "jbaxter", options = Map(("max.users", "10"), ("sso", "1")), productId = product.id.get, customerId = customer.id.get))

      When("an activation request is received")
      val licenceActivationRequest = ActivationRequest(serial = configuration.serial.get, machineIdentifier = "dev0", productVersion = "110606")

      Then("the activation response will contain the options")
      val activationResponse = activationService.activate(licenceActivationRequest)
      assert(activationResponse.productOptions.size === 2)
      assert(activationResponse.productOptions.get("max.users").isDefined)
      assert(activationResponse.productOptions.get("max.users").get === "10")
      assert(activationResponse.productOptions.get("sso").isDefined)
      assert(activationResponse.productOptions.get("sso").get === "1")
    }
  }
}