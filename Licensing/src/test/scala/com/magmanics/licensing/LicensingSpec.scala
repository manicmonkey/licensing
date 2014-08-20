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
package com.magmanics.licensing

import org.scalatest.{BeforeAndAfterEach, FeatureSpec, GivenWhenThen}

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 11-Jul-2010
 */
class LicensingSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfterEach {

  override protected def beforeEach() {
    //setup blank db
  }

  override protected def afterEach() {
    //need to empty db?
  }

  feature("Products can be managed by a user") {
    scenario("a product is created with a textfield") (pending)
    scenario("a product is created with a dropdown") (pending)
    scenario("a product is created with a checkbox") (pending)
    scenario("a product is disabled") (pending)
    scenario("a product is enabled") (pending)
  }

  feature("Licence configurations can be managed by a user") {
    scenario("a configuration is created") (pending)
    scenario("a configuration is disabled") (pending)
    scenario("a configuration is enabled") (pending)
  }

  feature("Activations fail if a parent is disabled") {
    scenario("an activation request for a disabled configuration") (pending)
    scenario("an activation request for a disabled product") (pending)
    scenario("an activation request for a disabled customer") (pending)
  }

  feature("An activation can contain additional information") {
    scenario("the activation contains extra info") (pending)
    scenario("the activation contains no extra info") (pending)
  }

  feature("An existing activation can be reactivated without cost") {
    scenario("there are available activations") (pending)
    scenario("there are no available activations") (pending)
  }

  feature("Activations are transported as xml") {
    scenario("an activation request is deserialised from xml") (pending)
    scenario("an activation response is serialised to xml") (pending)
  }
}