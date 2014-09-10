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
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 28-May-2010
 */
@Entity
@Table(name = "activations")
class ActivationEntity {

  def this(machineIdentifier: String, productVersion: String, activationType: String) {
    this()
    this.machineIdentifier = machineIdentifier
    this.productVersion = productVersion
    this.activationType = activationType
  }

  @Id
  @GeneratedValue
  var id: Long = _

  @Column(nullable = false)
  var created: Date = _ //todo default value...

  @Column(nullable = false)
  var machineIdentifier: String = _

  @Column(nullable = false)
  var productVersion: String = _

  @Column(nullable = false)
  var activationType: String = _ //todo ActivationType enum ('NEW' or 'UPGRADE')

  @Column(name = "configuration_id")
  var configurationId: Long = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "configuration_id", updatable = false, insertable = false)
  var configuration: ConfigurationEntity = _

  @OneToMany(mappedBy = "activation", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  var activationInfo: java.util.Set[ActivationInfoEntity] = _

  def getActivationInfo() = {
    if (activationInfo == null)
      activationInfo = new util.HashSet[ActivationInfoEntity]()
      activationInfo
  }

  def addActivationInfo(info: ActivationInfoEntity) {
    val activationInfo = getActivationInfo()
    activationInfo.add(info)
  }

  //  validation.add(a => { todo ensure can't go over limit
//    if (configuration.relation.)
//  })
}

@Entity
@Table(name = "activation_info")
class ActivationInfoEntity {

  @Id
  @GeneratedValue
  var id: Int = _

  @Basic
  var key: String = _

  @Basic
  var value: String = _

  @ManyToOne(optional = false)
  @JoinColumn(name = "activation_id")
  var activation: ActivationEntity = _
}