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