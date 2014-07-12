package com.magmanics.licensing.datalayer.dao

import com.magmanics.licensing.service.exception.DuplicateNameException
import com.magmanics.licensing.service.model.Customer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.testng.annotations.Test

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 24-Jul-2010
 */
@ContextConfiguration(Array("classpath:dao.xml", "classpath:data-layer.xml", "classpath:spring/datasource-test.xml"))
@Transactional
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
class CustomerDaoEntityTest extends AbstractTransactionalTestNGSpringContextTests { //focus on crud / mapping

//  @Test
//  def saveCustomer {
//    using(tx) {
//      new CustomerDaoCircumflex().create(customer)
//    }
//  }
//
//  @Test
//  def saveDisabledCustomer {
//    using(tx) {
//      val customer = new CustomerCircumflex
//      customer.name := "Edwards"
//      customer.enabled := false
//      new CustomerDaoCircumflex().create(customer)
//    }
//  }
//
//  @Test(expectedExceptions = Array(classOf[ConstraintException]))
//  def cannotSaveCustomerWithoutName {
//    using(tx) {
//      val customer = new CustomerCircumflex
//      customer.enabled := true
//      new CustomerDaoCircumflex().create(customer)
//    }
//  }
//
//  @Test(expectedExceptions = Array(classOf[ConstraintException]))
//  def cannotSaveCustomerWithoutEnabled {
//    using(tx) {
//      val customer = new CustomerCircumflex
//      customer.name := "Edwards"
//      new CustomerDaoCircumflex().create(customer)
//    }
//  }

  @Autowired
  var customerDao: CustomerDao = _

  @Test(expectedExceptions = Array(classOf[DuplicateNameException]))
  def cannotSaveCustomersWithTheSameName {
      customerDao.create(customer)
      customerDao.create(customer)
  }

  def customer = new Customer(name = "Edwards", enabled = true)
}