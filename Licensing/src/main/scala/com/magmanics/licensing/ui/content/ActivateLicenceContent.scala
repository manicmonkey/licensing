package com.magmanics.licensing.ui.content

import com.vaadin.ui._
import com.magmanics.vaadin.component.HtmlLabel

/**
 * @author jbaxter - 06/04/11
 */
class ActivateLicenceContent extends MainContent {
  addComponent(new HtmlLabel("<h3>Please choose from the following options...</h3>"))
  addComponent(new HorizontalLayout {
    addComponent(new HtmlLabel("<ul><li></li></ul>"))
    addComponent(new VerticalLayout {
      addComponent(new HtmlLabel("<h4>Paste the activation code</h4>"))
      addComponent(new HorizontalLayout {
        addComponent(new TextArea())
        val submit = new Button("Submit")
        addComponent(submit)
        setComponentAlignment(submit, Alignment.BOTTOM_CENTER)
        setSpacing(true)
      })
    })
  })
  addComponent(new HorizontalLayout {
    addComponent(new HtmlLabel("<ul><li></li></ul>"))
    addComponent(new VerticalLayout {
      addComponent(new HtmlLabel("<h4>Upload the licence file</h4>"))
      val upload = new Upload(null, null) {
        setImmediate(true)
        setButtonCaption("Select file")
      }
      addComponent(upload)
      setComponentAlignment(upload, Alignment.MIDDLE_RIGHT)
    })
  })
}













