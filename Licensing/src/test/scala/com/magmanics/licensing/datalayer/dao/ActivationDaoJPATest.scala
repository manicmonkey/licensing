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

import com.magmanics.licensing.model.{Configuration => ConfigurationModel, _}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.testng.Assert.assertEquals
import org.testng.annotations._

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27 -Jul-2010
 */
@ContextConfiguration(Array("classpath:dao.xml", "classpath:data-layer.xml", "classpath:spring/datasource-test.xml"))
@Transactional
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
class ActivationDaoJPATest extends AbstractTransactionalTestNGSpringContextTests {

  @Autowired
  var customerDao: CustomerDao = _

  @Autowired
  var productDao: ProductDao = _

  @Autowired
  var configurationDao: ConfigurationDao = _

  @Autowired
  var activationDao: ActivationDao = _

  var customer: Customer = _
  var product: Product = _
  var configuration: ConfigurationModel = _

  @BeforeClass
  def setupTestData {
    customer = customerDao.create(Customer(name = "WD40", enabled = true))
    product = productDao.create(Product(name = "PDM", description = "PDM Archive", enabled = true))
    configuration = configurationDao.create(ConfigurationModel(user = "jbaxter",
      productId = product.id.get, customerId = customer.id.get,
      serial = Some("12345-12345-12345-12345"), enabled = true, maxActivations = 2))
  }

  @AfterClass
  def removeTestData {
    configurationDao.delete(configuration.id.get)
    customerDao.delete(customer.id.get)
    productDao.delete(product.id.get)
  }

  @Test
  def saveActivation {
    assert(configurationDao.get(configuration.id.get).activations.isEmpty)
    activationDao.create(new Activation(machineIdentifier = "myHardwareId", productVersion = "product version", configurationId = configuration.id.get, activationType = ActivationType.NEW))
    activationDao.create(new Activation(machineIdentifier = "myHardwareId", productVersion = "product version", configurationId = configuration.id.get, activationType = ActivationType.NEW))
    val configuration1 = configurationDao.get(configuration.id.get)
    assertEquals(configuration1.activations.size, 2)
  }

  @Test
  def saveActivationWithInfo {
    val activation = new Activation(machineIdentifier = "myHardwareId", productVersion = "product version",
      configurationId = configuration.id.get, activationType = ActivationType.NEW,
      extraInfo = Map("weather" -> "sunny"))

    activationDao.create(activation)

    val configurationWithActivation = configurationDao.get(configuration.id.get)
    assertEquals(configurationWithActivation.activations.size, 1)
    val savedActivation = configurationWithActivation.activations.head
    assertEquals(savedActivation.extraInfo.size, 1)
    assertEquals(savedActivation.extraInfo.head._1, "weather")
    assertEquals(savedActivation.extraInfo.head._2, "sunny")
  }
}