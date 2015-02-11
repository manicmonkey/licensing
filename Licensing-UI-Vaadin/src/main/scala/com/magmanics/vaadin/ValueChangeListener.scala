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
import org.slf4j.LoggerFactory

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 12-Jun-2010
 */

class ValueChangeListener[T](handler: Option[T] => Unit) extends Property.ValueChangeListener {

  val log = LoggerFactory.getLogger(classOf[ValueChangeListener[AnyRef]])

  override def valueChange(event: Property.ValueChangeEvent) {
    log.debug("Got ValueChangeEvent: {}", event)
    val value = event.getProperty.getValue
    val option = if (value == null) None else Some(value.asInstanceOf[T])
    handler(option)
  }
}