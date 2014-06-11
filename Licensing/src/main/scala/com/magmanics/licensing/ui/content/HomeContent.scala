package com.magmanics.licensing.ui.content

import com.vaadin.ui._
import com.magmanics.licensing.ui.CrumbTrailMessageResource
import com.magmanics.vaadin.component.{LinkButton, UndefinedWidth, HtmlLabel}

/**
 * @author jbaxter - 06/04/11
 */
class HomeContent extends MainContent {

  val messageResource = new CrumbTrailMessageResource

  setContent(new HorizontalLayout {
    setWidth("100%")
  })
  addComponent(new HorizontalLayout {
    addComponent(new HtmlLabel("Please select an option...") with UndefinedWidth)
    List("licence-activation", "licence-management", "administration").foreach(l => addComponent(new LinkButton(messageResource.getMessage(l), l)))
    setSpacing(true)
    setMargin(true)
  })
}













