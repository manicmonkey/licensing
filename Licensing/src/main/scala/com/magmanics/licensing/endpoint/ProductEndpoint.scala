package com.magmanics.licensing.endpoint

import com.magmanics.licensing.model.Product
import com.magmanics.licensing.service.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * @author James Baxter - 19/08/2014.
 */
@RestController
@RequestMapping(Array("/products"))
class ProductEndpoint {

  @Autowired
  var productRepository: ProductRepository = _

  /**
   * Create a Product, returning the persistent Product (id populated)
   */
  @RequestMapping(method = Array(RequestMethod.POST))
  def create(product: Product): Product = productRepository.create(product)

  /**
   * Update the given Product
   */
  @RequestMapping(method = Array(RequestMethod.PUT))
  def update(product: Product) = productRepository.update(product)

  /**
   * Returns enabled Products
   */
  @RequestMapping(method = Array(RequestMethod.GET), params = Array("enabled=true"))
  def getEnabled: Seq[Product] = productRepository.getEnabled

  /**
   * Returns all Products
   */
  @RequestMapping(method = Array(RequestMethod.GET))
  def get(): Seq[Product] = productRepository.get()

  /**
   * Return the Product with the given id
   */
  @RequestMapping(method = Array(RequestMethod.GET), params = Array("id"))
  def get(@RequestParam id: Long): Option[Product] = productRepository.get(id)
}
