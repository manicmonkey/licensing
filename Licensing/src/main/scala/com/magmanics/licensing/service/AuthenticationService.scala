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

package com.magmanics.licensing.service

import com.magmanics.auditing.Auditable
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.{AuthenticationManager, BadCredentialsException, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

import scala.collection.JavaConversions._

/**
 * Responsible for interfacing with the underlying authentication provider, and determining whether a user has adequate credentials
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 28-Sep-2010
 */
trait AuthenticationService {
  /**
   * Attempt application login
   * @return whether the user authenticated with acceptable credentials and authorities
   */
  def login(name: String, password: String): Boolean
}

class AuthenticationServiceImpl(authenticationManager: AuthenticationManager) extends AuthenticationService {

  private val log = LoggerFactory.getLogger(classOf[AuthenticationService])

  /**
   * Attempt application login
   * @return whether the user authenticated with acceptable credentials and authorities
   */
  @Auditable("audit.application.login")
  override def login(name: String, password: String): Boolean = {

    log.debug("User '{}' logging in", name)

    val request: Authentication = new UsernamePasswordAuthenticationToken(name, password)
    try {
      val result: Authentication = authenticationManager.authenticate(request)
      if (result.getAuthorities.exists(p => p.getAuthority == "ROLE_USER")) {
        SecurityContextHolder.getContext().setAuthentication(result)
        return true
      }
    } catch {
      case e: BadCredentialsException =>
    }
    false
    //spring security configured to allow anonymous access so authentication exceptions are not expected
  }
}