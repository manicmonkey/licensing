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
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 28-May-2010
 */
class ActivationCircumflex extends Record[ActivationCircumflex] {

  def this(machineIdentifier: String, productVersion: String, activationType: String) {
    this()
    this.machineIdentifier := machineIdentifier
    this.productVersion := productVersion
    this.activationType := activationType
  }

  val created = "created" TIMESTAMP
  val machineIdentifier = "machine_identifier" VARCHAR(200)
  val productVersion = "product_version" VARCHAR(200)
  val configuration = "configuration_id" REFERENCES(ConfigurationCircumflex)
  val activationType = "activation_type" VARCHAR(10)

  def extraInfo = inverse(ActivationInfoCircumflex.activation)

  override def insert_!(fields: Field[_]*): Int = {
    if (created.empty_?) created := new Date
    super.insert_!(fields: _*)
  }

//  validation.add(a => { todo ensure can't go over limit
//    if (configuration.relation.)
//  })

  override def toString = {
    "ActivationCircumflex{id=" + id.get + ", machineIdentifier=" + machineIdentifier.get + ", activationType=" + activationType.get + "}"
  }
}

object ActivationCircumflex extends Table[ActivationCircumflex] {
  CONSTRAINT("type_chk").CHECK("activation_type IN ('NEW', 'UPGRADE')") //todo enumeration ActivationType enums
  
//  def apply(id: Option[Long], created: Date, machineIdentifier: String, activationType: ActivationType.Value): ActivationCircumflex = {
//    val a = new ActivationCircumflex
//    id match {
//      case Some(i) => a.id := i
//    }
//    a.created := created
//    a.machineIdentifier := machineIdentifier
//    a.activationType := activationType.toString
//    a
//  }
}

class ActivationInfoCircumflex extends Record[ActivationInfoCircumflex] {
  val activation = "activation_id" REFERENCES(ActivationCircumflex)
  val key = "key" VARCHAR(200)
  val value = "value" VARCHAR(200)
}

object ActivationInfoCircumflex extends Table[ActivationInfoCircumflex] {
  UNIQUE(this.id, this.key)
}