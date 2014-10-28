package com.magmanics.licensing

import com.magmanics.licensing.service.AuthenticationService
import org.scalatest.{FeatureSpec, BeforeAndAfterAll}
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder

/**
 * @author James Baxter - 19/10/2014.
 */
trait LoggedInUser extends FeatureSpec with BeforeAndAfterAll {

  def context: ConfigurableApplicationContext

  var authenticationService: AuthenticationService = _

  override protected def beforeAll() {
    super.beforeAll()
    authenticationService = context.getBean(classOf[AuthenticationService])

    //access over http given anonymous login by default and security expressions rely on this
    SecurityContextHolder.getContext.setAuthentication(new AnonymousAuthenticationToken(classOf[LoggedInUser].getName, "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")))
    authenticationService.login("admin", "password")
  }

  override protected def afterAll() {
    authenticationService.logout()
    super.afterAll()
  }
}
