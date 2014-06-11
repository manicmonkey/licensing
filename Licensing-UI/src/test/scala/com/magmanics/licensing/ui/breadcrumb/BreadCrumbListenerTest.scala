package com.magmanics.licensing.ui.breadcrumb

import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author jbaxter - 14/04/11
 */

class BreadCrumbListenerTest {
  @Test
  def cleanNullFragment() {
    val frags = new BreadCrumbListener().getCleanFragments(null)
    assertEquals(frags.size, 1)
    assertEquals(frags(0), "")
  }

  @Test
  def cleanEmptyFragment() {
    val frags = new BreadCrumbListener().getCleanFragments("")
    assertEquals(frags.size, 1)
    assertEquals(frags(0), "")
  }

  @Test
  def cleanSingleFragment() {
    val frags = new BreadCrumbListener().getCleanFragments("single")
    assertEquals(frags.size, 1)
    assertEquals(frags(0), "single")
  }

  @Test
  def cleanMultipleFragment() {
    val frags = new BreadCrumbListener().getCleanFragments("single/multiple")
    assertEquals(frags.size, 2)
    assertEquals(frags(0), "single")
    assertEquals(frags(1), "multiple")
  }

  @Test
  def cleanMultipleFragmentPrefix() {
    val frags = new BreadCrumbListener().getCleanFragments("/single/multiple")
    assertEquals(frags.size, 2)
    assertEquals(frags(0), "single")
    assertEquals(frags(1), "multiple")
  }

  @Test
  def cleanMultipleFragmentSuffix() {
    val frags = new BreadCrumbListener().getCleanFragments("single/multiple/")
    assertEquals(frags.size, 2)
    assertEquals(frags(0), "single")
    assertEquals(frags(1), "multiple")
  }
}