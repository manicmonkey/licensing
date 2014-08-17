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

import com.magmanics.licensing.ui.breadcrumb.CrumbWalkableComponent
import com.vaadin.ui._

/**
 * This class represents the main content container within the application. Implementations of MainContent are loaded into it.
 *
 * @author jbaxter - 06/04/11
 */
class MainContentContainer extends Panel with CrumbWalkableComponent {

  setHeight(null) //shrink height to fit content
  setWidth("100%") //stretch to fill screen
//  setSizeFull()
  setId("MainContentContainerId")

  var currentContent: MainContent = _

  def walkTo(path: String) = {
    val newContent = path match {
      case "licence-activation" => activateLicence
      case "licence-management" => licenceManagement
      case "administration" => administration
      case _ => homeContent
    }

    if (newContent != currentContent) {
      currentContent = newContent
      updateContent(newContent)
      newContent
    } else {
      currentContent
    }
  }

  def updateContent(mainContent: MainContent) {
    setContent(mainContent)
  }

  lazy val homeContent = new HomeContent
  lazy val licenceManagement = new LicenceManagementContent
  lazy val activateLicence = new ActivateLicenceContent
  lazy val administration = new AdministrationContent
}