package com.magmanics.licensing.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import scala.beans.BeanInfo

/**
 * @author James Baxter - 05/09/2014.
 */
@BeanInfo
case class User(id: Long, name: String, @JsonDeserialize(contentAs=classOf[java.lang.Long]) customers: Set[Long], permissions: Set[String])
