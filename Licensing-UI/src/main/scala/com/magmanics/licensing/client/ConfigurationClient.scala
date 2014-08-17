package com.magmanics.licensing.client

import javax.ws.rs._
import javax.ws.rs.core.MediaType

import com.magmanics.licensing.service.model.Configuration
import org.jboss.resteasy.client.jaxrs.{BasicAuthentication, ResteasyClientBuilder}

/**
 * REST Client for Licensing configuration endpoint
 *
 * @author James Baxter - 16/08/2014.
 */
@Consumes(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
@Produces(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
trait ConfigurationClient {

  /**
   * Create a configuration
   * @return the persistent configuration (ie includes an id).
   */
  @POST
  def create(configuration: Configuration): Configuration

  /**
   * Updates a configuration
   */
  @PUT
  def update(configuration: Configuration)

  /**
   * Lookup a Configuration by its id
   * @throws NoSuchEntityException If a configuration with the given id cannot be found
   */
  @GET
  @Path("/id/{id}")
  def get(@PathParam("id") id: Long): Configuration

  /**
   * Get configurations for a particular customer. Returns an empty list if none are found.
   */
  @GET
  @Path("/customer/{customer}")
  def getByCustomer(@PathParam("customer") customer: String): Seq[Configuration]

  /**
   * Try to get a configuration given a serial.
   */
  @GET
  @Path("/serial/{serial}")
  def getBySerial(@PathParam("serial") serial: String): Option[Configuration]
}

object ConfigurationClient {
  lazy val client = {
    val client = new ResteasyClientBuilder().build()
      .register(classOf[JacksonScalaContextResolver])
      .register(new BasicAuthentication("admin", "password"), 1)
    val target = client.target("http://localhost:8080/rest/configurations")
    target.proxy(classOf[ConfigurationClient])
  }
}