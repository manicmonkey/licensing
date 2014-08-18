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

package com.magmanics.licensing.model

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 02-Aug-2010
 */
//todo trait?
//abstract class ProductOption[T](val id: Option[Long],
//                                var name: String,
//                                var default: T)
trait ProductOption[T] {
  val id: Option[Long]
  var name: String
  var default: T
}

case class TextOption(id: Option[Long] = None, var name: String, var default: String) extends ProductOption[String]
case class BoolOption(id:Option[Long] = None, var name: String, var default: Boolean) extends ProductOption[Boolean]
case class ListOption(id:Option[Long] = None, var name: String, var default: String, values: Seq[String]) extends ProductOption[String]

//case class TextOption(_id:Option[Long] = None,
//                      _name: String,
//                      _default: String) extends ProductOption[String](_id, _name, _default) //, var value: String

//case class BoolOption(_id:Option[Long] = None,
//                      _name: String,
//                      _default: Boolean) extends ProductOption[Boolean](id = _id, name = _name, default = _default) //, var value: Boolean
//
//case class ListOption(_id:Option[Long] = None,
//                      _name: String,
//                      _default: String,
//                      values: Seq[String]) extends ProductOption[String](id = _id, name = _name, default = _default)