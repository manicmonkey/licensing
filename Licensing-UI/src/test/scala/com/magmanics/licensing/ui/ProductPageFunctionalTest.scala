package com.magmanics.licensing.ui

import com.gargoylesoftware.htmlunit.{DefaultCredentialsProvider, WebClient}
import com.magmanics.licensing.ui.page.ProductPage
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import org.openqa.selenium.remote.RemoteWebDriver

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 25-Nov-2010
 */

//class ProductPageFunctionalTest extends SeleneseTestNgHelper {
class ProductPageFunctionalTest {

//	@Test
  def testUntitled() {
//		selenium.open("/licensing/");
//		selenium.click("//div[@id='contentPanelButton-Products-class com.magmanics.licensing.ui.content.ProductContentPanel']/span/span");
//		verifyTrue(selenium.isTextPresent("Add new product"));
    var profile = new FirefoxProfile
    profile.setPreference("network.http.phishy-userpass-length", 255)
    val driver: RemoteWebDriver = new FirefoxDriver(profile) {
      def modifyWebClient(client: WebClient) = {
        val credentialsProvider = new DefaultCredentialsProvider
        credentialsProvider.addCredentials("admin", "password")
        client.setCredentialsProvider(credentialsProvider)
        client
      }
    }
    val page = new ProductPage(driver).get
    page.textPresent("Add new product")
	}
}
