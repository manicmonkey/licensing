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

import com.magmanics.licensing.service.model.Activation
import com.vaadin.data.Property
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.{AbstractSelect, Table}
import org.slf4j.LoggerFactory

import scala.math._

/**
 * @author jbaxter - 22/04/11
 */

class ActivationOverviewTable extends Table {

  val log = LoggerFactory.getLogger(classOf[ActivationOverviewTable])

  val container = new BeanItemContainer[Activation](classOf[Activation])
  setContainerDataSource(container)

  setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY)
  setVisibleColumns(Array("created", "machineIdentifier", "productVersion", "activationType"))
  setColumnHeaders(Array("Created", "Machine Identifier", "Product version", "Activation type"))

  setSelectable(true)
  setNullSelectionAllowed(false)

  setImmediate(true)

  def setActivations(activations: Seq[Activation]) {

    container.removeAllItems()

    activations.foreach(container.addBean(_))

    if (container.size > 0) {
      select(firstItemId)
      setPageLength(min(container.size, 4))
    }
  }

  def addListener(activationSelectionListener: Activation => Unit) {
    addListener(new Property.ValueChangeListener() {
      override def valueChange(event: Property.ValueChangeEvent) {
        val activation = event.getProperty.getValue.asInstanceOf[Activation]
        assert(activation != null, "Null selection not allowed")
        activationSelectionListener(activation)
      }
    })
  }
}