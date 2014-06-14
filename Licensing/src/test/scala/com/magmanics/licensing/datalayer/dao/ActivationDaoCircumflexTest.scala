package com.magmanics.licensing.datalayer.dao

import com.magmanics.licensing.datalayer.model.{ActivationCircumflex, ConfigurationCircumflex, CustomerCircumflex, ProductCircumflex}
import java.util.Date
import ru.circumflex.orm._
import com.magmanics.licensing.service.model.ActivationType
import org.testng.annotations._

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27 -Jul-2010
 */
//todo should be actually testing daos? 
class ActivationDaoCircumflexTest {
  
  val ddl = new DDLUnit(CustomerCircumflex, ProductCircumflex, ConfigurationCircumflex, ActivationCircumflex)

  @BeforeClass
  def createDatabase = ddl.CREATE()

  @AfterClass
  def removeDatabase = ddl.DROP()

  @BeforeTest
  def startTransaction = tx

  @AfterTest
  def rollbackTransaction = tx.rollback

  @Test
  def saveActivation {
    val customer = new CustomerCircumflex
    customer.name := "WD40"
    customer.enabled := true
    customer.save

    val product = new ProductCircumflex
    product.name := "PDM"
    product.description := "PDM Archive"
    product.enabled := true
    product.save

    val configuration = new ConfigurationCircumflex
    configuration.enabled := true
    configuration.serial := "12345-12345-12345-12345"
    configuration.maxActivations := 1
    configuration.product := product
    configuration.customer := customer
    configuration.created := new Date
    configuration.user := "jbaxter"
    configuration.save

    val activation = new ActivationCircumflex
    activation.machineIdentifier := "myHardwareId"
    activation.productVersion := "product version"
    activation.configuration := configuration
    activation.activationType := ActivationType.NEW.toString
    activation.save
  }
}