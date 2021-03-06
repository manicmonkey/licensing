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
package com.magmanics.licensing.datalayer.dao

import com.magmanics.licensing.datalayer.dao.exception.DataLayerException
import com.magmanics.licensing.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.testng.annotations.Test

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 12-Jun-2010
 */
@ContextConfiguration(Array("classpath:dao.xml", "classpath:data-layer.xml", "classpath:spring/datasource-test.xml"))
@Transactional
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
class ProductDaoJPATest extends AbstractTransactionalTestNGSpringContextTests {

  @Autowired
  var productDao: ProductDao = _
  
  @Test
  def saveProduct {
      productDao.create(product)
  }

  @Test(expectedExceptions = Array(classOf[DataLayerException]))
  def cannotSaveProductWithoutName {
    productDao.create(new Product(name = null))
  }

  @Test(expectedExceptions = Array(classOf[DataLayerException]))
  def cannotSaveProductsWithSameName {
      productDao.create(product)
      productDao.create(product)
  }

  //todo test for getOptions

  def product = Product(name = "PDM Client", description = "PDM Client for OCR and Workflow")
}