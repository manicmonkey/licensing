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
import com.magmanics.vaadin.component.{HtmlLabel, LinkButton}
import com.magmanics.vaadin.spring.SpringContextHelper
import com.vaadin.annotations.{Theme, Title}
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.navigator.{Navigator, ViewChangeListener}
import com.vaadin.server._
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui._

/**
 * @author jbaxter - 06/04/11
 */
@Theme("licensing")
@Title("Licensing application")
class LicensingApplication extends UI {

  lazy val springContextHelper = new SpringContextHelper(VaadinServlet.getCurrent.getServletContext)

  val container = new MainContentContainer

  lazy val homeContent = new HomeContent
  lazy val activationContent = new ActivateLicenceContent
  lazy val managementContent = new LicenceManagementContent
  lazy val administrationContent = new AdministrationContent
  lazy val auditContent = springContextHelper.getBean(classOf[AuditLogContent])
  lazy val customerContent = new CustomerContent
  lazy val productContent = new ProductContent
  lazy val userContent = new UserContent

  override def init(request: VaadinRequest) {

    //setup back/forward button support
    val navigator = new Navigator(this, container)
    navigator.addView(HomeContent.name, homeContent)
    navigator.addView(LicenceManagementContent.name, managementContent)
    navigator.addView(ActivateLicenceContent.name, activationContent)
    navigator.addView(AdministrationContent.name, administrationContent)
    navigator.addView(AuditLogContent.name, auditContent)
    navigator.addView(CustomerContent.name, customerContent)
    navigator.addView(ProductContent.name, productContent)
    navigator.addView(UserContent.name, userContent)

    //main ui elements
    val header = new HtmlLabel("<h1>Product Licensing</h1>")
    val menu = new Menu(navigator)

    //layout
    setContent(new VerticalLayout {
      setMargin(true)
      setSpacing(true)
      //header
      addComponent(new Panel {
        setContent(new HorizontalLayout {
          setMargin(new MarginInfo(false, false, false, true)) //indent text slightly
          setWidth("100%") //stretch to fill screen
          addComponent(header)
        })
        setHeight(null) //shrink height to fit content
        setWidth("100%") //stretch to fill screen
      })
      menu.setSpacing(true)
      addComponent(menu)
      addComponent(container)
    })
  }

  class Menu(navigator: Navigator) extends HorizontalLayout {

    val arrow = new Image(null, new ThemeResource("img/right_arrow.png"))

    val home = new HorizontalLayout(
      new Image(null, new ThemeResource("img/rectangle.png")),
      new LinkButton("Home", HomeContent.name, navigator),
      new Image(null, new ThemeResource("img/right_arrow.png"))
    )
    home.setSpacing(true)

    val context = new HorizontalLayout()
    context.setSpacing(true)

    val activationLink = new LinkButton("Licence activation", ActivateLicenceContent.name, navigator)
    val managementLink = new LinkButton("Licence management", LicenceManagementContent.name, navigator)

    val adminLink = new LinkButton("Administration", AdministrationContent.name, navigator)
    val auditLink = new LinkButton("Auditing", AuditLogContent.name, navigator)
    val customerLink = new LinkButton("Customers", CustomerContent.name, navigator)
    val productLink = new LinkButton("Products", ProductContent.name, navigator)
    val userLink = new LinkButton("Users", UserContent.name, navigator)

    navigator.addViewChangeListener(new ViewChangeListener {
      override def beforeViewChange(event: ViewChangeEvent) = true
      override def afterViewChange(event: ViewChangeEvent) {
        context.removeAllComponents()
        event.getViewName match {
          case HomeContent.name =>
            context.addComponent(activationLink)
            context.addComponent(managementLink)
            context.addComponent(adminLink)
          case ActivateLicenceContent.name =>
            context.addComponent(activationLink)
          case LicenceManagementContent.name =>
            context.addComponent(managementLink)
          case AdministrationContent.name =>
            context.addComponent(adminLink)
            context.addComponent(arrow)
            context.addComponent(auditLink)
            context.addComponent(customerLink)
            context.addComponent(productLink)
            context.addComponent(userLink)
          case AuditLogContent.name =>
            context.addComponent(adminLink)
            context.addComponent(arrow)
            context.addComponent(auditLink)
          case CustomerContent.name =>
            context.addComponent(adminLink)
            context.addComponent(arrow)
            context.addComponent(customerLink)
          case ProductContent.name =>
            context.addComponent(adminLink)
            context.addComponent(arrow)
            context.addComponent(productLink)
          case UserContent.name =>
            context.addComponent(adminLink)
            context.addComponent(arrow)
            context.addComponent(userLink)
        }
      }
    })
    addComponent(home)
    addComponent(context)
  }
}