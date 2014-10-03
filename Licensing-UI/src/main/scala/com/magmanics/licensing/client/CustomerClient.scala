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
  def get(): Set[Customer]

  @GET
  def getEnabled(@QueryParam("enabled") enabled: Boolean): Set[Customer]

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
