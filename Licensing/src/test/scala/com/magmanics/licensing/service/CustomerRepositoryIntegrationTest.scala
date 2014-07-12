package com.magmanics.licensing.service

import com.magmanics.licensing.TransactionalSpringBasedSuite
import com.magmanics.licensing.service.model.Customer
import org.scalatest.GivenWhenThen

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 10-Aug-2010
 */
//todo these service integration tests should help allow us to substitute the ui without changing anything downwards
class CustomerRepositoryIntegrationTest extends TransactionalSpringBasedSuite with GivenWhenThen { //focus on high level

  def contextLocation = "spring/integration-test.xml"

  lazy val customerService = context.getBean(classOf[CustomerRepository])

  feature("Customers have unique names") {
    info("As a user I should be able to give a customer name and it should be unique")
    scenario("a customer is created with a unique name") {
      Given("a new customer")
      When("the customer is saved")
      val customer = customerService.create(Customer(name = "Customer one"))

      Then("it will appear in the standard listing of customers")
      val retrievedCustomer = customerService.getEnabled.find(_.name == customer.name)
      assert(retrievedCustomer.isDefined)
    }

    scenario("a customer is created with a duplicate name") {
      Given("two identical customers")
      When("one customer is saved")
      customerService.create(Customer(name = "Customer one"))

      Then("saving the other should throw an error")
      //todo disabled as SQLException is not thrown from Circumflex - we get undeclared exception and can't catch it
//      intercept[DuplicateNameException] {
//        customerService.create(Customer(name = "Customer one"))
//      }
    }
  }

  feature("Customers can be enabled and disabled") {
    info("Customers should only appear if enabled")
    scenario("a customer is disabled") {
      Given("an existing enabled customer")
      val customer = customerService.create(Customer(name = "Customer one"))

      When("the customer is disabled and saved")
      customer.enabled = false
      customerService.update(customer)

      Then("the customer should not appear in the standard listing")
      assert(customerService.getEnabled.find(_.name == "Customer one").isEmpty)
    }

    scenario("a customer is enabled") {
      Given("an existing disabled customer")
      val customer = customerService.create(Customer(name = "Customer one", enabled = false))

      When("the customer is enabled and saved")
      customer.enabled = true
      customerService.update(customer)

      Then("the customer should appear in the standard listing")
      val customerOption = customerService.getEnabled.find(_.name == "Customer one")
      assert(customerOption.isDefined)
      assert(customerOption.get == customer)
    }
  }
}