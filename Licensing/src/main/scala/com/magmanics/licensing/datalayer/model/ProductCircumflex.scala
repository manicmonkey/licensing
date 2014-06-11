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

import ru.circumflex.orm._

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 28-May-2010
 * Time: 20:18:13
 * To change this template use File | Settings | File Templates.
 */

class ProductCircumflex extends Record[ProductCircumflex] {

  val name = "name" VARCHAR(100)
  val description = "description" VARCHAR(200)
  val enabled = "enabled" BOOLEAN

  def configurations = inverse(ConfigurationCircumflex.product)

  def listOptions = inverse(ListProductOptionCircumflex.product)
  def textOptions = inverse(TextProductOptionCircumflex.product)
  def radioOptions = inverse(RadioProductOptionCircumflex.product)

  def getOptions: Seq[ProductOptionCircumflex[Any]] = {
    val productOptions = Seq[ProductOptionCircumflex[Any]]() ++ listOptions.apply ++ textOptions.apply ++ radioOptions.apply
    productOptions.sortBy(_.getLabel)
  }
}

object ProductCircumflex extends Table[ProductCircumflex] {
  UNIQUE(this.name)
  validation.notEmpty((p: ProductCircumflex) => p.name)
}