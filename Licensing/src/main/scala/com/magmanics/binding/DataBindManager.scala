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

package com.magmanics.binding

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.collection.mutable.HashMap
import scala.ref.WeakReference

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 14/02/11
 */
@Component
@Scope("singleton")
class DataBindManager {

  private val log = LoggerFactory.getLogger(classOf[DataBindManager])

  val binds = HashMap[Class[AnyRef], Seq[WeakReference[AnyRef]]]()

  def registerForUpdates(bean: AnyRef, eventTypes: Seq[Class[AnyRef]]) {
    eventTypes.foreach(e => {
      val existingBeans = binds.getOrElse(e, List())
      binds.put(e, existingBeans :+ new WeakReference(bean))
    })
  }

  def pushUpdates(source: Class[AnyRef]) {
    binds(source).foreach(seq => {
      seq.get match {
        case Some(bean) => invokeUpdate(bean)
        case None => //todo remove old references now and then
      }
    })
  }

  private def invokeUpdate(bean: AnyRef) {
    log.debug("Need to implement subscriber method!!")
  }
}