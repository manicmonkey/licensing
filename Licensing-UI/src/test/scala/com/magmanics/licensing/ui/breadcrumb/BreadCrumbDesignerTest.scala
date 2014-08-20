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

import com.magmanics.licensing.ui.MessageResource
import com.magmanics.vaadin.component.LinkButton
import com.vaadin.data.util.ObjectProperty
import com.vaadin.ui.{Component, Label}
import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author jbaxter - 13/04/11
 */

class BreadCrumbDesignerTest {

  val mockMessageResource = new MessageResource {
    def getMessage(key: String) = key //just return the key
  }

  @Test(enabled = false)
  def worksWithNoItems() {
    val crumbs = new BreadCrumbDesigner(mockMessageResource).build(List())
    assertEquals(crumbs.size, 1)
    val crumb = crumbs(0)
    assertEquals(getLabelCaption(crumb), "home")
  }

  @Test(enabled = false)
  def worksWithOneItem() {
    val crumbs = new BreadCrumbDesigner(mockMessageResource).build(List("admin"))
    assertEquals(crumbs.size, 3)
    val crumb0 = crumbs(0)
    val crumb1 = crumbs(1)
    val crumb2 = crumbs(2)
    assertEquals(getLinkCaption(crumb0), "home")
    assertEquals(getLabelCaption(crumb1), ">")
    assertEquals(getLabelCaption(crumb2), "admin")
  }

  @Test(enabled = false)
  def worksWithTwoItems() {
    val crumbs = new BreadCrumbDesigner(mockMessageResource).build(List("admin", "users"))
    assertEquals(crumbs.size, 5)
    val crumb0 = crumbs(0)
    val crumb1 = crumbs(1)
    val crumb2 = crumbs(2)
    val crumb3 = crumbs(3)
    val crumb4 = crumbs(4)

    //check display
    assertEquals(getLinkCaption(crumb0), "home")
    assertEquals(getLabelCaption(crumb1), ">")
    assertEquals(getLinkCaption(crumb2), "admin")
    assertEquals(getLabelCaption(crumb3), ">")
    assertEquals(getLabelCaption(crumb4), "users")

    //check path
    assertEquals(getLinkPath(crumb0), "/")
    assertEquals(getLinkPath(crumb2), "/admin")
  }

  private def getLinkCaption(crumb: Component): String = {
    crumb.asInstanceOf[LinkButton].caption
  }

  private def getLinkPath(crumb: Component): String = {
    crumb.asInstanceOf[LinkButton].path
  }

  private def getLabelCaption(crumb: Component): String = {
    crumb.asInstanceOf[Label].getPropertyDataSource.asInstanceOf[ObjectProperty[String]].getValue
  }
}