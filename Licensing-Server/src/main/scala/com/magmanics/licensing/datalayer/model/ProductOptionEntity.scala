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

import java.util
import javax.persistence._

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-Jun-2010
 * Time: 01:34:18
 * To change this template use File | Settings | File Templates.
 */
trait ProductOptionEntity[T] {

  var name: String

  def getDefault: T
  def setDefault(default: T)
}

@Entity
@Table(name = "product_options_string")
class TextProductOptionEntity extends ProductOptionEntity[String] {

  @Id
  @GeneratedValue
  var id: Long = _

  @Basic
  @Column(nullable = false)
  var name: String = _

  @Basic
  @Column(nullable = false)
  var default: String = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  var product: ProductEntity = _

  override def getDefault = default
  override def setDefault(defaultValue: String) = default = defaultValue
}

@Entity
@Table(name = "product_options_boolean")
class RadioProductOptionEntity extends ProductOptionEntity[Boolean] {

  @Id
  @GeneratedValue
  var id: Long = _

  @Basic
  @Column(nullable = false)
  var name: String = _

  @Basic
  @Column(nullable = false)
  var default: Boolean = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  var product: ProductEntity = _

  override def getDefault = default
  override def setDefault(defaultValue: Boolean) = default = defaultValue
}

@Entity
@Table(name = "product_options_list")
class ListProductOptionEntity extends ProductOptionEntity[String] {

  @Id
  @GeneratedValue
  var id: Long = _

  @Basic
  @Column(nullable = false)
  var name: String = _

  @Basic
  @Column(nullable = false)
  var default: String = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  var product: ProductEntity = _

  @OneToMany(mappedBy = "listProductOption", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var optionValues: java.util.List[ListProductOptionValueEntity] = _

  def addOptionValue(optionValue: ListProductOptionValueEntity) {
    if (optionValues == null)
      optionValues = new util.ArrayList[ListProductOptionValueEntity]()
    optionValues.add(optionValue)
  }

  override def getDefault = default
  override def setDefault(defaultValue: String) = default = defaultValue
}

@Entity
@Table(name = "product_options_list_values")
class ListProductOptionValueEntity extends {

  def this(myValue: String, parentList: ListProductOptionEntity) = {
    this()
    value = myValue
    listProductOption = parentList
  }

  @Id
  @GeneratedValue
  var id: Long = _

  @Basic
  @Column(nullable = false)
  var value: String = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "list_product_option_id")
  var listProductOption: ListProductOptionEntity = _
}

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