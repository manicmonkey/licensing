package com.magmanics.licensing

import org.scalatest.GivenWhenThen

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 27-Jul-2010
 */
class SeleniumEndToEndTest extends TransactionalSpringBasedSuite with GivenWhenThen {
  //test db data against real world data
  def contextLocation = "config.xml"
}