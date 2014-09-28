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
import com.magmanics.licensing.model.{BoolOption, ListOption, Product, TextOption}
import org.scalatest.GivenWhenThen

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 14-Aug-2010
 */
class ProductRepositoryIntegrationTest extends TransactionalSpringBasedSuite with GivenWhenThen { //focus on high level

  def contextLocation = "spring/integration-test.xml"

  lazy val productRepository = context.getBean(classOf[ProductRepository])

  feature("Products can be created, saved, retrieved and disabled") {
    info("As an admin user I should be manage products")
    scenario("attempting to list products when there are none") {
      Given("there are no products in the system")

      When("the list of products is retrieved")
      val products = productRepository.get()

      Then("the list will be empty")
      assert(products.isEmpty)
    }
    scenario("a product is enabled when created") {
      Given("a new product")
      When("the product is saved")
      val product = productRepository.create(Product(name = "Ubuntu 10.10", description = "Maverick Meercat"))

      Then("it will appear in the standard listing of products")
      val retrievedProduct = productRepository.getEnabled.find(_.name == product.name).get
      assert(retrievedProduct.description == product.description)
    }

    scenario("a product can be created with options") {
      Given("a new product with options")
      val boolOption = BoolOption(name = "Enable LDAP", default = true)
      val textOption = TextOption(name = "Branding", default = "Funky Fox")
      val listOption = ListOption(name = "Users", default = "20", values = Set("10", "20", "30"))

      When("the product is saved")
      val product = productRepository.create(Product(name = "Teamcity", options = Set(boolOption, textOption, listOption)))

      Then("the product and its options will be retrievable")
      val existingOptions = productRepository.get(product.id.get).get.options
      assert(existingOptions.size == 3)
      existingOptions.foreach {
        case b: BoolOption => assert(b.name == boolOption.name && b.default == boolOption.default)
        case t: TextOption => assert(t.name == textOption.name && t.default == textOption.default)
        case l: ListOption =>
          assert(l.name == listOption.name)
          assert(l.default == listOption.default)
          assert(l.values == listOption.values)
      }
    }

    scenario("a product can be created without options") {
      Given("a new product without options")
      val p = Product(name = "Awkward Anteater")

      When("the product is saved")
      val product = productRepository.create(p)

      Then("the product will have no options when retrieved")
      assert(productRepository.get(product.id.get).get.options.isEmpty)
    }

    scenario("a product can be disabled") {
      Given("an existing enabled product appearing in the standard listing")
      val product = productRepository.create(Product(name = "Ubuntu 10.10", description = "Maverick Meercat"))
      assert(productRepository.getEnabled.exists(_.name == product.name))

      When("the product is disabled")
      product.enabled = false
      productRepository.update(product)

      Then("it will not appear in the standard listing")
      assert(productRepository.getEnabled.find(_.name == product.name).isEmpty)
      assert(productRepository.get().exists(_.name == product.name))
    }
  }

  scenario("a product can be updated") {
    Given("an existing product")
    val product = productRepository.create(Product(name = "Ubuntu 10.04", description = "Lucid Lynx"))
    assert(productRepository.getEnabled.exists(_.name == product.name))

    When("the products name and description is changed and saved")
    product.name = "Ubuntu 10.10" //todo document not using copy as vaadin wont (doc design decisions)
    product.description = "Maverick Meercat"
    productRepository.update(product)

    Then("the product should have the same name and description when is it next retrieved")
    val retrievedProduct = productRepository.getEnabled.find(_.name == product.name).get
    assert(retrievedProduct.description == product.description)
  }
}