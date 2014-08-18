package com.magmanics.licensing.datalayer.model

import com.magmanics.licensing.model.Customer
import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 12-Jun-2010
 */
class CustomerTest { //focus on extra methods

  @Test
  def enabledByDefault {
    assertTrue(new Customer(name = "Edwards").enabled)
  }

  @Test
  def customerNameRetrievable {
    assertEquals(new Customer(name = "Edwards").name, "Edwards")
  }

  @Test
  def customerNameCanBeChanged {
    val customer = new Customer(name = "Customer one")
    customer.name = "XXX"
    assertEquals(customer.name, "XXX")
  }
}