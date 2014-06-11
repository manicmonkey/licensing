package com.magmanics.licensing

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import ru.circumflex.orm._
import org.scalatest.{FeatureSpec, BeforeAndAfterEach, BeforeAndAfterAll}
import com.magmanics.licensing.datalayer.model._
import org.slf4j.LoggerFactory
import com.magmanics.auditing.model.AuditCircumflex

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 15 -Aug-2010
 */

trait TransactionalSpringBasedSuite extends FeatureSpec with BeforeAndAfterAll with BeforeAndAfterEach {

  val logger = LoggerFactory.getLogger(classOf[TransactionalSpringBasedSuite])

  val ddl = new DDLUnit(CustomerCircumflex, ConfigurationCircumflex, ConfigurationOptionCircumflex, ActivationCircumflex, ActivationInfoCircumflex, ProductCircumflex, TextProductOptionCircumflex, RadioProductOptionCircumflex, ListProductOptionCircumflex, ListProductOptionValueCircumflex, AuditCircumflex)

  def contextLocation: String

  var context: ConfigurableApplicationContext = _

  override protected def beforeAll() {

    LogbackConfigurator.configure

    ddl.create

    try {
      context = new ClassPathXmlApplicationContext(contextLocation)
    } catch {
      case e: Exception => {
        ddl.drop
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
      ddl.drop
    } catch {
      case e: Exception => logger.error("Error dropping tables", e)
    }
  }

  override def beforeEach = tx

  override def afterEach = tx.rollback
}