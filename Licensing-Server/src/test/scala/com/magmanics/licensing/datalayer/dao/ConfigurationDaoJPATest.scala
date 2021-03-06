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

import java.util.UUID

import com.magmanics.licensing.model._
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.testng.Assert._
import org.testng.annotations.{AfterClass, BeforeClass, Test}

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27 -Jul-2010
 */
@ContextConfiguration(Array("classpath:dao.xml", "classpath:data-layer.xml", "classpath:spring/datasource-test.xml"))
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
class ConfigurationDaoJPATest extends AbstractTransactionalTestNGSpringContextTests {

  val log = LoggerFactory.getLogger(classOf[ConfigurationDaoJPATest])

  @Autowired
  var configurationDao: ConfigurationDao = _

  @Autowired
  var customerDao: CustomerDao = _

  @Autowired
  var productDao: ProductDao = _

  var customer: Customer = _
  var product: Product = _

  @BeforeClass
  def setupTestData {
    customer = customerDao.create(Customer(name = "DHL"))
    product = productDao.create(Product(name = "JFinder V3"))
  }

  @AfterClass
  def removeTestData {
    customerDao.delete(customer.id.get)
    productDao.delete(product.id.get)
  }

  @Test
  def newConfigurationsAreGivenAnId {
    val transientConfiguration = new Configuration(user = "jbaxter", serial = Some("fake-serial"), productId = product.id.get, customerId = customer.id.get)
    val persistentConfiguration = configurationDao.create(transientConfiguration)

    assertTrue(persistentConfiguration.id.isDefined)
  }

  @Test
  def createWithOptions {
    val configuration = Configuration(user = "jbaxter", serial = Some(UUID.randomUUID.toString), options = Map(("maximum.users", "10"), ("minimum.software.version", "2.0")), productId = product.id.get, customerId = customer.id.get)
    val sC = configurationDao.create(configuration)
    assertEquals(sC.options.size, 2)
    val c = configurationDao.get(sC.id.get)
    assertEquals(c.options.size, 2)
  }

  @Test
  def addActivations {
    val conf = configurationDao.create(Configuration(None, "jbaxter", product.id.get, customer.id.get, serial = Some(UUID.randomUUID().toString), maxActivations = 2))

    conf.addActivation("prod1", "1.0")
    conf.addActivation("dev1", "1.1")

    configurationDao.update(conf)

    assertEquals(configurationDao.get(conf.id.get).activations.size, 2)
  }

  //
  //  val customer = new CustomerCircumflex
  //  customer.name = "WD40"
  //  customer.enabled = true
  //
  //  val product = new ProductCircumflex
  //  product.name = "PDM"
  //  product.description = "PDM Archive"
  //
  //  @BeforeClass
  //  def setupEntities {
  //    log.debug("Creating context data")
  //    new CustomerDaoCircumflex().create(customer)
  //    ProductDaoCircumflex.create(product)
  //    log.debug("Entites created")
  //  }
  //
  //  @Test
  //  def saveLicenceConfiguration {
  //    using(tx) {
  //      val conf = newConfiguration
  //      new ConfigurationDaoCircumflex().create(conf)
  //    }
  //  }
  //
  //  @Test(expectedExceptions = Array(classOf[ConstraintException]))
  //  def cannotDuplicateSerial {
  //    using(tx) {
  //      val conf1 = newConfiguration
  //      val conf2 = newConfiguration
  //      val licenceConfigurationDao = new ConfigurationDaoCircumflex
  //      licenceConfigurationDao.create(conf1)
  //      licenceConfigurationDao.create(conf2)
  //    }
  //  }
}