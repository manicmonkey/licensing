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
 * Date: 12-Jun-2010
 * Time: 01:34:18
 * To change this template use File | Settings | File Templates.
 */
trait ProductOptionCircumflex[+T] {

  def name: TextField

  def getLabel(): String = name.getValue
  def setLabel(label: String) = name := label

  def getDefault: T
  def setDefault[U >: T](default: U)
}

class TextProductOptionCircumflex extends Record[TextProductOptionCircumflex] with ProductOptionCircumflex[String] {

  val name = "name" VARCHAR(100) NOT_NULL
  val default = "default" VARCHAR(255) NOT_NULL
  val product = "product_id" REFERENCES(ProductCircumflex) ON_DELETE CASCADE

  override def getDefault() = default.getValue
  override def setDefault[U >: String](defaultValue: U) = default := defaultValue.asInstanceOf[String]
}

object TextProductOptionCircumflex extends Table[TextProductOptionCircumflex]

class RadioProductOptionCircumflex extends Record[RadioProductOptionCircumflex] with ProductOptionCircumflex[Boolean] {

  val name = "name" VARCHAR(100) NOT_NULL
  val default = "default" BOOLEAN
  val product = "product_id" REFERENCES(ProductCircumflex) ON_DELETE CASCADE

  override def getDefault() = default.getValue
  override def setDefault[U >: Boolean](defaultValue: U) = default := defaultValue.asInstanceOf[Boolean]
}

object RadioProductOptionCircumflex extends Table[RadioProductOptionCircumflex]

class ListProductOptionCircumflex extends Record[ListProductOptionCircumflex] with ProductOptionCircumflex[String] {

  val name = "name" VARCHAR(100) NOT_NULL
  val default = "default" VARCHAR(100) NOT_NULL
  val product = "product_id" REFERENCES(ProductCircumflex) ON_DELETE CASCADE
  
  def values = inverse(ListProductOptionValueCircumflex.listProductOption)

  override def getDefault() = default.getValue
  override def setDefault[U >: String](defaultValue: U) = default := defaultValue.asInstanceOf[String]
}

object ListProductOptionCircumflex extends Table[ListProductOptionCircumflex]

class ListProductOptionValueCircumflex extends Record[ListProductOptionValueCircumflex] {

  def this(myValue: String, parentList: ListProductOptionCircumflex) = {
    this()
    value := myValue
    listProductOption := parentList
  }

  val value = "value" VARCHAR(100) NOT_NULL
  val listProductOption = "list_product_option_id" REFERENCES(ListProductOptionCircumflex) ON_DELETE CASCADE
}

object ListProductOptionValueCircumflex extends Table[ListProductOptionValueCircumflex]

  /**
   * for o in objects {
   *   print o.name
   *   o.type match {
   *     case "TEXT" => display textfield(o.default.getOrElse(""))
   *     case "RADIO" => display radio(o.default.getOrElse(false))
   *     case "LIST" => display list(o.values, o.default.get)
   *   }
   * }
   */