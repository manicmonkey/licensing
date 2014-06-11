package com.magmanics.licensing.datalayer.model

import com.magmanics.circumflex.orm.AutoRollbackTransaction
import com.magmanics.licensing.service.model.Customer
import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 12-Jun-2010
 */
class CustomerTest extends AutoRollbackTransaction { //focus on extra methods

  @Test
  def enabledByDefault {
    val customer = new Customer(name = "Edwards")
    assertTrue(customer.enabled)
  }

  @Test
  def customerNameRetrievable {
    val customer = new Customer(name = "Edwards")
    assertEquals(customer.name, "Edwards")
  }

  @Test
  def customerNameCanBeChanged {
    val customer = new Customer(name = "Customer one")
    customer.name = "XXX"
    assertEquals(customer.name, "XXX")
  }
}