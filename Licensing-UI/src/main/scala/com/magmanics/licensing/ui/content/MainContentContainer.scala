package com.magmanics.licensing.ui.content

import com.vaadin.Application
import com.vaadin.ui._
import scala.collection.JavaConversions._
import com.magmanics.licensing.ui.breadcrumb.CrumbWalkableComponent

/**
 * This class represents the main content container within the application. Implementations of MainContent are loaded into it.
 *
 * @author jbaxter - 06/04/11
 */
class MainContentContainer extends HorizontalLayout with CrumbWalkableComponent {

  setHeight(null) //shrink height to fit content
  setWidth("100%") //stretch to fill screen
//  setSizeFull()
  setDebugId("MainContentContainerId")

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
    removeAllComponents()
    addComponent(mainContent)
  }

  lazy val homeContent = new HomeContent
  lazy val licenceManagement = new LicenceManagementContent
  lazy val activateLicence = new ActivateLicenceContent
  lazy val administration = new AdministrationContent
}