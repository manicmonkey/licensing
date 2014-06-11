package com.magmanics.licensing.ui.content.user

import com.vaadin.ui.ComboBox

/**
 * @author jbaxter - 22/06/11
 */
class UserSelectionComboBox extends ComboBox {

  setInputPrompt("Please select a user")

  def setUsers(users: Seq[String]) {
    getContainerDataSource.removeAllItems()
    users.foreach(addItem(_))
  }

  def getUsers() = {
    getValue.asInstanceOf[Set[String]]
  }
}