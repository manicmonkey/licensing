package com.magmanics.licensing.endpoint

import com.magmanics.licensing.model.User
import org.springframework.web.bind.annotation.{RequestBody, RequestMethod, RequestMapping, RestController}

/**
 * @author James Baxter - 03/09/2014.
 */
@RestController
@RequestMapping(Array("/users"))
class UserEndpoint {

  val permissions = Set("login", "create_configuration", "update_activation_limit", "create_customer", "create_product")
  val users = Seq(
    User(1, "admin", Set(1, 2), permissions),
    User(2, "daffy_duck", Set(1, 2, 3, 4, 5, 6, 7), permissions),
    User(3, "old_mac_donald", Set(1, 3, 4, 5), permissions.dropRight(2)),
    User(4, "santa", Set(), Set("login", "update_activation_limit")))

  @RequestMapping(method = Array(RequestMethod.GET))
  def getUsers = users

  @RequestMapping(method = Array(RequestMethod.PUT))
  def updateUser(@RequestBody user: User) = {}
}
