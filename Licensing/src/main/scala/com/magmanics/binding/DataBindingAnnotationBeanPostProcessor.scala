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

package com.magmanics.binding

import org.springframework.beans.factory.config.BeanPostProcessor
/**
 * This class post-processes classes and registers {@link DataBindSubscriber}s and {@link DataBindPublisher}s with the {@link DataBindManager}
 *
 * todo move into higher package.
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 14/02/11
 */
class DataBindingAnnotationBeanPostProcessor extends BeanPostProcessor {

  def postProcessBeforeInitialization(bean: AnyRef, beanName: String) = {
    //todo check for BindAnnotation and pop into DataBindManager (which can deal in weakref of instances)
//    val c: Class = bean.getClass
//    val b: DataBindSubscriber = c.getAnnotation(classOf[Bind])
//    b.value.foreach()
    //todo aspect for @bindable
    //todo magic http://code.google.com/p/spring-custom-annotations/source/browse/trunk/src/main/java/com/npstrandberg/spring/annotation/event/EventAnnotationBeanPostProcessor.java
    bean
  }

  def postProcessAfterInitialization(bean: AnyRef, beanName: String) = bean
}