package com.magmanics.licensing.ui.content

import com.vaadin.ui.Label

/**
 * @author James Baxter - 07/09/2014.
 */
class ProductContent extends MainContent {
  addComponent(new Label("Product content to go here..."))
}

object ProductContent {
  val name = "products"
}
