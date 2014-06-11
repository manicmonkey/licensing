package com.magmanics.auditing.service

import java.util.Date

/**
 * Criteria to narrow down Audit searches
 *
 * @author jbaxter - 27/06/11
 */
case class AuditSearchDto(fromDate: Date, toDate: Date, users: Seq[String], auditCodes: Seq[String], text: String)