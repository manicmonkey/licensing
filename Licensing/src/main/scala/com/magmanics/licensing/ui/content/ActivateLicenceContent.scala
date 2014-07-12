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

package com.magmanics.licensing.ui.content

import com.magmanics.vaadin.component.HtmlLabel
import com.vaadin.ui._

/**
 * @author jbaxter - 06/04/11
 */
class ActivateLicenceContent extends MainContent {
  addComponent(new HtmlLabel("<h3>Please choose from the following options...</h3>"))
  addComponent(new HorizontalLayout {
    addComponent(new HtmlLabel("<ul><li></li></ul>"))
    addComponent(new VerticalLayout {
      addComponent(new HtmlLabel("<h4>Paste the activation code</h4>"))
      addComponent(new HorizontalLayout {
        addComponent(new TextArea())
        val submit = new Button("Submit")
        addComponent(submit)
        setComponentAlignment(submit, Alignment.BOTTOM_CENTER)
        setSpacing(true)
      })
    })
  })
  addComponent(new HorizontalLayout {
    addComponent(new HtmlLabel("<ul><li></li></ul>"))
    addComponent(new VerticalLayout {
      addComponent(new HtmlLabel("<h4>Upload the licence file</h4>"))
      val upload = new Upload(null, null) {
        setImmediate(true)
        setButtonCaption("Select file")
      }
      addComponent(upload)
      setComponentAlignment(upload, Alignment.MIDDLE_RIGHT)
    })
  })
}













