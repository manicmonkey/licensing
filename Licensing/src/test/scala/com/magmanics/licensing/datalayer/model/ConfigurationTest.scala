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

import com.magmanics.licensing.model.exception.NoActivationsLeftException
import com.magmanics.licensing.model.{Activation, ActivationType, Configuration}
import org.testng.Assert._
import org.testng.annotations.Test
/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 12 -Jun-2010
 */

class ConfigurationTest {

  @Test(expectedExceptions = Array(classOf[IllegalStateException]))
  def cannotCreateWithMaxActivationsLessThanOne() {
    Configuration(user = "jbaxter", maxActivations = 0, productId = 1, customerId = 2)
  }

  @Test(expectedExceptions = Array(classOf[IllegalStateException]))
  def cannotCreateWithMoreActivationsThanMaxActivations() {
    val activation1 = Activation(configurationId = 1, machineIdentifier = "dev0", productVersion = "110606", activationType = ActivationType.NEW, extraInfo = Map())
    val activation2 = Activation(configurationId = 1, machineIdentifier = "dev1", productVersion = "110606", activationType = ActivationType.NEW, extraInfo = Map())
    Configuration(user = "jbaxter", maxActivations = 1, activations = List(activation1, activation2), productId = 1, customerId = 2L)
  }

  @Test(expectedExceptions = Array(classOf[IllegalStateException]))
  def cannotReduceMaxActivationsBelowCurrentNumberOfActivations() {
    val conf = Configuration(id = Some(3), user = "jbaxter", maxActivations = 2, productId = 1, customerId = 2L)

    conf.addActivation(machineIdentifier = "dev0", productVersion = "110606")
    conf.addActivation(machineIdentifier = "dev1", productVersion = "110606")

    conf copy (maxActivations = 1)
  }

  //todo new activation unique per configuration / machineIdentifier?

  @Test(expectedExceptions = Array(classOf[NoActivationsLeftException]))
  def cannotAddToManyActivations {
    val conf = Configuration(id = Some(3), user = "jbaxter", maxActivations = 1, productId = 1, customerId = 2L)

    conf.addActivation(machineIdentifier = "dev0", productVersion = "110606")
    conf.addActivation(machineIdentifier = "dev1", productVersion = "110606")
  }

  @Test
  def calculatesSomeActivationsRemainingCorrectly {
    val conf = Configuration(id = Some(3), maxActivations = 2, user = "jbaxter", productId = 1, customerId = 2L)

    conf.addActivation(machineIdentifier = "dev0", productVersion = "110606")
    conf.addActivation(machineIdentifier = "dev0", productVersion = "110607") //upgrade

    assertTrue(conf.activationsAvailable)
    assertEquals(conf.totalActivations, 1)
    assertEquals(conf.activations.head.productVersion, "110607")
  }

  @Test
  def calculatesNoActivationsRemainingCorrectly {
    val conf = Configuration(id = Some(3), maxActivations = 2, user = "jbaxter", productId = 1, customerId = 2L)

    conf.addActivation(machineIdentifier = "dev0", productVersion = "110606")
    conf.addActivation(machineIdentifier = "dev1", productVersion = "110606")

    assertEquals(conf.totalActivations, 2)
    assertFalse(conf.activationsAvailable)
  }

  @Test(expectedExceptions = Array(classOf[IllegalStateException]))
  def cannotAddActivationsWhenDisabled {
    val conf = Configuration(id = Some(3), enabled = false, user = "jbaxter", productId = 1, customerId = 2L)
    conf.addActivation(machineIdentifier = "dev0", productVersion = "110606")
  }

  @Test
  def createdWithDefaultDate {
    val before = System.currentTimeMillis
    val conf = Configuration(user = "jbaxter", productId = 1, customerId = 2)
    val after = System.currentTimeMillis

    assertNotNull(conf.created)
    val created: Long = conf.created.getTime
    assertTrue(before <= created)
    assertTrue(after >= created)
  }
}