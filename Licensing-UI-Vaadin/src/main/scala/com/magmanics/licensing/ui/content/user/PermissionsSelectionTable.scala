package com.magmanics.licensing.ui.content.user

import com.magmanics.vaadin.component.TableWithCheckboxes

import scala.collection.JavaConverters._

/**
 * @author James Baxter - 05/09/2014.
 */
class PermissionsSelectionTable extends TableWithCheckboxes {

  setSelectable(true)
  setMultiSelect(true)
  setImmediate(true)
  setPageLength(5)

  override def containerProperties = List(("permission", classOf[String], "", "Permission", null, null))

  override def itemRows = Seq("login", "create_configuration", "update_activation_limit", "create_customer", "create_product").map(p => Array(p) -> p)

  def setPermissions(permissions: Set[String]) = setValue(permissions.asJava)
}
