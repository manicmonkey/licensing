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

package com.magmanics.licensing.ui

import com.magmanics.licensing.ui.content._
import com.magmanics.vaadin.ClickHandler
import com.magmanics.vaadin.component.HtmlLabel
import com.magmanics.vaadin.spring.SpringContextHelper
import com.vaadin.annotations.{Theme, Title}
import com.vaadin.server.{VaadinRequest, VaadinServlet}
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui._

/**
 * @author jbaxter - 06/04/11
 */
@Theme("valo")
@Title("Licensing application")
class LicensingApplication extends UI {

  lazy val springContextHelper = new SpringContextHelper(VaadinServlet.getCurrent.getServletContext)

  lazy val auditLogContent = springContextHelper.getBean(classOf[AuditLogContent])

  val container = new MainContentContainer

  override def init(request: VaadinRequest) {

    setContent(new VerticalLayout {
      setMargin(true)
      setSpacing(true)
      //header
      addComponent(new Panel {
        setContent(new HorizontalLayout {
          setMargin(new MarginInfo(false, false, false, true)) //indent text slightly
          setWidth("100%") //stretch to fill screen
          addComponent(new HtmlLabel("<h1>Product Licensing</h1>"))
        })

        setHeight(null) //shrink height to fit content
        setWidth("100%") //stretch to fill screen
      })
      //menu
      addComponent(new HorizontalLayout(
        new Button("Licence activation") {
          addClickListener(new ClickHandler(_ => container.walkTo("licence-activation")))
        },
        new Button("Licence management") {
          addClickListener(new ClickHandler(_ => container.walkTo("licence-management")))
        },
        new Button("Administration") {
          addClickListener(new ClickHandler(_ => container.walkTo("administration")))
        }
      ) {
        setSpacing(true)
      })
      //content
      addComponent(container)
    })

//    container.updateContent(initialContent)
//    container.updateContent(new HomeContent)
//    container.updateContent(new LicenceManagementContent)
    container.updateContent(auditLogContent)
  }

  lazy val initialContent = new MainContent {
    addComponent(new HtmlLabel("<h3>Please login...</h3>"))
    addComponent(new LoginForm)
  }
}