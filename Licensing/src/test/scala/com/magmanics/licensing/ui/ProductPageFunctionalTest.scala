package com.magmanics.licensing.ui

import org.testng.annotations.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import page.ProductPage
import org.apache.http.client.CredentialsProvider
import org.apache.http.auth.{UsernamePasswordCredentials, Credentials}
import com.gargoylesoftware.htmlunit.{DefaultCredentialsProvider, WebClient}
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.firefox.{FirefoxProfile, FirefoxDriver}

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
