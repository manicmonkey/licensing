package com.magmanics.vaadin.spring

import javax.servlet.ServletContext

import org.springframework.web.context.support.WebApplicationContextUtils

/**
 * @author James Baxter - 31/08/2014.
 */
class SpringContextHelper(servletContext: ServletContext) {

  def getContext(servletContext: ServletContext) = {
    WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
  }

  def getBean[T](requiredType: Class[T]): T = {
    getContext(servletContext).getBean(requiredType)
  }
}
