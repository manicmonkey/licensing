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

package com.magmanics.vaadin

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.event.ItemClickEvent
import com.vaadin.event.ItemClickEvent.ItemClickListener
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer

/**
 * todo settle on an event dispatch mechanism
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 09-Oct-2010
 */
trait ModelClickListenable[T] extends ItemClickListener {

  def log: Logger

  val modelClickListeners = ListBuffer[ModelClickListener[T]]()

  override def itemClick(event: ItemClickEvent) {
    val item = event.getItemId.asInstanceOf[T]
    log.debug("Item clicked: {}", item)
    val selected: Option[T] = item match {
                     case null => None
                     case t => Some(t)
                   }
    modelClickListeners.foreach(_.modelClicked(selected))
  }

//  def addListener(listener: Property.ValueChangeListener)

//  addListener(this.asInstanceOf[Property.ValueChangeListener])
}

trait ModelValueListenable[T] extends Property.ValueChangeListener {

  val log = LoggerFactory.getLogger(classOf[ModelClickListener[T]])

  val modelClickListeners = ListBuffer[ModelClickListener[T]]()

  override def valueChange(event: ValueChangeEvent) {
    val item = event.getProperty.getValue.asInstanceOf[T]
    log.debug("Got event: " + item)
    val selected: Option[T] = item match {
                     case null => None
                     case t => Some(t)
                   }
    modelClickListeners.foreach(_.modelClicked(selected))
  }

//  def addListener(listener: Property.ValueChangeListener)

//  addListener(this.asInstanceOf[Property.ValueChangeListener])
}