package com.magmanics.licensing.ui.content.product

import com.magmanics.licensing.model._
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.AbstractSelect.ItemCaptionMode
import com.vaadin.ui.Table

import scala.beans.BeanInfo

/**
 * @author James Baxter - 23/09/2014.
 */
class ProductOptionsTable extends Table {

  setSelectable(true)
  //  setMultiSelect(true)
  setImmediate(true)
  setPageLength(5)

  val container = new BeanItemContainer[ProductOptionViewModel](classOf[ProductOptionViewModel])
  setContainerDataSource(container)

  setItemCaptionMode(ItemCaptionMode.PROPERTY)
  setVisibleColumns("name", "type", "default")
  setColumnHeaders("Name", "Type", "Default")

  setSelectable(true)
  setNullSelectionAllowed(true)

  def setProductOptions(productOptions: Set[ProductOption[_ <: Any]]) = {
    container.removeAllItems()
    productOptions.toList.sortBy(_.name).map {
      case t: TextOption => ProductOptionViewModel(t.id, t.name, t.default, "Text")
      case b: BoolOption => ProductOptionViewModel(b.id, b.name, String.valueOf(b.default), "Radio")
      case l: ListOption => ProductOptionViewModel(l.id, l.name, l.default, "List")
    }.foreach(container.addBean)
  }

  def getSelected: ProductOptionViewModel = {
    getValue.asInstanceOf[ProductOptionViewModel]
  }
}