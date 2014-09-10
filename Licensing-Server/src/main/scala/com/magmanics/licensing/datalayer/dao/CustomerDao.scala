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

import java.sql.SQLException
import javax.persistence.{EntityManager, PersistenceContext}

import com.magmanics.licensing.datalayer.dao.exception.DataLayerException
import com.magmanics.licensing.datalayer.model.CustomerEntity
import com.magmanics.licensing.service.exception.DuplicateNameException
import com.magmanics.licensing.model.Customer
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
 * Dao for [[com.magmanics.licensing.model.Customer Customer]]s
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27-Jul-2010
 */
trait CustomerDao {
  /**
   * Persists a Customer
   * @return The newly created Customer with its id populated to facilitate further operations
   */
  @throws[DuplicateNameException]("If a Customer with the same name already exists")
  def create(customer: Customer): Customer

  /**
   * Gets all Customers within the system
   */
  def get(): Seq[Customer]

  /**
   *  Gets all enabled Customers within the system
   */
  def getEnabled: Seq[Customer]

  /**
   * Gets a Customer by the given id
   */
  def get(id: Long): Option[Customer]

  /**
   * Update the given Customer
   */
  @throws[DuplicateNameException]("If a Customer with the same name already exists")
  def update(customer: Customer)

  /**
   * Delete the Customer with the given id. Ignores missing entities
   */
  def delete(id: Long)
}

/**
 * Circumflex implementation of  [[com.magmanics.licensing.datalayer.dao.CustomerDao CustomerDao]]
 */
class CustomerDaoJPA extends CustomerDao {

  import com.magmanics.licensing.datalayer.dao.ImplicitDataModelConversion._

  val log = LoggerFactory.getLogger(classOf[CustomerDaoJPA])

  @PersistenceContext
  var em: EntityManager = _

  def create(c: Customer): Customer = {

    log.debug("Creating {}", c)

    val customerEntity = new CustomerEntity
    customerEntity.name = c.name
    customerEntity.enabled = c.enabled

    synchronized {
      val query = em.createNamedQuery("Customer.GetByName")
      query.setParameter("name", customerEntity.name)
      query.getResultList.asScala.foreach(_ => throw new DuplicateNameException("Customer with name '" + customerEntity.name + "' already exists"))

      try {
        em.persist(customerEntity)
      } catch {
        case e: SQLException => throw new DataLayerException("Cannot create Customer: " + c, e)
      }
    }

    customerEntity
  }

  def get(): Seq[Customer] = {
    log.debug("Getting all Customers")
    val query = em.createNamedQuery[CustomerEntity]("Customer.GetAll", classOf[CustomerEntity])
    query.getResultList.asScala
  }

  def getEnabled(): Seq[Customer] = {
    log.debug("Getting all enabled Customers")
    val query = em.createNamedQuery[CustomerEntity]("Customer.GetEnabled", classOf[CustomerEntity])
    query.getResultList.asScala
  }

  def get(id: Long): Option[Customer] = {
    getEntity(id)
  }

  def update(c: Customer) {

    log.debug("Updating {}", c)

    val id = c.id.getOrElse(
      throw new IllegalStateException("Cannot update a Customer which has no id: " + c))

    val customer = getEntity(id).getOrElse(
      throw new IllegalStateException("Cannot update Customer as id is unknown: " + c))

    customer.name = c.name
    customer.enabled = c.enabled
    
    em.merge(customer)
  }

  def delete(id: Long) {
    log.debug("Deleting Customer with id: {}", id)
    getEntity(id).foreach(c => {
      log.debug("Deleting Customer: {}", c)
      em.remove(c)
    })
  }

  private def getEntity(id: Long): Option[CustomerEntity] = {
    log.debug("Getting Customer with id: {}", id)
    Option(em.find(classOf[CustomerEntity], id))
  }
}