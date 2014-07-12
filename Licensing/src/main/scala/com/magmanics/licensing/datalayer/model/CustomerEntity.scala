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

import javax.persistence._

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 28-May-2010
 * Time: 20:28:12
 * To change this template use File | Settings | File Templates.
 */

//todo http://blog.knoldus.com/2014/01/20/scala-slick-2-0-for-multi-database/
@Entity
@Table(name = "Customers")
@NamedQueries(Array(
  new NamedQuery(name = "Customer.GetByName", query = "SELECT c FROM CustomerEntity c WHERE c.name = :name"),
  new NamedQuery(name = "Customer.GetAll", query = "SELECT c FROM CustomerEntity c"),
  new NamedQuery(name = "Customer.GetEnabled", query = "SELECT c FROM CustomerEntity c WHERE c.enabled = true")
))
class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = _

  @Basic
  @Column(nullable = false, unique = true)
  var name: String = _

  @Basic
  @Column(nullable = false)
  var enabled: Boolean = _

  @OneToMany(mappedBy = "customer", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var configurations: java.util.Set[ConfigurationEntity] = _
}