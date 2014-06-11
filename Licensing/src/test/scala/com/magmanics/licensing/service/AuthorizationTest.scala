package com.magmanics.licensing.service

import model.Customer
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import com.magmanics.licensing.TransactionalSpringBasedSuite
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.scalatest.GivenWhenThen
import org.springframework.security.access.AccessDeniedException

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 20-Sep-2010
 */

class AuthorizationTest extends TransactionalSpringBasedSuite with GivenWhenThen {

  val users = Set("createCustomer", "updateCustomer", "createProduct", "updateProduct", "createConfiguration", "updateConfiguration")

  lazy val authenticationManager = context.getBean(classOf[AuthenticationManager])
  lazy val customerRepository = context.getBean(classOf[CustomerRepository])

  def contextLocation = "spring-authorization-test.xml"

  feature("Authentication is required to access certain features") {
    scenario("Trying to create a Customer") {
      given("A new Customer")
      val c = Customer(name = "DHL")
      when("We try to create it using different accounts")
      then("Only the account with the correct privilege will succeed")

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