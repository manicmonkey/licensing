package com.magmanics.licensing.datalayer.dao

import com.magmanics.circumflex.orm.AutoRollbackTransaction
/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 24-Jul-2010
 */

class CustomerDaoCircumflexTest extends AutoRollbackTransaction { //focus on crud / mapping

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
//
//  @Test(expectedExceptions = Array(classOf[DataLayerException]))
//  def cannotSaveCustomersWithTheSameName {
//    using(tx) {
//      new CustomerDaoCircumflex().create(customer)
//      new CustomerDaoCircumflex().create(customer)
//    }
//  }
//
//  def customer: CustomerCircumflex = {
//    val newCustomer = new CustomerCircumflex
//    newCustomer.name := "Edwards"
//    newCustomer.enabled := true
//    newCustomer
//  }
}