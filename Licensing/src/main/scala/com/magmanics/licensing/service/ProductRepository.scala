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

import com.magmanics.auditing.Auditable
import com.magmanics.binding.DataBindPublisher
import com.magmanics.licensing.datalayer.dao.ProductDao
import com.magmanics.licensing.service.model.Product
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Repository for {@link com.magmanics.licensing.service.model.Product Products}. In addition to basic data
 * access, implementations will add method level authentication and auditing.
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 15-Aug-2010
 */
trait ProductRepository {
  /**
   * Create a Product, returning the persistent Product (id populated)
   */
  def create(product: Product): Product

  /**
   * Update the given Product
   */
  def update(product: Product)

  /**
   * Returns enabled Products
   */
  def getEnabled(): Seq[Product]

  /**
   * Returns all Products
   */
  def get(): Seq[Product]

  /**
   * Return the Product with the given id
   */
  def get(id: Long): Option[Product]
}

@PreAuthorize("isAuthenticated()")
class ProductRepositoryImpl(productDao: ProductDao) extends ProductRepository {

  val log = LoggerFactory.getLogger(classOf[ProductRepositoryImpl])

  @PreAuthorize("hasRole('CREATE_PRODUCT')")
  @Auditable("audit.product.create")
  @DataBindPublisher(Array(classOf[Product]))
  def create(product: Product): Product = {
    log.debug("Creating {}", product)
    productDao.create(product)
  }

  @PreAuthorize("hasRole('UPDATE_PRODUCT')")
  @Auditable("audit.product.update")
  @DataBindPublisher(Array(classOf[Product]))
  def update(product: Product) {
    log.debug("Updating {}", product)
    productDao.update(product)
  }

  @Auditable("audit.products.getEnabled")
  def getEnabled(): Seq[Product] = {
    val products = productDao.get.filter(_.enabled)
    log.debug("Got enabled products: {}", products)
    products
  }

  @Auditable("audit.products.get")
  def get(): Seq[Product] = productDao.get

  @Auditable("audit.product.getById")
  def get(id: Long): Option[Product] = productDao.get(id)
}