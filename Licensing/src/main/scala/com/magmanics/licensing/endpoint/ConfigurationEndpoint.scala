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

import com.magmanics.licensing.model.Configuration
import com.magmanics.licensing.service.{ConfigurationRepository, CustomerRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

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
  def create(@RequestBody configuration: Configuration): Configuration = configurationRepository.create(configuration)

  /**
   * Lookup a Configuration by its id
   * todo catch NoSuchEntityException and return 404
   */
  @RequestMapping(method = Array(RequestMethod.GET), params = Array("id"))
  def get(@RequestParam id: Long): Configuration = configurationRepository.get(id)

  /**
   * Get configurations for a particular customer. Returns an empty list if none are found.
   */
  @RequestMapping(method = Array(RequestMethod.GET), params = Array("customer"))
  def getByCustomer(@RequestParam customer: String): Seq[Configuration] = configurationRepository.getByCustomer(customer)

  /**
   * Try to get a configuration given a serial.
   */
  @RequestMapping(method = Array(RequestMethod.GET), params = Array("serial"))
  def getBySerial(@RequestParam serial: String): Option[Configuration] = configurationRepository.getBySerial(serial)

  /**
   * Updates a configuration
   */
  @RequestMapping(method = Array(RequestMethod.PUT))
  def update(@RequestBody configuration: Configuration) = configurationRepository.update(configuration)
}
