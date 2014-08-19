package com.magmanics.licensing.client

import javax.ws.rs._
import javax.ws.rs.core.MediaType

import com.magmanics.licensing.model.Product
import org.jboss.resteasy.client.jaxrs.{BasicAuthentication, ResteasyClientBuilder}

/**
 * REST Client for Licensing product endpoint
 *
 * @author James Baxter - 19/08/2014.
 */
@Consumes(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
@Produces(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
trait ProductClient {


  /**
   * Create a Product, returning the persistent Product (id populated)
   */
  @POST
  def create(product: Product): Product

  /**
   * Update the given Product
   */
  @PUT
  def update(product: Product)

  /**
   * Returns enabled Products
   */
  @GET
  @Path("?enabled=true")
  def getEnabled: Seq[Product]

  /**
   * Returns all Products
   */
  @GET
  def get(): Seq[Product]

  /**
   * Return the Product with the given id
   */
  @GET
  def get(@QueryParam("id") id: Long): Option[Product]
}

object ProductClient {
  lazy val client = {
    val client = new ResteasyClientBuilder().build()
      .register(classOf[JacksonScalaContextResolver])
      .register(new BasicAuthentication("admin", "password"), 1)
    val target = client.target("http://localhost:8080/rest/products")
    target.proxy(classOf[ProductClient])
  }
}