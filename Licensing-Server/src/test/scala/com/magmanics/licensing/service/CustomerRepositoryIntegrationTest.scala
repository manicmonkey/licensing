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
import com.magmanics.licensing.model.Customer
import org.scalatest.GivenWhenThen

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 10-Aug-2010
 */
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
      val retrievedCustomer = customerService.getEnabled(enabled = true).find(_.name == customer.name)
      assert(retrievedCustomer.isDefined)
    }

    scenario("a customer is created with a duplicate name") {
      Given("two identical customers")
      When("one customer is saved")
      customerService.create(Customer(name = "Customer one"))

      Then("saving the other should throw an error")
      //todo fix this test
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
      assert(customerService.getEnabled(enabled = true).find(_.name == "Customer one").isEmpty)
      assert(customerService.getEnabled(enabled = false).find(_.name == "Customer one").nonEmpty)
    }

    scenario("a customer is enabled") {
      Given("an existing disabled customer")
      val customer = customerService.create(Customer(name = "Customer one", enabled = false))

      When("the customer is enabled and saved")
      customer.enabled = true
      customerService.update(customer)

      Then("the customer should appear in the standard listing")
      val customerOption = customerService.getEnabled(enabled = true).find(_.name == "Customer one")
      assert(customerOption.isDefined)
      assert(customerOption.get == customer)
    }
  }
}