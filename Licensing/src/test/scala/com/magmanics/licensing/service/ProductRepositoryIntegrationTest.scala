package com.magmanics.licensing.service

import model.{ListOption, TextOption, BoolOption, Product}
import org.scalatest.{GivenWhenThen}
import com.magmanics.licensing.TransactionalSpringBasedSuite

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 14-Aug-2010
 */
class ProductRepositoryIntegrationTest extends TransactionalSpringBasedSuite with GivenWhenThen { //focus on high level

  def contextLocation = "spring-integration-test.xml"

  lazy val productRepository = context.getBean(classOf[ProductRepository])

  feature("Products can be created, saved, retrieved and disabled") {
    info("As an admin user I should be manage products")
    scenario("attempting to list products when there are none") {
      given("there are no products in the system")

      when ("the list of products is retrieved")
      val products = productRepository.get

      then("the list will be empty")
      assert(products.isEmpty)
    }
    scenario("a product is enabled when created") {
      given("a new product")
      when("the product is saved")
      val product = productRepository.create(Product(name = "Ubuntu 10.10", description = "Maverick Meercat"))

      then("it will appear in the standard listing of products")
      val retrievedProduct = productRepository.getEnabled.find(_.name == product.name).get
      assert(retrievedProduct.description == product.description)
    }

    scenario("a product can be created with options") {
      given("a new product with options")
      val boolOption = BoolOption(name = "Enable LDAP", default = true)
      val textOption = TextOption(name = "Branding", default = "Funky Fox")
      val listOption = ListOption(name = "Users", default = "20", values = List("10", "20", "30"))

      when("the product is saved")
      val product = productRepository.create(Product(name = "Teamcity", options = List(boolOption, textOption, listOption)))

      then("the product and its options will be retrievable")
      val existingOptions = productRepository.get(product.id.get).get.options
      assert(existingOptions.size == 3)
      existingOptions.foreach(
        _ match {
          case b: BoolOption => assert(b.name == boolOption.name && b.default == boolOption.default)
          case t: TextOption => assert(t.name == textOption.name && t.default == textOption.default)
          case l: ListOption => {
            assert(l.name == listOption.name)
            assert(l.default == listOption.default)
            assert(l.values == listOption.values)
          }
        })
    }

    scenario("a product can be created without options") {
      given("a new product without options")
      val p = Product(name = "Awkward Anteater")

      when("the product is saved")
      val product = productRepository.create(p)

      then("the product will have no options when retrieved")
      assert(productRepository.get(product.id.get).get.options.isEmpty)
    }

    scenario("a product can be disabled") {
      given("an existing enabled product appearing in the standard listing")
      val product = productRepository.create(Product(name = "Ubuntu 10.10", description = "Maverick Meercat"))
      assert(productRepository.getEnabled.find(_.name == product.name).isDefined)

      when("the product is disabled")
      product.enabled = false
      productRepository.update(product)

      then("it will not appear in the standard listing")
      assert(productRepository.getEnabled.find(_.name == product.name).isEmpty)
      assert(productRepository.get.find(_.name == product.name).isDefined)
    }
  }

  scenario("a product can be updated") {
    given("an existing product")
    val product = productRepository.create(Product(name = "Ubuntu 10.04", description = "Lucid Lynx"))
    assert(productRepository.getEnabled.find(_.name == product.name).isDefined)

    when("the products name and description is changed and saved")
    product.name = "Ubuntu 10.10" //todo document not using copy as vaadin wont (doc design decisions)
    product.description = "Maverick Meercat"
    productRepository.update(product)

    then("the product should have the same name and description when is it next retrieved")
    val retrievedProduct = productRepository.getEnabled.find(_.name == product.name).get
    assert(retrievedProduct.description == product.description)
  }
}