package com.magmanics.licensing.datalayer.model

import org.testng.annotations.Test
import java.util.Date
import org.testng.Assert._
import com.magmanics.licensing.service.model.{ActivationType, Activation}

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 12-Jun-2010
 */

class ActivationTest {

  @Test
  def autoCreatedDate {
    val before = new Date
    val created: Date = Activation(machineIdentifier = "test", productVersion = "blah", activationType = ActivationType.NEW, extraInfo = Map()).created
    val after = new Date

    assertNotNull(created)
    assert(before.getTime <= created.getTime)
    assert(after.getTime >= created.getTime)
  }
}