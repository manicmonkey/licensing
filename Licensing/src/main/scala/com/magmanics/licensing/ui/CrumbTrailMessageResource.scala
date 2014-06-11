package com.magmanics.licensing.ui

/**
 * @author jbaxter - 13/04/11
 */
class CrumbTrailMessageResource extends MessageResource {
  val underlyingMessageResource = new MockMessageResource
  def getMessage(key: String) = {
    underlyingMessageResource.getMessage("crumb-fragment-" + key)
  }
}