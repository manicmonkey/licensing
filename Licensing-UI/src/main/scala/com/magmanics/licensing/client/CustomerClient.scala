package com.magmanics.licensing.client

import javax.ws.rs._
import javax.ws.rs.core.MediaType

import com.magmanics.licensing.model.Customer
import org.jboss.resteasy.client.jaxrs.{BasicAuthentication, ResteasyClientBuilder}

/**
 * REST Client for Licensing customer endpoint
 *
 * @author James Baxter - 16/08/2014.
 */
@Consumes(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
@Produces(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
trait CustomerClient {

  @POST
  def create(customer: Customer): Customer

  @GET
  def get(): Seq[Customer]

  @GET
  @Path("?enabled=true")
  def getEnabled: Seq[Customer]

  @GET
  def get(@QueryParam("id") id: Long): Customer

  @PUT
  def update(customer: Customer)
}

object CustomerClient {
  lazy val client = {
    val client = new ResteasyClientBuilder().build()
      .register(classOf[JacksonScalaContextResolver])
      .register(new BasicAuthentication("admin", "password"), 1)
    val target = client.target("http://localhost:8080/rest/customers")
    target.proxy(classOf[CustomerClient])
  }
}
