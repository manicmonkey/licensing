package com.magmanics.vaadin.component

import com.magmanics.vaadin.ClickHandler
import com.vaadin.navigator.Navigator
import com.vaadin.ui.Button

/**
 * @author James Baxter - 05/09/2014.
 */
class LinkButton(text: String, target: String, navigator: Navigator) extends Button(text) {
  addClickListener(new ClickHandler(_ =>  navigator.navigateTo(target)))
}
