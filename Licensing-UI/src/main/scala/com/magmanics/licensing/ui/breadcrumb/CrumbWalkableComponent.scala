package com.magmanics.licensing.ui.breadcrumb

/**
 * This trait represents a component which forms part of a crumb trail
 *
 * @author jbaxter - 11/04/11
 */
trait CrumbWalkableComponent {

  def walkTo(path: String): CrumbWalkableComponent
}