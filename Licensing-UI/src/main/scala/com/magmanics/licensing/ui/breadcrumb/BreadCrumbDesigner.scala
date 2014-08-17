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

package com.magmanics.licensing.ui.breadcrumb

import com.magmanics.licensing.ui.{CrumbTrailMessageResource, MessageResource}
import com.magmanics.vaadin.component.{LinkButton, UndefinedWidth}
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.{Component, Label}

import scala.collection.immutable.List

/**
 * This component is responsible for building a list of components, which when laid out on a canvas, represent a bread crumb trail
 *
 * @author jbaxter - 07/04/11
 */
class BreadCrumbDesigner(messageResource: MessageResource = new CrumbTrailMessageResource) {

  private val homeLink = new LinkButton(messageResource.getMessage("home"), "/")

  //admin, users ->
  //List('/', '/admin', '/admin/users')
  //List('Home', 'Admin', 'Users')
  //CrumbLink('Home', '/'), CrumbDivider, CrumbLink('Admin', '/admin'), CrumbDivider, CrumbLink('Users', '/admin/users')

  def getBreadCrumbComponents(fragments: List[String]): List[CrumbComponent] = {
    val reversedFragments = ("home" :: fragments).reverse
//    reversedFragments.tail.foldRight(messageResource.getMessage(_))
    List(CrumbDivider())
  }

  /**
   * Build a list of components which visually represent the given breadcrumb trail. A home link is always implicitly added
   * @param links May be an empty list, must not be null
   */
  def build(links: List[String]): List[Component] = {
    val reversed: List[String] = ("/" :: links).reverse
    val linkAndDividerLists: List[List[Component]] = reversed.tail.map(l => List(divider, getLink(l)))
    val linksAndDividers: List[Component] = linkAndDividerLists.flatMap(s => s)
    val currentLocation: Component = new Label(reversed.head, ContentMode.TEXT) with UndefinedWidth
    val mapped = currentLocation :: linksAndDividers
    mapped.reverse
  }

  private def divider = {
    val divider = new Label(">", ContentMode.TEXT)
    divider.setWidth(null)
    divider
  }

  private def getLink(l: String) = {
    val caption = l match {
      case "/" => messageResource.getMessage("home")
      case _ => messageResource.getMessage(l)
    }
    new LinkButton(caption, l) with UndefinedWidth
  }
}

trait CrumbComponent

case class CrumbLink(label: String, path: String) extends CrumbComponent
case class CrumbLabel(label: String) extends CrumbComponent
case class CrumbDivider() extends CrumbComponent