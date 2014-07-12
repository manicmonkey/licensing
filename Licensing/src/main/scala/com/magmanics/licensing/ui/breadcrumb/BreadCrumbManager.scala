/**
 * Magmanics Licensing. This web application allows for centralized control
 * of client application activation, with optional configuration parameters
 * to control licensable features, and storage of supplementary information
 * about the client machine. Client applications may interface with this
 * central server (for activation) using libraries licenced under an
 * alternative licence.
 *
 * Copyright (C) 2010 James Baxter <j.w.baxter(at)gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmanics.licensing.ui.breadcrumb

import com.magmanics.licensing.ui.LicensingApplication
import com.vaadin.ui.UriFragmentUtility
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