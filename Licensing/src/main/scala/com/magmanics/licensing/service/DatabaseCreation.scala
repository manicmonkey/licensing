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

package com.magmanics.licensing.service

import ru.circumflex.orm.DDLUnit
import com.magmanics.licensing.datalayer.model._
import org.slf4j.LoggerFactory
import com.magmanics.auditing.model.AuditCircumflex

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 08-Jun-2010
 * Time: 15:27:59
 * To change this template use File | Settings | File Templates.
 */

object DatabaseCreation {

  val log = LoggerFactory.getLogger("DatabaseCreation")

  def create {
    val ddl = new DDLUnit(CustomerCircumflex, ConfigurationCircumflex, ConfigurationOptionCircumflex, ActivationCircumflex, ActivationInfoCircumflex, ProductCircumflex, TextProductOptionCircumflex, RadioProductOptionCircumflex, ListProductOptionCircumflex, ListProductOptionValueCircumflex, AuditCircumflex)

    ddl.CREATE()

    for (message <- ddl.messages) {
      log.info(message.toString)
    }
  }
}