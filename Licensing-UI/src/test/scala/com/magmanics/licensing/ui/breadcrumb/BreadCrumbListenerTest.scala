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

import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author jbaxter - 14/04/11
 */

class BreadCrumbListenerTest {
  @Test(enabled = false)
  def cleanNullFragment() {
    val frags = new BreadCrumbListener().getCleanFragments(null)
    assertEquals(frags.size, 1)
    assertEquals(frags(0), "")
  }

  @Test(enabled = false)
  def cleanEmptyFragment() {
    val frags = new BreadCrumbListener().getCleanFragments("")
    assertEquals(frags.size, 1)
    assertEquals(frags(0), "")
  }

  @Test(enabled = false)
  def cleanSingleFragment() {
    val frags = new BreadCrumbListener().getCleanFragments("single")
    assertEquals(frags.size, 1)
    assertEquals(frags(0), "single")
  }

  @Test(enabled = false)
  def cleanMultipleFragment() {
    val frags = new BreadCrumbListener().getCleanFragments("single/multiple")
    assertEquals(frags.size, 2)
    assertEquals(frags(0), "single")
    assertEquals(frags(1), "multiple")
  }

  @Test(enabled = false)
  def cleanMultipleFragmentPrefix() {
    val frags = new BreadCrumbListener().getCleanFragments("/single/multiple")
    assertEquals(frags.size, 2)
    assertEquals(frags(0), "single")
    assertEquals(frags(1), "multiple")
  }

  @Test(enabled = false)
  def cleanMultipleFragmentSuffix() {
    val frags = new BreadCrumbListener().getCleanFragments("single/multiple/")
    assertEquals(frags.size, 2)
    assertEquals(frags(0), "single")
    assertEquals(frags(1), "multiple")
  }
}