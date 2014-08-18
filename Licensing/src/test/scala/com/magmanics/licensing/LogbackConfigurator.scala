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