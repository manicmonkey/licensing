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

package com.magmanics.vaadin.spring

import org.springframework.context.NoSuchMessageException
import org.springframework.web.context.support.WebApplicationObjectSupport

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 01-Oct-2010
 */
class VaadinApplicationObjectSupport extends WebApplicationObjectSupport {

  def getMessage(key: String): String = {
    try {
      getMessageSourceAccessor.getMessage(key)
    } catch {
      case e: NoSuchMessageException => key + "???"
    }
  }

  def getMessage(key: String, args: Array[java.lang.Object]): String = {
    getMessageSourceAccessor.getMessage(key, args)
  }
}