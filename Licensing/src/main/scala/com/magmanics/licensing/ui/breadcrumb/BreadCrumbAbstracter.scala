package com.magmanics.licensing.ui.breadcrumb

import collection.mutable.ListBuffer
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