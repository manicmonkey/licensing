package com.magmanics.vaadin.spring

import javax.servlet.ServletContext

import org.springframework.web.context.support.WebApplicationContextUtils

/**
 * Helper class to get Spring Beans via ServletContext
 *
 * @author James Baxter - 31/08/2014.
 */
class SpringContextHelper(servletContext: ServletContext) {

  lazy val context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)

  def getBean[T](requiredType: Class[T]): T = {
    context.getBean(requiredType)
  }
}
