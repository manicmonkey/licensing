package com.magmanics.licensing.datalayer.dao

import com.magmanics.circumflex.orm.AutoRollbackTransaction
import com.magmanics.licensing.datalayer.model.{ConfigurationCircumflex, CustomerCircumflex, ActivationCircumflex, ProductCircumflex}
import org.testng.Assert._
import com.magmanics.licensing.service.model.Configuration
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.{LoggerFactory}
import ch.qos.logback.classic.LoggerContext

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27 -Jul-2010
 */

class ConfigurationDaoCircumflexTest extends AutoRollbackTransaction {
  val log = LoggerFactory.getLogger(classOf[ConfigurationDaoCircumflexTest])

//  @Test
  def newConfigurationsAreGivenAnId {
    val customer = new CustomerCircumflex
    customer.name := "WD40"
    customer.enabled := true
    customer.save

    val product = new ProductCircumflex
    product.name := "PDM"
    product.description := "PDM Archive"
    product.save

    val transientConfiguration = new Configuration(user = "jbaxter", serial = Some("fake-serial"), productId = product.id(), customerId = customer.id())
    val persistentConfiguration = new ConfigurationDaoCircumflex(null).create(transientConfiguration)

    assertTrue(persistentConfiguration.id.isDefined)
  }
  //
  //  val customer = new CustomerCircumflex
  //  customer.name := "WD40"
  //  customer.enabled := true
  //
  //  val product = new ProductCircumflex
  //  product.name := "PDM"
  //  product.description := "PDM Archive"
  //
  //  @BeforeClass
  //  def setupEntities {
  //    log.debug("Creating context data")
  //    new CustomerDaoCircumflex().create(customer)
  //    ProductDaoCircumflex.create(product)
  //    log.debug("Entites created")
  //  }
  //
  //  @AfterClass
  //  def removeEntities {
  //    log.debug("Removing entities")
  //    new CustomerDaoCircumflex().delete(customer)
  //    ProductDaoCircumflex.delete(product)
  //  }
  //
  //  def newActivation: ActivationCircumflex = {
  //    val activation = new ActivationCircumflex
  //    activation.machineIdentifier := "dev0"
  //    activation.activationType := ActivationType.NEW.toString
  //    activation
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
  //
  //  @Test
  //  def addActivations {
  //    using(tx) {
  //      val conf = newConfiguration
  //      conf.setMaxActivations(2)
  //      val licenceConfigurationDao = new ConfigurationDaoCircumflex
  //      licenceConfigurationDao.create(conf)
  //
  //      val act1 = newActivation
  //      val act2 = newActivation
  //
  //      conf.addActivation(act1)
  //      conf.addActivation(act2)
  ////      val licenceActivationDao = new ActivationDaoCircumflex
  ////      licenceActivationDao.create(act1)
  ////      licenceActivationDao.create(act2)
  //
  //      assertEquals(conf.activations.apply.size, 2)
  //    }
  //  }
  //
  //  def newConfiguration: ConfigurationCircumflex = {
  //
  //    val configuration = new ConfigurationCircumflex
  //    configuration.enabled := true
  //    configuration.serial := "12345-12345-12345-12345"
  //    configuration.maxActivations := 1
  //    configuration.product := product
  //    configuration.customer := customer
  //    configuration.created := new Date
  //    configuration.user := "jbaxter"
  //    configuration
  //  }
}