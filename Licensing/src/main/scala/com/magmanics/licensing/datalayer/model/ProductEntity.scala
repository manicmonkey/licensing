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
 * User: James
 * Date: 28-May-2010
 * Time: 20:18:13
 */
@Entity
@Table(name = "products")
@NamedQueries(Array(
  new NamedQuery(name = "Product.GetAll", query = "SELECT p FROM ProductEntity p"),
  new NamedQuery(name = "Product.GetByName", query = "SELECT p FROM ProductEntity p WHERE p.name = :name")
))
class ProductEntity {

  @Id
  @GeneratedValue
  var id: Long = _

  @Basic
  @Column(nullable = false, unique = true)
  var name: String = _

  @Basic
  var description: String = _

  @Basic
  @Column(nullable = false)
  var enabled: Boolean = _

  @OneToMany(mappedBy = "product", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var configurations: java.util.Set[ConfigurationEntity] = _

  @OneToMany(mappedBy = "product", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var listOptions: java.util.Set[ListProductOptionEntity] = _

  @OneToMany(mappedBy = "product", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var textOptions: java.util.Set[TextProductOptionEntity] = _

  @OneToMany(mappedBy = "product", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var radioOptions: java.util.Set[RadioProductOptionEntity] = _

  def getListOptions = {
    if (listOptions == null)
      listOptions = new util.HashSet[ListProductOptionEntity]()
    listOptions
  }

  def getTextOptions = {
    if (textOptions == null)
      textOptions = new util.HashSet[TextProductOptionEntity]()
    textOptions
  }

  def getRadioOptions = {
    if (radioOptions == null)
      radioOptions = new util.HashSet[RadioProductOptionEntity]()
    radioOptions
  }

  def getOptions: Seq[_ <: ProductOptionEntity[_]] = {
    import scala.collection.JavaConverters._
    val productOptions: scala.collection.mutable.Set[_ <: ProductOptionEntity[_]] = getListOptions.asScala ++ getTextOptions.asScala ++ getRadioOptions.asScala
    productOptions.toSeq.sortBy(_.name)
  }

  def addOption(option: RadioProductOptionEntity) {
    getRadioOptions.add(option)
  }

  def addOption(option: TextProductOptionEntity) {
    getTextOptions.add(option)
  }

  def addOption(option: ListProductOptionEntity) {
    getListOptions.add(option)
  }
}