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
package com.magmanics.licensing.endpoint

import com.magmanics.licensing.service.model.Configuration
import com.magmanics.licensing.service.{ConfigurationRepository, CustomerRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RestController}

/**
 * @author James Baxter - 16/08/2014.
 */
@RestController
@RequestMapping(Array("/configurations"))
class ConfigurationEndpoint {

  @Autowired
  var configurationRepository: ConfigurationRepository = _

  @Autowired
  var customerRepository: CustomerRepository = _

  /**
   * Create a configuration
   * @return the persistent configuration (ie includes an id).
   */
  @RequestMapping(method = Array(RequestMethod.POST))
  def create(configuration: Configuration): Configuration = configurationRepository.create(configuration)

  /**
   * Lookup a Configuration by its id
   * @throws NoSuchEntityException If a configuration with the given id cannot be found
   */
  @RequestMapping(value = Array("/id/{id}"), method = Array(RequestMethod.GET))
  def get(@PathVariable id: Long): Configuration = configurationRepository.get(id)

  /**
   * Get configurations for a particular customer. Returns an empty list if none are found.
   */
  @RequestMapping(value = Array("/customer/{customer}"), method = Array(RequestMethod.GET))
  def getByCustomer(@PathVariable customer: String): Seq[Configuration] = configurationRepository.getByCustomer(customer)

  /**
   * Try to get a configuration given a serial.
   */
  @RequestMapping(value = Array("/serial/{serial}"), method = Array(RequestMethod.GET))
  def getBySerial(@PathVariable serial: String): Option[Configuration] = configurationRepository.getBySerial(serial)

  /**
   * Updates a configuration
   */
  @RequestMapping(method = Array(RequestMethod.PUT))
  def update(configuration: Configuration) = configurationRepository.update(configuration)
}
