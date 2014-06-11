package com.magmanics.vaadin.component

import com.vaadin.ui.Button
import com.vaadin.ui.themes.BaseTheme
import com.magmanics.licensing.ui.LicensingApplication

/**
 * A button with link styling which triggers a fragment update when clicked
 *
 * @author jbaxter - 06/04/11
 */
class LinkButton(val caption: String, val path: String) extends Button(caption) {
  setStyleName(BaseTheme.BUTTON_LINK)
  addListener(new Button.ClickListener {
    def buttonClick(event: Button#ClickEvent) {
      getApplication.asInstanceOf[LicensingApplication].breadCrumbFragmentManager.walkTo(path)
    }
  })
}