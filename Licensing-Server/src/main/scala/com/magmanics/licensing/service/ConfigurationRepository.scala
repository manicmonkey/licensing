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

import java.util.Date

import com.magmanics.auditing.Auditable
import com.magmanics.licensing.datalayer.SerialGenerator
import com.magmanics.licensing.datalayer.dao.ConfigurationDao
import com.magmanics.licensing.model.Configuration
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Repository for [[com.magmanics.licensing.model.Configuration Configurations]]. In addition to basic data
 * access, implementations will add method level authentication and auditing.
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 15-Aug-2010
 */
trait ConfigurationRepository {
  /**
   * Create a configuration
   * @return the persistent configuration (ie includes an id).
   */
  def create(configuration: Configuration): Configuration

  /**
   * Lookup a Configuration by its id
   * todo catch NoSuchEntityException and return 404
   */
  def get(id: Long): Configuration

  /**
   * Get configurations for a particular customer. Returns an empty list if none are found.
   */
  def getByCustomer(customer: String): Set[Configuration]

  /**
   * Try to get a configuration given a serial.
   */
  def getBySerial(serial: String): Option[Configuration]

  /**
   * Updates a configuration
   */
  def update(configuration: Configuration)
}

@PreAuthorize("isAuthenticated()")
class ConfigurationRepositoryImpl(configurationDao: ConfigurationDao, serialGenerator: SerialGenerator, authenticationService: AuthenticationService) extends ConfigurationRepository {

  val log = LoggerFactory.getLogger(classOf[ConfigurationRepositoryImpl])

  @PreAuthorize("hasRole('CREATE_CONFIGURATION')")
  @Auditable("audit.configuration.create")
  def create(configuration: Configuration): Configuration = {
    val generatedSerial = serialGenerator.generateSerial()
    val currentUser = authenticationService.currentUser().getOrElse(throw new IllegalStateException("Could not get current user"))
    val newConfiguration = configuration copy (serial = Some(generatedSerial), user = currentUser, created = new Date)
    log.debug("Creating new configuration: {}", newConfiguration)
    configurationDao.create(newConfiguration)
  }

  @Auditable("audit.configuration.get")
  def get(id: Long): Configuration = {
    log.debug("Getting Configuration#{}", id)
    val configuration = configurationDao.get(id)
    log.debug("Got configuration: {}", configuration)
    configuration
  }

  @Auditable("audit.configurations.getByCustomer")
  def getByCustomer(customer: String): Set[Configuration] = {
    log.debug("Getting Configurations for {}", customer)
    val configurations = configurationDao.getByCustomer(customer)
    log.debug("Got configurations for customer({}): {}", customer, configurations)
    configurations
  }

  @Auditable("audit.configuration.getBySerial")
  def getBySerial(serial: String): Option[Configuration] = {
    log.debug("Getting Configuration with serial: {}", serial)
    configurationDao.getBySerial(serial)
  }

  @PreAuthorize("hasRole('UPDATE_CONFIGURATION')")
  @Auditable("audit.configuration.update")
  def update(configuration: Configuration) {
    log.debug("Updating {}", configuration)
    configurationDao.update(configuration)
  }
}