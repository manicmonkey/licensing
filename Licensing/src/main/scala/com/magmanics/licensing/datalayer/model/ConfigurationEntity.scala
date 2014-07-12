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
import java.util.Date
import javax.persistence._

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 28-May-2010
 * Time: 20:13:57
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Configurations")
@NamedQueries(Array(
  new NamedQuery(name = "Configuration.GetByCustomer", query = "SELECT c FROM ConfigurationEntity c WHERE c.customer = :customer"),
  new NamedQuery(name = "Configuration.GetBySerial", query = "SELECT c FROM ConfigurationEntity c WHERE c.serial = :serial")
))
class ConfigurationEntity {

  @Id
  @GeneratedValue
  var id: Long = _

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  var created: Date = _

  @Column(nullable = false)
  var user: String = _

  @Column(nullable = false)
  var enabled: Boolean = _

  @Column(nullable = false, unique = true)
  var serial: String = _

  @Column(nullable = false)
  var maxActivations: Int = _

  @Column(name = "customer_id")
  var customerId: Long = _

  @Column(name = "product_id")
  var productId: Long = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "customer_id", updatable = false, insertable = false)
  var customer: CustomerEntity = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id", updatable = false, insertable = false)
  var product: ProductEntity = _

  @OneToMany(mappedBy = "configuration", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var activations: java.util.List[ActivationEntity] = _

  @OneToMany(mappedBy = "configuration", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var options: java.util.List[ConfigurationOptionEntity] = _

  def addOption(option: ConfigurationOptionEntity) {
    if (options == null)
      options = new util.ArrayList[ConfigurationOptionEntity]()
    options.add(option)
  }
}

@Entity
@Table(name = "configuration_options")
@UniqueConstraint(name = "unique_configuration_option_key", columnNames = Array("configuration_id", "key"))
class ConfigurationOptionEntity {

  @Id
  @GeneratedValue
  var id: Int = _

  @Basic
  @Column(nullable = false)
  var key: String = _

  @Basic
  @Column(nullable = false)
  var value: String = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "configuration_id")
  var configuration: ConfigurationEntity = _
}