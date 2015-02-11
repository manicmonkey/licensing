package com.magmanics.licensing.ui.content.product

import scala.beans.BeanInfo

/**
 * A flattening of the ProductOption hierarchy for the UI
 *
 * @author James Baxter - 28/09/2014.
 */
@BeanInfo
case class ProductOptionViewModel(id: Option[Long] = None, var name: String, var default: String, var `type`: String)
