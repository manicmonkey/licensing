package com.magmanics.auditing.dao

import com.magmanics.auditing.model.{Audit, AuditCode}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.testng.annotations.Test

/**
 * Created by James on 11/07/2014.
 */
@ContextConfiguration(Array("classpath:audit.xml", "classpath:data-layer.xml", "classpath:datasource-test.xml"))
@Transactional
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
class AuditDaoJPATest extends AbstractTestNGSpringContextTests {

  @Autowired
  var auditDao: AuditDao = _

  @Test
  def auditsLargerThan2000CharsTruncated() {
    auditDao.create(Audit("username", AuditCode("audit.code"), Range(1, 3000).foldLeft("")((left, int) => left + int)))
  }
}
