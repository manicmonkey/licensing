package com.magmanics.licensing.client

import javax.ws.rs._
import javax.ws.rs.core.MediaType

import com.magmanics.auditing.model.{AuditCode, Audit}
import com.magmanics.auditing.service.AuditSearchDto
import org.jboss.resteasy.client.jaxrs.{BasicAuthentication, ResteasyClientBuilder}

/**
 * REST Client for Licensing audit endpoint
 *
 * @author James Baxter - 27/08/2014.
 */
@Consumes(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
@Produces(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
trait AuditClient {

  @POST
  def create(audit: Audit)

  /**
   * @return a list of the distinct usernames for which there are audit entries
   */
  @GET
  @Path("/usernames")
  def getUsernames: Seq[String]

  /**
   * @return a sequence of the distinct audit codes for which there are audit entries
   */
  @GET
  @Path("/auditcodes")
  def getAuditCodes: Seq[AuditCode]

  /**
   * @return a sequence of [[com.magmanics.auditing.model.Audit Audits]] which conform to the given search criteria
   */
  @GET
  @Path("/messages")
  def getAuditMessages(auditSearch: AuditSearchDto): Seq[Audit]
}

object AuditClient {
  lazy val client = {
    val client = new ResteasyClientBuilder().build()
      .register(classOf[JacksonScalaContextResolver])
      .register(new BasicAuthentication("admin", "password"), 1)
    val target = client.target("http://localhost:8080/rest/audits")
    target.proxy(classOf[AuditClient])
  }
}