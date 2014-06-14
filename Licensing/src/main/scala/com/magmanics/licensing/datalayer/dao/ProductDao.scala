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

import com.magmanics.licensing.service.model.{BoolOption, ListOption, TextOption, Product}
import exception.{DataLayerException, ConstraintException}
import com.magmanics.licensing.datalayer.model._
import com.magmanics.licensing.service.exception.DuplicateNameException
import org.slf4j.LoggerFactory
import ru.circumflex.core.ValidationException

/**
 * DAO for {@link com.magmanics.licensing.service.model.Product Product}s
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 31-May-2010
 */
abstract class ProductDao {
  /**
   * Persists a new Product
   * @return The newly created Product with its id populated to facilitate further operations
   * @throws DuplicateNameException If a Product with the same name already exists
   */
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
   * @throws DuplicateNameException If a Product with the same name already exists
   */
  def update(product: Product)
}

class ProductDaoCircumflex extends ProductDao {

  val log = LoggerFactory.getLogger(classOf[ProductDaoCircumflex])

  import ru.circumflex.orm._
  import com.magmanics.licensing.datalayer.dao.ImplicitCircumflexModelConversion._

  def create(p: Product): Product = {

    log.debug("Creating {}", p)

    if (get(p.name).nonEmpty)
      throw new DuplicateNameException("Cannot create Product as its name is already in use: " + p)

    try {
      val product = new ProductCircumflex
      product.name := p.name
      product.description := p.description
      product.enabled := p.enabled
      product.save()
      //todo how to attach to product? cascade?? new test proj
      //todo load options and delete/recreate

      p.options.foreach(o => {
        o match {
          case b: BoolOption => {
            val radio = new RadioProductOptionCircumflex
            radio.name := b.name
            radio.default := b.default
            radio.product := product
            radio.save()
          }
          case t: TextOption => {
            val text = new TextProductOptionCircumflex
            text.name := t.name
            text.default := t.default
            text.product := product
            text.save()
          }
          case l: ListOption => {
            val list = new ListProductOptionCircumflex
            list.name := l.name
            list.default := l.default
            list.product := product
            list.save()
            l.values.foreach(o => {
              val option = new ListProductOptionValueCircumflex
              option.value := o
              option.listProductOption := list
              option.save()
            })
          }
        }
      })
      
      product

    } catch {
      case ve: ValidationException => throw new ConstraintException(ve)
      case e: Exception => throw new DataLayerException(e)
    }
  }

  def get(): Seq[Product] = {
    log.debug("Getting all Products")
    val p = ProductCircumflex AS "p"
    (SELECT(p.*) FROM (p) ORDER_BY (p.name)).list()
  }

  def getEnabled(): Seq[Product] = {
    log.debug("Getting all enabled Products")
    get.filter(_.enabled)
  }

  def get(id: Long): Option[Product] = {
    getCircumflex(id)
  }

  private def get(name: String): Option[Product] = {
    log.debug("Getting Product with name: {}", name)
    val p = ProductCircumflex AS "p"
    SELECT (p.*) FROM (p) WHERE (p.name EQ name) unique
  }

  private def getCircumflex(id: Long): Option[ProductCircumflex] = {
    log.debug("Getting Product with id: {}", id)
    val p = ProductCircumflex AS "p"
    SELECT (p.*) FROM (p) WHERE (p.PRIMARY_KEY EQ id) unique
  }

  def update(p: Product) {

    log.debug("Updating {}", p)

    val id = p.id.getOrElse(
      throw new IllegalStateException("Cannot update Product as it does not have an id: " + p))

    val product = getCircumflex(id).getOrElse(
      throw new IllegalStateException("Cannot update Product as could not find existing record with same id: " + p))

    val existingProduct = get(p.name)
    if (existingProduct.nonEmpty && existingProduct.get.id != p.id)
      throw new DuplicateNameException("Cannot create Product as its name is already in use: " + p)

    product.name := p.name
    product.description := p.description
    product.enabled := p.enabled
    //todo how to attach to product? cascade?? new test proj
    //todo load options and delete/recreate
    p.options.foreach {
      case b: BoolOption =>
        val radio = new RadioProductOptionCircumflex
        if (b.id.isDefined) radio.PRIMARY_KEY := b.id.get
        radio.name := b.name
        radio.default := b.default
        product.radioOptions.apply :+ radio
      case t: TextOption =>
        val text = new TextProductOptionCircumflex
        if (t.id.isDefined) text.PRIMARY_KEY := t.id.get
        text.name := t.name
        text.default := t.default
        product.textOptions.apply :+ text
      case l: ListOption =>
        val list = new ListProductOptionCircumflex
        if (l.id.isDefined) list.PRIMARY_KEY := l.id.get
        list.name := l.name
        list.default := l.default
        l.values.foreach(o => {
          val option = new ListProductOptionValueCircumflex
          if (l.id.isDefined) option.PRIMARY_KEY := l.id.get
          option.value := o
          list.values.get :+ option
        })
        product.listOptions.get :+ list
    }

    product.UPDATE()
  }
}