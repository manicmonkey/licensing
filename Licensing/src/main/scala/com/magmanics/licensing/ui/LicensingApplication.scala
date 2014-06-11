package com.magmanics.licensing.ui

import breadcrumb._
import com.vaadin.Application
import com.vaadin.ui._
import content._
import com.magmanics.vaadin.spring.VaadinComponent
import com.magmanics.vaadin.component.HtmlLabel
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
//    container.updateContent(new LicenceManagementContent)
    container.updateContent(auditLogContent)
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