package com.magmanics.licensing.ui

import com.vaadin.ui.{Label, Component}
import collection.immutable.List
import com.magmanics.licensing.ui.component.{UndefinedWidth, LinkButton}

/**
 * This component is responsible for building a list of components, which when laid out on a canvas, represent a bread crumb trail
 *
 * @author jbaxter - 07/04/11
 */
class MockMessageResource extends MessageResource {
  def getMessage(key: String) = {
    key match {
      case "crumb-fragment-home" => "Home"
      case "crumb-fragment-licence-activation" => "Activate licence"
      case "crumb-fragment-licence-management" => "Licence management"
      case "crumb-fragment-administration" => "Administration"
      case _ => throw new IllegalArgumentException("Unknown key: " + key)
    }
  }
}



