package com.magmanics.licensing.ui.content

import com.vaadin.ui.Label

/**
 * @author James Baxter - 07/09/2014.
 */
class CustomerContent extends MainContent {
  addComponent(new Label("Customer content to go here..."))
}

object CustomerContent {
  val name = "customers"
}