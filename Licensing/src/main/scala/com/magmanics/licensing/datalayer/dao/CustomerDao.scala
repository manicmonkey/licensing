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

import com.magmanics.licensing.datalayer.model.CustomerCircumflex
import com.magmanics.licensing.service.model.Customer
import java.sql.SQLException
import com.magmanics.licensing.service.exception.DuplicateNameException
import org.slf4j.LoggerFactory

/**
 * Dao for {@link com.magmanics.licensing.service.model.Customer Customer}s
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27-Jul-2010
 */
abstract class CustomerDao {
  /**
   * Persists a Customer
   * @return The newly created Customer with its id populated to facilitate further operations
   * @throws DuplicateNameException If a Customer with the same name already exists
   */
  def create(customer: Customer): Customer

  /**
   * Gets all Customers within the system
   */
  def get(): Seq[Customer]

  /**
   * Gets a Customer by the given id
   */
  def get(id: Long): Option[Customer]

  /**
   *  Gets all enabled Customers within the system
   */
  def getEnabled(): Seq[Customer]

  /**
   * Update the given Customer
   * @throws DuplicateNameException If a Customer with the same name already exists
   */
  def update(customer: Customer)
}

/**
 * Circumflex implementation of  {@link com.magmanics.licensing.datalayer.dao.CustomerDao CustomerDao}
 */
class CustomerDaoCircumflex extends CustomerDao {

  val log = LoggerFactory.getLogger(classOf[CustomerDaoCircumflex])

  import ru.circumflex.orm._
  import com.magmanics.licensing.datalayer.dao.ImplicitCircumflexModelConversion._

  def create(c: Customer): Customer = {

    log.debug("Creating {}", c)

    val customerCircumflex = new CustomerCircumflex
    customerCircumflex.name := c.name
    customerCircumflex.enabled := c.enabled

    try {
      customerCircumflex.save
    } catch {
      case e: SQLException => {
        val message = e.getMessage //todo replace with regex
        if (message != null && message.contains("duplicate value(s) for column(s) NAME in statement")) {
          throw new DuplicateNameException("Cannot create Customer as name is already is use: " + c)
        } else {
          throw e
        }
      }
    }

    customerCircumflex
  }//throws ValidationException

  def get(): Seq[Customer] = {
    log.debug("Getting all Customers")
    val c = CustomerCircumflex AS "c"
    SELECT (c.*) FROM (c) ORDER_BY (c.name) list
  }

  def get(id: Long): Option[Customer] = {
    getCircumflex(id)
  }

  def getCircumflex(id: Long): Option[CustomerCircumflex] = {
    log.debug("Getting Customer with id: {}", id)
    val c = CustomerCircumflex AS "c"
    SELECT (c.*) FROM (c) WHERE (c.PRIMARY_KEY EQ id) unique
  }

  def getEnabled(): Seq[Customer] = {
    log.debug("Getting all enabled Customers")
    val c = CustomerCircumflex AS "c"
    SELECT (c.*) FROM (c) WHERE (c.enabled EQ true) ORDER_BY (c.name) list
  }

  def update(c: Customer) {

    log.debug("Updating {}", c)

    val id = c.id.getOrElse(
      throw new IllegalStateException("Cannot update a Customer which has no id: " + c))

    val customer = getCircumflex(id).getOrElse(
      throw new IllegalStateException("Cannot update Customer as id is unknown: " + c))

    customer.name := c.name
    customer.enabled := c.enabled
    customer.save
  }
}