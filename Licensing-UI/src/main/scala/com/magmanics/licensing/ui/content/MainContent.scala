package com.magmanics.licensing.ui.content

import com.vaadin.Application
import com.vaadin.ui._
import scala.collection.JavaConversions._
import com.magmanics.licensing.ui.breadcrumb.CrumbWalkableComponent

/**
 * @author jbaxter - 06/04/11
 */
class MainContent extends Panel with CrumbWalkableComponent {
  setContent(new VerticalLayout {
    setWidth(null)
    setSpacing(true)
    setMargin(true)
  })
  setWidth("100%")
  setHeight(null)

  def walkTo(path: String) = null
}