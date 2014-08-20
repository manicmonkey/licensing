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

import com.magmanics.licensing.service.exception.DuplicateNameException
import com.magmanics.licensing.model.Customer
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