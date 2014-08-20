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

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FeatureSpec}
import org.slf4j.LoggerFactory
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.AbstractPlatformTransactionManager

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 15 -Aug-2010
 */
trait TransactionalSpringBasedSuite extends FeatureSpec with BeforeAndAfterAll with BeforeAndAfterEach {

  val logger = LoggerFactory.getLogger(classOf[TransactionalSpringBasedSuite])

//  val ddl = new DDLUnit(CustomerCircumflex, ConfigurationCircumflex, ConfigurationOptionCircumflex, ActivationCircumflex, ActivationInfoCircumflex, ProductCircumflex, TextProductOptionCircumflex, RadioProductOptionCircumflex, ListProductOptionCircumflex, ListProductOptionValueCircumflex, AuditCircumflex)

  def contextLocation: String

  var context: ConfigurableApplicationContext = _
  
  var transactionStatus: TransactionStatus = _

  override protected def beforeAll() {

    LogbackConfigurator.configure()

//    ddl.CREATE()

    try {
      context = new ClassPathXmlApplicationContext(contextLocation)
    } catch {
      case e: Exception => {
//        ddl.DROP()
        throw e
      }
    }
  }

  override protected def afterAll() {
    try {
      context.close
    } catch {
      case e: Exception => logger.error("Error closing context", e)
    }

    try {
//      ddl.DROP()
    } catch {
      case e: Exception => logger.error("Error dropping tables", e)
    }
  }

  override def beforeEach = {
    val txMgr = context.getBean(classOf[AbstractPlatformTransactionManager])
    transactionStatus = txMgr.getTransaction(null)
  }

  override def afterEach = {
    val txMgr = context.getBean(classOf[AbstractPlatformTransactionManager])
    txMgr.rollback(transactionStatus)
  }
}