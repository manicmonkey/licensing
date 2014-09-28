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
package com.magmanics.licensing.datalayer.model

import com.magmanics.licensing.model.{BoolOption, ListOption, Product, TextOption}
import org.testng.annotations.Test

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 10-Aug-2010
 */

class ProductTest {

  @Test
  def createWithOptions {
    val boolOption = BoolOption(name = "Boolean option", default = true)
    val textOption = TextOption(name = "Text option", default = "default value")
    val listOption = ListOption(name = "List option", default = "default value", values = Set("Option one", "Option two"))

    val product = Product(name = "PDM", description = "Document archive", options = Set(boolOption, textOption, listOption))
  }

  @Test
  def enabledByDefault {
    assert(Product(name = "Ubuntu 10.04").enabled)
  }
}