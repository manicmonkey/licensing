package com.magmanics.licensing.datalayer.dao

import com.magmanics.licensing.service.model.{Configuration => ConfigurationModel, _}
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
      serial = Some("12345-12345-12345-12345"), enabled = true, maxActivations = 1))
  }

  @AfterClass
  def removeTestData {
    configurationDao.delete(configuration.id.get)
    customerDao.delete(customer.id.get)
    productDao.delete(product.id.get)
  }

  @Test
  def saveActivation {
    val activation = new Activation(machineIdentifier = "myHardwareId", productVersion = "product version",
      configurationId = configuration.id.get, activationType = ActivationType.NEW)
    activationDao.create(activation)
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