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

package com.magmanics.licensing.ui.content.configuration

import org.slf4j.LoggerFactory
import scala.math._
import com.vaadin.ui.Table
import com.magmanics.licensing.service.model.Configuration

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 22 -Jun-2010
 */
class ConfigurationDetailTable extends Table {

  val log = LoggerFactory.getLogger(classOf[ConfigurationDetailTable])

  addContainerProperty("property", classOf[String], null, "Option", null, null)
  addContainerProperty("value", classOf[String], null, "Value", null, null)

  def setConfiguration(c: Configuration) {

    removeAllItems()

    log.debug("Showing options for: {}", c)

    c.options.foreach(o => addItem(Array(o._1, o._2), o._1))

    setPageLength(min(size, 8))
  }
}