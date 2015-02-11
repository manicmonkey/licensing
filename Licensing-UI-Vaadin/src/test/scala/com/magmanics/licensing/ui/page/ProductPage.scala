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
package com.magmanics.licensing.ui.page

import java.util.concurrent.TimeUnit

import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.LoadableComponent
import org.openqa.selenium.{By, WebElement}
import org.testng.Assert

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