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

import com.magmanics.licensing.service.model.{Customer, Configuration}
import com.magmanics.licensing.datalayer.model.{CustomerCircumflex, ConfigurationOptionCircumflex, ConfigurationCircumflex}
import exception.{NoSuchEntityException, DataLayerException, ConstraintException}
import org.slf4j.LoggerFactory

/**
 * DAO for {@link com.magmanics.licensing.service.model.Configuration Configuration}s.
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27 -Jul-2010
 */
abstract class ConfigurationDao {
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
class ConfigurationDaoCircumflex(activationDao: ActivationDao) extends ConfigurationDao {

  val log = LoggerFactory.getLogger(classOf[ConfigurationDaoCircumflex])

  import ru.circumflex.orm._
  import com.magmanics.licensing.datalayer.dao.ImplicitCircumflexModelConversion._

  def create(configuration: Configuration): Configuration = {

    log.debug("Creating {}", configuration)

    if (configuration.serial.isEmpty)
      throw new IllegalArgumentException("Serial number must be specified in configuration: " + configuration)

    try {
      val c = new ConfigurationCircumflex
      c.created := configuration.created
      c.user := configuration.user
      c.enabled := configuration.enabled
      c.serial := configuration.serial.get
      c.maxActivations := configuration.maxActivations
      c.product := configuration.productId
      c.customer := configuration.customerId
      c.save

      configuration.options.foreach(option => {
        val o = new ConfigurationOptionCircumflex
        o.configuration := c
        o.key := option._1
        o.value := option._2
        o.save
      })

      c
    } catch {
      case ve: ValidationException => throw new ConstraintException(ve)
      case e: Exception => throw new DataLayerException(e)
    }
  }

  def update(c: Configuration) {

    log.debug("Updating {}", c)

    val id = c.id.getOrElse(
      throw new IllegalStateException("Cannot update configuration as no id: " + c))

    val configuration = getCircumflex(id).getOrElse(
      throw new IllegalStateException("Cannot update configuration as could not find id: " + c))

    configuration.maxActivations := c.maxActivations
    configuration.enabled := c.enabled
    configuration.save

    c.activations.filter(_.id.isEmpty).foreach(activationDao.create(_, id))
  }

  def get(id: Long): Configuration = {
    getCircumflex(id).getOrElse({throw new NoSuchEntityException})
  }

  def getBySerial(serial: String): Option[Configuration] = {

    log.debug("Looking up Configuration with serial: {}", serial)

    val l = ConfigurationCircumflex AS "l"
    SELECT(l.*) FROM(l) WHERE(l.serial EQ serial) unique match {
      case Some(c) => Some(c) //bit of a hack unpacking and repacking Option[ConfigurationCircumflex] so it can be implicitly converted
      case _ => None
    }
  }

  def getByCustomer(customer: Customer): Seq[Configuration] = {

    log.debug("Getting Configurations for {}", customer)

    val con = ConfigurationCircumflex AS "con"
    val cus = CustomerCircumflex AS "cus"
    SELECT(con.*) FROM (con JOIN cus) WHERE (cus.id EQ customer.id.get) list
  }

  private def getCircumflex(id: Long): Option[ConfigurationCircumflex] = {

    log.debug("Getting Configuration with id: {}", id)

    val c = ConfigurationCircumflex AS "c"
    (SELECT(c.*) FROM (c) WHERE (c.id EQ id)).unique
  }
}