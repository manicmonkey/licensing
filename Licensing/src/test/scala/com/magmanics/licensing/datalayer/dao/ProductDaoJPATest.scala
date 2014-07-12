package com.magmanics.licensing.datalayer.dao

import com.magmanics.licensing.datalayer.dao.exception.DataLayerException
import com.magmanics.licensing.service.model.Product
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