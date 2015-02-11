package com.magmanics.licensing.client

import javax.ws.rs.{POST, GET, Produces, Consumes}
import javax.ws.rs.core.MediaType

import com.magmanics.licensing.model.User
import org.jboss.resteasy.client.jaxrs.{BasicAuthentication, ResteasyClientBuilder}

/**
 * @author James Baxter - 05/09/2014.
 */
@Consumes(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
@Produces(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
trait UserClient {

  @GET
  def getUsers: Seq[User]

  @POST
  def updateUser(user: User)
}