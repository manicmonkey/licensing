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
import com.magmanics.licensing.datalayer.dao.CustomerDao
import com.magmanics.licensing.model.Customer
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Repository for [[com.magmanics.licensing.model.Customer Customers]]. In addition to basic data
 * access, implementations will add method level authentication and auditing.
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 30-May-2010
 */
trait CustomerRepository {
  /**
   * Create a Customer, returning the persistent Customer (id populated)
   */
  def create(customer: Customer): Customer

  /**
   * Get all Customers within the system
   */
  def get(): Seq[Customer]

  /**
   * Get enabled Customers
   */
  def getEnabled: Seq[Customer]

  /**
   * Gets a Customer given the specified id
   */
  def get(id: Long): Option[Customer]

  /**
   * Update the given Customer
   */
  def update(customer: Customer)
}

@PreAuthorize("isAuthenticated()")
class CustomerRepositoryImpl(customerDao: CustomerDao) extends CustomerRepository {

  val log = LoggerFactory.getLogger(classOf[CustomerRepositoryImpl])

  @PreAuthorize("hasRole('CREATE_CUSTOMER')")
  @Auditable("audit.customer.create")
  def create(customer: Customer): Customer = {
    if (customer.id.isDefined)
      throw new IllegalArgumentException("Should not be trying to create a customer with an existing id: " + customer)

    log.debug("Creating {}", customer)
    customerDao.create(customer)
  }

  @Auditable("audit.customers.get")
  def get(): Seq[Customer] = {
    customerDao.get()
  }

  @Auditable("audit.customers.getEnabled")
  def getEnabled: Seq[Customer] = {
    customerDao.getEnabled
  }

  @Auditable("audit.customer.getById")
  def get(id: Long): Option[Customer] = {
    customerDao.get(id)
  }

  @PreAuthorize("hasRole('UPDATE_CUSTOMER')")
  @Auditable("audit.customer.update")
  def update(customer: Customer) {
    log.debug("Updating {}", customer)
    customerDao.update(customer)
  }
}