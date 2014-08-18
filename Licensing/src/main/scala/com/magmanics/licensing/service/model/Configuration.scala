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

package com.magmanics.licensing.service.model

import java.util.Date

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.magmanics.licensing.service.exception.NoActivationsLeftException

import scala.beans.BeanInfo

/**
 * Represents a (mostly) immutable licence configuration. Although clearly related to a Customer and Product, it is not
 * directly linked at this level of the architecture. Is an aggregate root and provides the sole point of access for
 * licence activations.
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 02-Aug-2010
 */
//todo investigate versioning / optimistic locking
//todo add logging/auditing of events
//todo getting lots of Options here - consider NewConfiguration class (altho must bear in mind client/ui)
//todo proper encapsulation of activations possible?
@BeanInfo
case class Configuration(@JsonDeserialize(contentAs=classOf[java.lang.Long]) id: Option[Long] = None, //annotation communicates generic type to jackson
                         user: String,
                         productId: Long,
                         customerId: Long,
                         created: Date = new Date,
                         serial: Option[String] = None,
                         options: Map[String, String] = Map(),
                         var enabled: Boolean = true,
                         var maxActivations: Int = 1,
                         var activations: Seq[Activation] = List()) {

  if(maxActivations < 1) {
    throw new IllegalStateException("maxActivations cannot be less than 1 (recieved " + maxActivations + ")")
  }

  if (activations.count(_.activationType == ActivationType.NEW) > maxActivations) {
    throw new IllegalStateException("maxActivations cannot be less than the number of existing 'NEW' activations")
  }

  /**
   * Adds an [[com.magmanics.licensing.service.model.Activation Activation]] to this Configuration
   */
  @throws[NoActivationsLeftException]("If the activation limit has been reached")
  def addActivation(machineIdentifier: String, productVersion: String, extraInfo: Map[String, String] = Map()) {

    if (!enabled)
      throw new IllegalStateException("Cannot add an activation to a disabled configuration(" + this + "): machineIdentifier(" + machineIdentifier + "), extraInfo(" + extraInfo + ")")

    val activationType = if(activations.exists(_.machineIdentifier equalsIgnoreCase machineIdentifier)) ActivationType.UPGRADE else ActivationType.NEW

    if (activationType == ActivationType.NEW && !activationsAvailable)
      throw new NoActivationsLeftException("No activations available. Used maximum of: " + maxActivations)

    activations = Activation(machineIdentifier = machineIdentifier, productVersion = productVersion, activationType = activationType, extraInfo = extraInfo, configurationId = id.get) +: activations
  }

  /**
   * @return The number of [[com.magmanics.licensing.service.model.ActivationType new]] Activations against this Configuration
   */
  def totalActivations: Int = activations.filter(_.activationType == ActivationType.NEW).size

  /**
   * @return Whether there are activations remaining
   */
  def activationsAvailable: Boolean = totalActivations < maxActivations
}

object Configuration {
//  def apply(id: Option[Long] = None,
//            user: String,
//            productId: Long,
//            customerId: Long,
//            created: Date = new Date,
//            serial: Option[String] = None,
//            options: Map[String, String] = Map(),
//            enabled: Boolean = true,
//            maxActivations: Int = 1,
//            activations: Seq[Activation] = List()) = new Configuration(id, user, productId, customerId, created, serial, options, enabled, maxActivations, activations)
  def apply(user: String, product: Product, customer: Customer) = new Configuration(user = user, productId = product.id.get, customerId = customer.id.get)
}