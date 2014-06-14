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

package com.magmanics.licensing.datalayer.model

import ru.circumflex.orm._
/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 28-May-2010
 * Time: 20:28:12
 * To change this template use File | Settings | File Templates.
 */

class CustomerCircumflex extends Record[Long, CustomerCircumflex] with IdentityGenerator[Long, CustomerCircumflex] {

  val id = "id".BIGINT.AUTO_INCREMENT.NOT_NULL
  val name = "name".VARCHAR(200).NOT_NULL.UNIQUE
  val enabled = "enabled".BOOLEAN

  def PRIMARY_KEY = id
  def relation = CustomerCircumflex

  def configurations = inverseMany(ConfigurationCircumflex.customer)

//  override def equals(that: Any) = {
//    that match {
//      case other: CustomerCircumflex => !name.empty_? && name.get.equals(other.name.get)
//      case _ => false
//    }
//  }

//  override def toString = {
//    "CustomerCircumflex{name=" + (if (name.empty_?) "null" else name.get) + "}"
//  }
}

object CustomerCircumflex extends CustomerCircumflex with Table[Long, CustomerCircumflex] { //todo remove not required
  validation.notEmpty((c: CustomerCircumflex) => c.name)
  validation.notNull((c: CustomerCircumflex) => c.enabled)

//  def getByName(name: String): Option[CustomerCircumflex] = {
//    CustomerCircumflex.criteria.add(CustomerCircumflex.name EQ name).unique
//  }
}