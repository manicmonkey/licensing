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
package com.magmanics.auditing.dao

import com.magmanics.auditing.model.{Audit, AuditCode}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

/**
 * @author James Baxter - 11/07/2014.
 */
@ContextConfiguration(Array("classpath:audit.xml", "classpath:data-layer.xml", "classpath:datasource-test.xml"))
@Transactional
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
class AuditDaoJPATest extends AbstractTestNGSpringContextTests {

  @Autowired
  var auditDao: AuditDao = _

  @Test
  def auditsLargerThan2000CharsTruncated() {
    assertEquals(auditDao.getAuditCodes.size, 0)
    auditDao.create(Audit("username", AuditCode("audit.code"), Range(1, 3000).foldLeft("")((left, int) => left + int)))
    assertEquals(auditDao.getAuditCodes.size, 1)
  }
}
