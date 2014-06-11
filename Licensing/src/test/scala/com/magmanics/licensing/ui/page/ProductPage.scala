package com.magmanics.licensing.ui.page

import org.testng.Assert
import org.openqa.selenium.{By, WebElement, WebDriver}
import org.openqa.selenium.support.ui.{WebDriverWait, LoadableComponent}
import java.util.concurrent.TimeUnit
import org.openqa.selenium.remote.RemoteWebDriver

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 28-Nov-2010
 */

class ProductPage(driver: RemoteWebDriver) extends LoadableComponent[ProductPage] {

  var MENU_PRODUCTS: WebElement = null

  override def load {
    driver.get("http://admin:password@localhost:8080/licensing")
    driver.get("http://admin:password@localhost:8080/licensing")
//    MENU_PRODUCTS.click
  }

  override def isLoaded {
    val p = driver.getCurrentUrl
    Assert.assertTrue(driver.getCurrentUrl.endsWith("licensing/"))
//    Assert.assertNotNull(MENU_PRODUCTS)
    Assert.assertNotNull(p)
  }

//  def getNewProductButton
  def textPresent(text: String) {
//    Assert.assertNotNull(MENU_PRODUCTS)
    val source = driver.getPageSource
//    val wait = new WebDriverWait(driver, 5)
//    wait.until()
    val c = driver.getCommandExecutor
    driver.manage.timeouts.implicitlyWait(30, TimeUnit.SECONDS)
    driver.findElement(By.xpath("//div[@id='MENU_PRODUCTS']")).click
//    driver.findElement(By.xpath("//div[@id='MENU_PRODUCTS']/span/span")).click
    Assert.assertTrue(driver.findElement(By.id("CreateProductButton")).getText.equalsIgnoreCase(text))
  }
}