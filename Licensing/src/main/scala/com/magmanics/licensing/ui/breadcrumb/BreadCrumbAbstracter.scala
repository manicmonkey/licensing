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

import scala.collection.mutable.ListBuffer
/**
 * @author jbaxter - 16/04/11
 */

class BreadCrumbAbstracter {

  def build(frags: List[String]): Seq[CrumbComponent] = {
    val links = getLinks(frags)
    val labels = getLabels(frags)
    val linksAndLabels = joinLinksAndLabels(links, labels)
    suffixCurrent(linksAndLabels, frags.last)
//    val linksAndLabels = joinLinksAndLabels(links, labels)
//    linksAndLabels.length match {
//      case 0 => frags.last
//    }
  }

  def getLinks(frags: List[String]): Seq[String] = {
    val links = ListBuffer[String]()

    for (l <- frags) {
      links += frags.takeWhile(_ != l).foldLeft("/")((left, right) => left match {
        case "/" => left + right
        case _ => left + "/" + right
      })
    }
    links
  }

  def getLabels(frags: List[String]) = ("home" :: frags).map(_.capitalize)

  def joinLinksAndLabels(links: Seq[String], labels: List[String]): List[CrumbComponent] = {
    val zipped: List[CrumbLink] = labels.zip(links).map(l => CrumbLink(l._1, l._2))
    zipped.head :: zipped.tail.map(l => List(CrumbDivider(), l)).flatMap(l => l)
  }

  def suffixCurrent(crumbs: List[CrumbComponent], current: String) = {
    crumbs.length match {
      case 0 => crumbs :+ CrumbLabel(current)
      case _ => crumbs :+ CrumbDivider() :+ CrumbLabel(current)
    }
  }
}