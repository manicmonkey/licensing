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

package com.magmanics.licensing.ui.content.user

import com.magmanics.licensing.model.User
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.AbstractSelect.ItemCaptionMode
import com.vaadin.ui.ComboBox

/**
 * @author jbaxter - 22/06/11
 */
class UserSelectionComboBox(users: Seq[User]) extends ComboBox {

  setInputPrompt("Please select a user")
  val container = new BeanItemContainer[User](classOf[User])
  setContainerDataSource(container)
  setItemCaptionMode(ItemCaptionMode.PROPERTY)
  setItemCaptionPropertyId("name")
  setNullSelectionAllowed(false)

  users.sortBy(_.name).foreach(container.addBean)

  def onUserChanged(handler: User => Unit) {
    addValueChangeListener(new ValueChangeListener {
      override def valueChange(event: ValueChangeEvent) {
        handler(event.getProperty.getValue.asInstanceOf[User])
      }
    })
  }
}