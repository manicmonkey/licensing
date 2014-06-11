package com.magmanics.licensing.datalayer.model

import org.testng.annotations.Test
import com.magmanics.licensing.service.model.{Product, ListOption, TextOption, BoolOption}

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 10-Aug-2010
 */

class ProductTest {

  @Test
  def createWithOptions {
    val boolOption = BoolOption(name = "Boolean option", default = true)
    val textOption = TextOption(name = "Text option", default = "default value")
    val listOption = ListOption(name = "List option", default = "default value", values = List("Option one", "Option two"))

    val product = Product(name = "PDM", description = "Document archive", options = List(boolOption, textOption, listOption))
  }

  @Test
  def enabledByDefault {
    val product = Product(name = "Ubuntu 10.04")
    assert(product.enabled)
  }
}