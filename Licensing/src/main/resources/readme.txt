Need to flesh out tests...

End-to-end integration tests with Selenium for UI / whole stack (ScalaTest can drive)
Functional tests with ScalaTest for services
Integration tests with JUnit for daos (model mapped correctly etc)
Unit tests with ScalaCheck for model

add implicit conversions for entities -> service case class

maybe move dtos to highlight dependence and sphere of influence
move dtos to datalayer or at least out of service as cyclic dependency between packages

acceptance test for activation clients? mock implementation of service in java/.net for including in external projects

Auditing
-create / update configurations, products, customers, activations
-logins
-double click to view details (diff?) (id and rev in interface?)
-include username
-spring security?
-email reports - interval and frequency of certain (configurable) types of events
--domain objects @auditable
--service methods @read, @write, @read_write
--read/write - mean db, not return/param - need read write?
--advice targets service package with return/arg auditable and uses read/write for audit message
---implement audit dao -> move all classes to higher audit package
---provide proper audit message
---need ui at some point (auditCode = lang prop), group by auditCode and/or username for filtering, oh datetime narrow to

customer - grouping for user visibility? using PostAuthorize

long lists of identically types params harmful - promote mistaken transposition

Sort out ui
Jaxws

button to email licence to address
what to do about updating product options?

need jsr 303 bean validation? would be nice @Valid

if spawning threads for push - need to configure SecurityContextHolder.MODE_INHERITABLETHREADLOCAL
Custom AuthenticationEntryPoint/ExceptionTranslationFilter to handle users attempting to access secured resources? shouldn't be 100% necessary if resources hidden...