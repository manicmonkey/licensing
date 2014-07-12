package com.magmanics.licensing.datalayer.model

import java.util.Date

import com.magmanics.licensing.service.model.{Activation, ActivationType}
import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 12-Jun-2010
 */

class ActivationTest {

  @Test
  def autoCreatedDate {
    val before = new Date
    val created: Date = Activation(machineIdentifier = "test", productVersion = "blah", activationType = ActivationType.NEW, extraInfo = Map(), configurationId = 1).created
    val after = new Date

    assertNotNull(created)
    assert(before.getTime <= created.getTime)
    assert(after.getTime >= created.getTime)
  }
}