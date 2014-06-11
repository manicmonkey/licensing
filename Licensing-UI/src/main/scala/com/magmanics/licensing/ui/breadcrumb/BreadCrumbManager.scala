package com.magmanics.licensing.ui.breadcrumb

import com.vaadin.ui.UriFragmentUtility
import com.magmanics.licensing.ui.LicensingApplication
import com.vaadin.ui.UriFragmentUtility._

/**
 * This class is responsible for listening to fragment change events and walking the screen graph to display the relevant view
 *
 * @author jbaxter - 07/04/11
 */
class BreadCrumbListener extends UriFragmentUtility with FragmentChangedListener {

  addListener(this)
  
  def fragmentChanged(source: UriFragmentUtility#FragmentChangedEvent) {
    Console.println("Got fragment: " + getFragment)

    val frags = getCleanFragments(getFragment)
    frags.foldLeft(getApplication.asInstanceOf[CrumbWalkableComponent])((crumbTrail, fragment) => crumbTrail.walkTo(fragment))
    getLicensingApplication.crumbTrail.updateDisplay(frags)
  } //todo unit test

  def getCleanFragments(fragment: String) = {
    val nullSafeFragment = if (fragment == null) "" else fragment
    val cleanedFragment = nullSafeFragment.stripPrefix("/").stripSuffix("/")
    cleanedFragment.split('/').toList
  }

  def getLicensingApplication: LicensingApplication = {
    getApplication.asInstanceOf[LicensingApplication]
  }
}

/**
 * This class is responsible for updating the url fragment displayed in the browser, converting between relative and full paths as required
 *
 * @author jbaxter - 13/04/11
 */
class BreadCrumbFragmentManager extends UriFragmentUtility {

  def walkTo(path: String) {
    if (path.startsWith("/")) {
      //full path
      setFragment(path)
    } else {
      //relative path
      val frag = getFragment
      if (frag == null || frag == "") {
        setFragment(path)
      } else {
        setFragment(frag.stripSuffix("/") + "/" + path)
      }
    }
  }
}