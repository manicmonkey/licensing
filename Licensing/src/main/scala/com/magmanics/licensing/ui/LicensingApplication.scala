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

import com.magmanics.licensing.ui.breadcrumb._
import com.magmanics.licensing.ui.content._
import com.magmanics.vaadin.component.HtmlLabel
import com.magmanics.vaadin.spring.VaadinComponent
import com.vaadin.Application
import com.vaadin.ui._
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author jbaxter - 06/04/11
 */
@VaadinComponent
class LicensingApplication @Autowired() (auditLogContent: AuditLogContent) extends Application with CrumbWalkableComponent {

  val breadCrumbFragmentManager = new BreadCrumbFragmentManager
  val breadCrumbManager = new BreadCrumbListener

  val crumbTrail = new BreadCrumbPanel
  val container = new MainContentContainer

  def init() {
    val window = new Window("Licensing application")
    window.setContent(new VerticalLayout {
      setMargin(true)
      setSpacing(true)
    })
    window.addComponent(breadCrumbManager)
    window.addComponent(new Header)
    window.addComponent(crumbTrail)
    window.addComponent(container)
    window.setSizeFull()
    setMainWindow(window)

//    container.updateContent(initialContent)
//    container.updateContent(new HomeContent)
    container.updateContent(new LicenceManagementContent)
//    container.updateContent(auditLogContent)
    crumbTrail.updateDisplay(List(""))
  }

  lazy val initialContent = new MainContent {
    addComponent(new HtmlLabel("<h3>Please login...</h3>"))
    addComponent(new LoginForm)
  }

  def walkTo(path: String) = {
    container.walkTo(path)
  }
}

class Header extends HorizontalLayout {
  addComponent(new Panel {
    setContent(new HorizontalLayout {
      setMargin(false, false, false, true) //indent text slightly
      setWidth("100%") //stretch to fill screen
    })
    addComponent(new HtmlLabel("<h1>Product Licensing</h1>"))
  })
  setHeight(null) //shrink height to fit content
  setWidth("100%") //stretch to fill screen
}

class BreadCrumbPanel extends HorizontalLayout {

  private val breadCrumbDesigner = new BreadCrumbDesigner

  private val breadCrumbContainer = new HorizontalLayout {
    setSpacing(true)
  }

  addComponent(new Panel {
    setContent(new HorizontalLayout {
      setMargin(true, false, true, true) //padding to sides and beneath
      setWidth("100%") //stretch to fit screen
    })
    addComponent(breadCrumbContainer)
  })
  setHeight(null) //shrink height to fit content
  setWidth("100%") //stretch to fill screen

  def updateDisplay(crumbs: List[String]) {
    breadCrumbContainer.removeAllComponents()
    breadCrumbContainer.addComponent(new Label("You are here:") {
      setWidth(null)
    })
    breadCrumbDesigner.build(crumbs).foreach(breadCrumbContainer.addComponent(_))
  }
}