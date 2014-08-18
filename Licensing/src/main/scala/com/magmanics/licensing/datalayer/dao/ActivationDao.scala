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

package com.magmanics.licensing.datalayer.dao

import javax.persistence.{EntityManager, PersistenceContext}

import com.magmanics.licensing.datalayer.model.{ActivationEntity, ActivationInfoEntity}
import com.magmanics.licensing.model.Activation
import org.slf4j.LoggerFactory

/**
 * DAO for creating [[com.magmanics.licensing.service.model.Activation Activations]]. Retrieval is facilitated through the association 'activations' on [[com.magmanics.licensing.service.model.Configuration Configuration]].
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 28-Jul-2010
 */
trait ActivationDao {
  /**
   * Persist a new Activation and associate with the given Configuration id
   */
  def create(activation: Activation)
}

class ActivationDaoJPA extends ActivationDao {

  val log = LoggerFactory.getLogger(classOf[ActivationDaoJPA])

  @PersistenceContext
  var em: EntityManager = _

  override def create(activation: Activation) {

    log.debug("Creating Activation({}) against Configuration({})", activation, activation.configurationId)
    val a = new ActivationEntity() //todo constructor in data model could take service model?
    a.created = activation.created
    a.configurationId = activation.configurationId
    a.activationType = activation.activationType.toString
    a.machineIdentifier = activation.machineIdentifier
    a.productVersion = activation.productVersion

    activation.extraInfo.foreach(info => {
      val i = new ActivationInfoEntity
      i.activation = a
      i.key = info._1
      i.value = info._2
      a.addActivationInfo(i)
    })

    em.persist(a)
  }
}