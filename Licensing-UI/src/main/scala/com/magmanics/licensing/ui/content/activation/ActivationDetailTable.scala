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

package com.magmanics.licensing.ui.content.activation

import com.magmanics.licensing.model.Activation
import com.vaadin.data.Property
import com.vaadin.ui.Table
import org.slf4j.LoggerFactory

import scala.math._

/**
 * @author jbaxter - 22/04/11
 */

class ActivationDetailTable extends Table {

  val log = LoggerFactory.getLogger(classOf[ActivationDetailTable])

  addContainerProperty("name", classOf[String], null)
  addContainerProperty("value", classOf[String], null)

  setSizeFull()
  setColumnHeader("name", "Name")
  setColumnHeader("value", "Value")

  def setActivation(activation: Activation) {
    log.debug("Model clicked: {}", activation)
    activation.extraInfo.foreach(info => {
      val item = addItem(info._1)
      item.getItemProperty("name").asInstanceOf[Property[String]].setValue(info._1)
      item.getItemProperty("value").asInstanceOf[Property[String]].setValue(info._2)
    })
    markAsDirty()
    setPageLength(min(size, 8))
  }
}