package com.magmanics.licensing.ui

/**
 * This component is responsible for building a list of components, which when laid out on a canvas, represent a bread crumb trail
 *
 * @author jbaxter - 07/04/11
 */
trait MessageResource {
  def getMessage(key: String): String
}



