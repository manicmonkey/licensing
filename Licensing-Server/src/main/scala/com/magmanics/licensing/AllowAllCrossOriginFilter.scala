package com.magmanics.licensing

import javax.servlet._
import javax.servlet.http.HttpServletResponse

/**
 * @author jbaxter - 2014-09-12
 */
class AllowAllCrossOriginFilter extends Filter {

  override def init(filterConfig: FilterConfig) {}

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    response.asInstanceOf[HttpServletResponse].addHeader("Access-Control-Allow-Origin", "*")
    response.asInstanceOf[HttpServletResponse].addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
    response.asInstanceOf[HttpServletResponse].addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS")
    chain.doFilter(request, response)
  }

  override def destroy() {}
}
