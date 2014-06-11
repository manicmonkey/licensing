package com.magmanics.licensing.ui.content

import com.magmanics.licensing.ui.component.LinkButton

/**
 * @author jbaxter - 13/04/11
 */

class AdministrationContent extends MainContent {
  addComponent(new LinkButton("Magic", "magic"))

  override def walkTo(path: String) = {
    Console.println("walking to: " + path)
    null
  }
}