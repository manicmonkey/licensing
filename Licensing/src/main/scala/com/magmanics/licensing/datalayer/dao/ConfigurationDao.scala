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

import com.magmanics.licensing.datalayer.dao.exception.{DataLayerException, NoSuchEntityException}
import com.magmanics.licensing.datalayer.model._
import com.magmanics.licensing.service.model.{Configuration, Customer}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
 * DAO for {@link com.magmanics.licensing.service.model.Configuration Configuration}s.
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27 -Jul-2010
 */
trait ConfigurationDao {
  /**
   * Persist a new Configuration. Does not persist any attached {@link com.magmanics.licensing.service.model.Activation Activations}
   * as none are expected when creating a Configuration.
   * @return The newly created Configuration with its id populated to facilitate future operations
   */
  def create(configuration: Configuration): Configuration //throws ValidationException

  /**
   * Updates the given Configuration, but is limited to:
   * <ul>
   * <li>Changing the maximum number of activations</li>
   * <li>Changing the enabled/disabled status</li>
   * <li>Adding new {@link com.magmanics.licensing.service.model.Activation Activations}s.</li>
   * </ul>
   */
  def update(configuration: Configuration)

  /**
   * Get a Configuration by its id
   * @throws NoSuchEntityException if a Configuration with the given id does not exist
   */
  def get(id: Long): Configuration

  /**
   * Attempts to find a Configuration given a serial
   */
  def getBySerial(serial: String): Option[Configuration]

  /**
   * Gets all Configurations for a given Customer
   */
  def getByCustomer(customer: Customer): Seq[Configuration]
}

/**
 * Circumflex implementation of {@link com.magmanics.licensing.datalayer.dao.ConfigurationDao ConfigurationDao}
 */
class ConfigurationDaoJPA(activationDao: ActivationDao) extends ConfigurationDao {

  import com.magmanics.licensing.datalayer.dao.ImplicitDataModelConversion._

  val log = LoggerFactory.getLogger(classOf[ConfigurationDaoJPA])

  @PersistenceContext
  var em: EntityManager = _

  def create(configuration: Configuration): Configuration = {

    log.debug("Creating {}", configuration)

    if (configuration.serial.isEmpty)
      throw new IllegalArgumentException("Serial number must be specified in configuration: " + configuration)

    try {
      val c = new ConfigurationEntity
      c.created = configuration.created
      c.user = configuration.user
      c.enabled = configuration.enabled
      c.serial = configuration.serial.get
      c.maxActivations = configuration.maxActivations
      c.productId = configuration.productId
      c.customerId = configuration.customerId

      configuration.options.foreach(option => {
        val o = new ConfigurationOptionEntity
        o.configuration = c
        o.key = option._1
        o.value = option._2
        c.addOption(o)
      })

      em.persist(c) //persist the entity
      em.refresh(c) //refresh load associations (ie load product based on product id)

      c
    } catch {
      case e: Exception => throw new DataLayerException(e)
    }
  }

  def update(c: Configuration) {

    log.debug("Updating {}", c)

    val id = c.id.getOrElse(
      throw new IllegalStateException("Cannot update configuration as no id: " + c))

    val configuration = getEntity(id).getOrElse(
      throw new IllegalStateException("Cannot update configuration as could not find id: " + c))

    configuration.maxActivations = c.maxActivations
    configuration.enabled = c.enabled

    c.activations.filter(_.id.isEmpty).foreach(activationDao.create)

    em.merge(configuration)   //save changes
    em.flush()
    // if we don't do a refresh, a em.find won't pick up new associations such as
    // activations (this appears to be caching issue)
    em.refresh(configuration)
  }

  def get(id: Long): Configuration = {
    getEntity(id).getOrElse(throw new NoSuchEntityException)
  }

  def getBySerial(serial: String): Option[Configuration] = {

    log.debug("Looking up Configuration with serial: {}", serial)

    val query = em.createNamedQuery[ConfigurationEntity]("Configuration.GetBySerial", classOf[ConfigurationEntity])
    query.setParameter("serial", serial)
    query.getResultList.asScala.headOption.map(configurationEntityToConfiguration)
  }

  def getByCustomer(customer: Customer): Seq[Configuration] = {

    log.debug("Getting Configurations for {}", customer)

    val customerEntity = new CustomerEntity
    customerEntity.id = customer.id.get
    customerEntity.name = customer.name
    customerEntity.enabled = customer.enabled

    val query = em.createNamedQuery[ConfigurationEntity]("Configuration.GetByCustomer", classOf[ConfigurationEntity])
    query.setParameter("customer", customerEntity)
    query.getResultList.asScala
  }

  private def getEntity(id: Long): Option[ConfigurationEntity] = {

    log.debug("Getting Configuration with id: {}", id)
    Option(em.find(classOf[ConfigurationEntity], id))
  }
}