package com.magmanics.licensing.client

import org.jboss.resteasy.client.jaxrs.{BasicAuthentication, ResteasyClientBuilder}

/**
 * @author James - 08/02/2015.
 */
class ClientFactory {

  def get[T](subResource: String)(implicit m: Manifest[T]): T = {
    val host = System.getenv("SERVER_PORT_8080_TCP_ADDR")
    val port = System.getenv("SERVER_PORT_8080_TCP_PORT")

    val client = new ResteasyClientBuilder().build()
      .register(classOf[JacksonScalaContextResolver])
      .register(new BasicAuthentication("admin", "password"), 1)
    val baseUrl = "http://" + host + ":" + port + "/rest/"
    Console.println("Got baseUrl: " + baseUrl)
    val target = client.target(baseUrl + subResource)
    target.proxy(m.runtimeClass.asInstanceOf[Class[T]])
  }
}

object ClientFactory {
  
  def getAuditClient = {
    new ClientFactory().get[AuditClient]("audits")
  }
  
  def getConfigurationClient = {
    new ClientFactory().get[ConfigurationClient]("configurations")
  }
  
  def getCustomerClient = {
    new ClientFactory().get[CustomerClient]("customers")
  }
  
  def getProductClient = {
    new ClientFactory().get[ProductClient]("products")
  }
  
  def getUserClient = {
    new ClientFactory().get[UserClient]("users")
  }
}