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

package com.magmanics.vaadin

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 22 -Jun-2010
 */

trait ModelViewer[A] {
  def display(model: Option[A])
}

trait Observer[T] {
  def receiveUpdate(value: Option[T])
}

//trait Observable[S] {
//  this: S =>
//  private var observers: List[Observer[S]] = Nil
//
//  def addObserver(observer: Observer[S]) = observers = observer :: observers
//
//  def notifyObservers(obj: S) = observers.foreach(_.receiveUpdate(obj))
//}

trait ObservableValueChangeListener[T] extends Property.ValueChangeListener {

//  this: T =>
  private var observers: List[Observer[T]] = Nil

  def addObserver(observer: Observer[T]) = observers = observer :: observers

  def notifyObservers(value: Option[T]) = observers.foreach(_.receiveUpdate(value))

  /* to be implemented by subclasses - should return the currently selected value */
  def getValue: Any

  override def valueChange(event: ValueChangeEvent) {
    val value = getValue
    value match {
      case null => notifyObservers(None)
      case _ => notifyObservers(Some(value.asInstanceOf[T]))
    }
//    notifyObservers(getValue.asInstanceOf[T])
  }
}
