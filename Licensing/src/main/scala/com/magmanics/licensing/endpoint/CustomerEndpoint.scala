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

import com.magmanics.licensing.service.CustomerRepository
import com.magmanics.licensing.model.Customer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * @author James Baxter - 16/08/2014.
 */
@RestController
@RequestMapping(Array("/customers"))
class CustomerEndpoint {

  @Autowired
  var customerRepository: CustomerRepository = _

  /**
   * Create a Customer, returning the persistent Customer (id populated)
   */
  @RequestMapping(method = Array(RequestMethod.POST))
  def create(customer: Customer): Customer = customerRepository.create(customer)

  /**
   * Get enabled Customers
   */
  @RequestMapping(method = Array(RequestMethod.GET), params = Array("enabled=true"))
  def getEnabled: Seq[Customer] = customerRepository.getEnabled

  /**
   * Get all Customers within the system
   */
  @RequestMapping(method = Array(RequestMethod.GET))
  def get(): Seq[Customer] = customerRepository.get()

  /**
   * Gets a Customer given the specified id
   */
  @RequestMapping(method = Array(RequestMethod.GET), params = Array("id"))
  def get(@RequestParam id: Long): Option[Customer] = customerRepository.get(id)

  /**
   * Update the given Customer
   */
  @RequestMapping(method = Array(RequestMethod.PUT))
  def update(customer: Customer) = customerRepository.update(customer)
}
