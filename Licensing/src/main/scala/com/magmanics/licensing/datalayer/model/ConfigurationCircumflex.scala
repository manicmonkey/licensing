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
import java.util.Date
/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 28-May-2010
 * Time: 20:13:57
 * To change this template use File | Settings | File Templates.
 */
class ConfigurationCircumflex extends Record[Long, ConfigurationCircumflex] with IdentityGenerator[Long, ConfigurationCircumflex] {
  val id = "id".BIGINT.AUTO_INCREMENT.NOT_NULL
  val created = "created".TIMESTAMP
  val user = "user".VARCHAR(100)
  val enabled = "enabled".BOOLEAN
  val serial = "serial".VARCHAR(50).UNIQUE
  val maxActivations = "max_activations".INTEGER

  val product = "product_id".BIGINT.REFERENCES(ProductCircumflex).ON_DELETE(CASCADE)
  val customer = "customer_id".BIGINT.REFERENCES(CustomerCircumflex).ON_DELETE(CASCADE)

  def PRIMARY_KEY = id
  def relation = ConfigurationCircumflex

  def activations = inverseMany(ActivationCircumflex.configuration) //todo insert only - no delete
  def options = inverseMany(ConfigurationOptionCircumflex.configuration)

  override def INSERT_!(fields: Field[_, ConfigurationCircumflex]*): Int = {
    if (created.isEmpty) created := new Date
    super.INSERT_!(fields: _*)
  }
}

object ConfigurationCircumflex extends ConfigurationCircumflex with Table[Long, ConfigurationCircumflex]

class ConfigurationOptionCircumflex extends Record[Long, ConfigurationOptionCircumflex] with IdentityGenerator[Long, ConfigurationOptionCircumflex] {
  val id = "id".BIGINT.AUTO_INCREMENT.NOT_NULL
  val configuration = "configuration_id".BIGINT.REFERENCES(ConfigurationCircumflex)
  val key = "key".VARCHAR(200).NOT_NULL
  val value = "value".VARCHAR(200)

  def PRIMARY_KEY = id
  def relation = ConfigurationOptionCircumflex
}

object ConfigurationOptionCircumflex extends ConfigurationOptionCircumflex with Table[Long, ConfigurationOptionCircumflex] {
  UNIQUE(this.PRIMARY_KEY, this.key)
}