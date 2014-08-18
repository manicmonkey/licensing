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