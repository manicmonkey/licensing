package com.magmanics.licensing.ui

import com.vaadin.ui.{Label, Component}
import collection.immutable.List
import com.magmanics.licensing.ui.component.{UndefinedWidth, LinkButton}

/**
 * This component is responsible for building a list of components, which when laid out on a canvas, represent a bread crumb trail
 *
 * @author jbaxter - 07/04/11
 */
trait MessageResource {
  def getMessage(key: String): String
}



