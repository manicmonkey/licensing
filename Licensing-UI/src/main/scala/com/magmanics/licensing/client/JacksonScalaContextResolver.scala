package com.magmanics.licensing.client

import javax.ws.rs.core.MediaType
import javax.ws.rs.ext.Provider
import javax.ws.rs.{Consumes, Produces}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.jaxrs.cfg.Annotations
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
 * Provider which augments default Jackson serialization with support for Scala objects
 *
 * @author James Baxter - 16/08/2014.
 */
@Provider
@Consumes(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
@Produces(Array(MediaType.APPLICATION_JSON, "application/*+json", "text/json"))
class JacksonScalaContextResolver extends JacksonJaxbJsonProvider(ScalaObjectMapper, Annotations.values())

object ScalaObjectMapper extends ObjectMapper {
  registerModule(DefaultScalaModule)
}
