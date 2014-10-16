package com.magmanics.licensing.endpoint

import com.magmanics.licensing.service.AuthenticationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, RestController}

/**
 * @author James Baxter - 10/10/2014.
 */
@RestController
@RequestMapping(Array("/authentication"))
class AuthenticationEndpoint {

  private val log = LoggerFactory.getLogger(classOf[AuthenticationEndpoint])

  @Autowired
  var authenticationService: AuthenticationService = _

  @RequestMapping(method = Array(RequestMethod.POST))
  def login(@RequestBody credentials: Credentials) = {
    log.debug("User '{}' logging in with password.length {}", credentials.username, credentials.password)
    AuthenticationStatus(authenticationService.login(credentials.username, credentials.password))
  }

  @RequestMapping(method = Array(RequestMethod.GET))
  def loggedIn() = {
    AuthenticationStatus(authenticationService.loggedIn())
  }

  @RequestMapping(method = Array(RequestMethod.DELETE))
  def logout() {
    authenticationService.logout()
  }
}

case class Credentials(username: String, password: String)

case class AuthenticationStatus(authenticated: Boolean)