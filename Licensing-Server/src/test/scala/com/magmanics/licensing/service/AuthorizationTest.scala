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
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 20-Sep-2010
 */

class AuthorizationTest extends TransactionalSpringBasedSuite with GivenWhenThen {

  val users = Set("createCustomer", "updateCustomer", "createProduct", "updateProduct", "createConfiguration", "updateConfiguration")

  lazy val authenticationManager = context.getBean(classOf[AuthenticationManager])
  lazy val customerRepository = context.getBean(classOf[CustomerRepository])

  def contextLocation = "spring/authorization-test.xml"

  feature("Authentication is required to access certain features") {
    scenario("Trying to create a Customer") {
      Given("A new Customer")
      val c = Customer(name = "DHL")
      When("We try to create it using different accounts")
      Then("Only the account with the correct privilege will succeed")

      users.filterNot(_.equals("createCustomer")).foreach(username => {
        login(username, "password")
        intercept[AccessDeniedException] {
          customerRepository.create(Customer(name = "DHL"))
        }
      })

      login("createCustomer", "password")
      customerRepository.create(Customer(name = "DHL"))

      /**
       * order auth tests

      CREATE_CUSTOMER
      UPDATE_CUSTOMER

      CREATE_PRODUCT
      UPDATE_PRODUCT

      CREATE_CONFIGURATION
      UPDATE_CONFIGURATION

      CAN_ACTIVATE
       */
    }
  }

  def login(name: String, password: String) {
    val request: Authentication = new UsernamePasswordAuthenticationToken(name, password);
    val result: Authentication = authenticationManager.authenticate(request);
    SecurityContextHolder.getContext().setAuthentication(result);
  }
}