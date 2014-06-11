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
import org.slf4j.Logger

/**
 * Add a nice of scala-ness to Vaadin
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 20-Jun-2010
 */

trait ValueChanger[A] {

  def log: Logger

  /*
   * implementations to be provided by users of this trait
   */
  def addListener(listener: Property.ValueChangeListener)
  def getValue: Any

  /**
   * Add a ValueChangeListener using function passing
   */
  def addListener(f: Option[A] => Unit) {
    addListener(new Property.ValueChangeListener {
      def valueChange(e: Property.ValueChangeEvent) {
        log.debug("Raising listener for: {}", getValue)
        val value = getValue
        if (value != null)
          f(Some(value.asInstanceOf[A]))
        else
          f(None)
      }
    })
  }
}