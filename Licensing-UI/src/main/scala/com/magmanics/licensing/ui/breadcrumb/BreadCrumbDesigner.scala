package com.magmanics.licensing.ui.breadcrumb

import com.vaadin.ui.{Label, Component}
import collection.immutable.List
import com.magmanics.licensing.ui.component.{UndefinedWidth, LinkButton}
import com.magmanics.licensing.ui.{MessageResource, CrumbTrailMessageResource, MockMessageResource}

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
    val currentLocation: Component = new Label(reversed.head, Label.CONTENT_TEXT) with UndefinedWidth
    val mapped = currentLocation :: linksAndDividers
    mapped.reverse
  }

  private def divider = {
    val divider = new Label(">", Label.CONTENT_TEXT)
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