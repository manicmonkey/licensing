package com.magmanics.auditing.endpoint

import com.magmanics.auditing.model.{AuditCode, Audit}
import com.magmanics.auditing.service.{AuditService, AuditSearchDto}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestBody, RequestMethod, RequestMapping, RestController}

/**
 * @author James Baxter - 27/08/2014.
 */
@RestController
@RequestMapping(Array("/audits"))
class AuditEndpoint {

  @Autowired
  var auditService: AuditService = _

  /**
   * Persist the given Audit
   */
  @RequestMapping(method = Array(RequestMethod.POST))
  def create(@RequestBody audit: Audit) = auditService.create(audit)

  /**
   * @return a list of the distinct usernames for which there are audit entries
   */
  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/usernames"))
  def getUsernames: Seq[String] = auditService.getUsernames

  /**
   * @return a sequence of the distinct audit codes for which there are audit entries
   */
  @RequestMapping(method = Array(RequestMethod.GET), value = Array("/auditcodes"))
  def getAuditCodes: Seq[AuditCode] = auditService.getAuditCodes

  /**
   * @return a sequence of [[com.magmanics.auditing.model.Audit Audits]] which conform to the given search criteria
   */
  @RequestMapping(method = Array(RequestMethod.POST), value = Array("/messages"))
  def getAuditMessages(@RequestBody auditSearch: AuditSearchDto): Seq[Audit] = auditService.getAuditMessages(auditSearch)
}
