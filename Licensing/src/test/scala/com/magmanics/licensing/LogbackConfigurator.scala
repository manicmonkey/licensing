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
package com.magmanics.licensing

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.core.joran.spi.JoranException
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.{ILoggerFactory, LoggerFactory}

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 19 -Sep-2010
 */
object LogbackConfigurator {

  val logger = LoggerFactory.getLogger("LogbackConfigurator")

  def configure() {

    val loggerFactory: ILoggerFactory = LoggerFactory.getILoggerFactory

    loggerFactory match {
      case loggerContext: LoggerContext =>
        loggerContext.reset()

        val configurator = new JoranConfigurator()
        configurator.setContext(loggerContext)
        val configUrl = getClass.getResource("/logback-test.xml")

        try {
          configurator.doConfigure(configUrl)
          logger.debug("Configured logging with {}.", configUrl)
          StatusPrinter.print(loggerContext)
        } catch {
          case ex: JoranException =>
            logger.error("Error configuring logging framework!", ex)
            StatusPrinter.print(loggerContext)
        }
      case _ =>
    }
  }
}