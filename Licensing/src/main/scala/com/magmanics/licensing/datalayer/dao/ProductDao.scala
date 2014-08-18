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

package com.magmanics.licensing.datalayer.dao

import javax.persistence.{EntityManager, PersistenceContext}

import com.magmanics.licensing.datalayer.dao.exception.DataLayerException
import com.magmanics.licensing.datalayer.model._
import com.magmanics.licensing.service.exception.DuplicateNameException
import com.magmanics.licensing.service.model.{BoolOption, ListOption, Product, TextOption}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
 * DAO for [[com.magmanics.licensing.service.model.Product Product]]s
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 31-May-2010
 */
trait ProductDao {
  /**
   * Persists a new Product
   * @return The newly created Product with its id populated to facilitate further operations
   */
  @throws[DuplicateNameException]("If a Product with the same name already exists")
  def create(product: Product): Product

  /**
   * Gets all Products within the system
   */
  def get(): Seq[Product]

  /**
   * Get all enabled Products within the system
   */
  def getEnabled(): Seq[Product]

  /**
   * Gets the Product with the given id
   */
  def get(id: Long): Option[Product]

  /**
   *  Update the given Product
   */
  @throws[DuplicateNameException]("If a Product with the same name already exists")
  def update(product: Product)

  /**
   * Deletes the Product with the given id. Ignores missing entities
   */
  def delete(id: Long)
}

class ProductDaoJPA extends ProductDao {

  import com.magmanics.licensing.datalayer.dao.ImplicitDataModelConversion._

  val log = LoggerFactory.getLogger(classOf[ProductDaoJPA])

  @PersistenceContext
  var em: EntityManager = _

  def create(p: Product): Product = {

    log.debug("Creating {}", p)

    if (get(p.name).nonEmpty)
      throw new DuplicateNameException("Cannot create Product as its name is already in use: " + p)

    try {
      val product = new ProductEntity
      product.name = p.name
      product.description = p.description
      product.enabled = p.enabled

      //todo how to attach to product? cascade?? new test proj
      //todo load options and delete/recreate

      p.options.foreach {
        case b: BoolOption =>
          val radio = new RadioProductOptionEntity
          radio.name = b.name
          radio.default = b.default
          radio.product = product
          product.addOption(radio)
        case t: TextOption =>
          val text = new TextProductOptionEntity
          text.name = t.name
          text.default = t.default
          text.product = product
          product.addOption(text)
        case l: ListOption =>
          val list = new ListProductOptionEntity
          list.name = l.name
          list.default = l.default
          list.product = product
          product.addOption(list)
          l.values.foreach(o => {
            val option = new ListProductOptionValueEntity()
            option.value = o
            option.listProductOption = list
            list.addOptionValue(option)
          })
      }

      em.persist(product)
      em.refresh(product)

      product

    } catch {
//      case ve: ValidationException => throw new ConstraintException(ve)
      case e: Exception => throw new DataLayerException(e)
    }
  }

  def get(): Seq[Product] = {
    log.debug("Getting all Products")
    em.createNamedQuery[ProductEntity]("Product.GetAll", classOf[ProductEntity]).getResultList.asScala
  }

  def getEnabled(): Seq[Product] = {
    log.debug("Getting all enabled Products")
    em.createNamedQuery[ProductEntity]("Product.GetEnabled", classOf[ProductEntity]).getResultList.asScala
  }

  def get(id: Long): Option[Product] = {
    getEntity(id)
  }

  private def getEntity(id: Long): Option[ProductEntity] = {
    log.debug("Getting Product with id: {}", id)
    Option(em.find(classOf[ProductEntity], id))
  }

  private def get(name: String): Option[Product] = {
    log.debug("Getting Product with name: {}", name)
    val query = em.createNamedQuery[ProductEntity]("Product.GetByName", classOf[ProductEntity])
    query.setParameter("name", name)
    query.getResultList.asScala.headOption
  }

  def update(p: Product) {

    log.debug("Updating {}", p)

    val id = p.id.getOrElse(
      throw new IllegalStateException("Cannot update Product as it does not have an id: " + p))

    val product = getEntity(id).getOrElse(
      throw new IllegalStateException("Cannot update Product as could not find existing record with same id: " + p))

    val existingProduct = get(p.name)
    if (existingProduct.nonEmpty && existingProduct.get.id != p.id)
      throw new DuplicateNameException("Cannot create Product as its name is already in use: " + p)

    product.name = p.name
    product.description = p.description
    product.enabled = p.enabled
    //todo how to attach to product? cascade?? new test proj
    //todo load options and delete/recreate
    p.options.foreach {
      case b: BoolOption =>
        val radio = new RadioProductOptionEntity
        if (b.id.isDefined) radio.id = b.id.get
        radio.name = b.name
        radio.default = b.default
        product.radioOptions.add(radio)
      case t: TextOption =>
        val text = new TextProductOptionEntity
        if (t.id.isDefined) text.id = t.id.get
        text.name = t.name
        text.default = t.default
        product.textOptions.add(text)
      case l: ListOption =>
        val list = new ListProductOptionEntity
        if (l.id.isDefined) list.id = l.id.get
        list.name = l.name
        list.default = l.default
        l.values.foreach(o => {
          val option = new ListProductOptionValueEntity()
          if (l.id.isDefined) option.id = l.id.get
          option.value = o
          list.optionValues.add(option)
        })
        product.listOptions.add(list)
    }

    em.merge(product)
  }

  def delete(id: Long) {
    log.debug("Deleting Product with id: {}", id)
    getEntity(id).foreach(p => {
      log.debug("Deleting Product: {}", p)
      em.remove(p)
    })
  }
}