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

package com.magmanics.auditing.model

import java.util.Date

import scala.beans.BeanInfo

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 18-Sep-2010
 */
//return-type class method arg-types return-obj arg-objs
// ^ ties too much to method invocation :/ maybe only intercept methods annotated with @Audit
// and use AuditCodes to represent message text

// examples...
// create activation, failed activation,
// create configuration, modify configuration, read configuration
// create product, modify product (need default values for past conf?)
// create customer, modify customer

@BeanInfo //generate JavaBean style accessors for ui binding
case class Audit(username: String, auditCode: AuditCode, auditMessage: String, created: Date = new Date())

case class AuditCode(value: String)