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

import com.vaadin.data.Property
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.AbstractSelect.ItemCaptionMode
import com.vaadin.ui.Table
import org.slf4j.LoggerFactory

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 17-Jun-2010
 */
class ConfigurationOverviewTable extends Table {

  val log = LoggerFactory.getLogger(classOf[ConfigurationOverviewTable])

  val container = new BeanItemContainer[ConfigurationInfo](classOf[ConfigurationInfo])
  setContainerDataSource(container)

  setVisibleColumns("product", "created", "user", "activations", "serial")
  setColumnHeaders("Product", "Created", "User", "Activations", "Serial")
  setItemCaptionMode(ItemCaptionMode.PROPERTY)

  setSelectable(true)
  setNullSelectionAllowed(false)
  setImmediate(true)

  def setConfigurations(c: Seq[ConfigurationInfo]) {

    container.removeAllItems()

    c.foreach(container.addBean)

    if (container.size > 0) {
//        setPageLength(min(container.size + 1, 4))
        select(firstItemId)
    }
  }

  def addListener(configurationSelectedListener: ConfigurationInfo => Unit) {
    addValueChangeListener(new Property.ValueChangeListener() {
      override def valueChange(event: Property.ValueChangeEvent) {
        val configuration = event.getProperty.getValue.asInstanceOf[ConfigurationInfo]
        assert(configuration != null, "Null selection not allowed")
        configurationSelectedListener(configuration)
      }
    })
  }

  setCellStyleGenerator(new Table.CellStyleGenerator {
      def getStyle(source: Table, itemId: Any, propertyId: Any): String = {
          if (propertyId == null) {
              // no propertyId, styling row
//                    if (markedRows.contains(itemId))
//                      return "marked"
//                    else
                null
//                } else if (ExampleUtil.iso3166_PROPERTY_NAME.equals(propertyId)) {
//                    return "bold";
          } else {
              // no style
              null
          }
      }
  })
}
