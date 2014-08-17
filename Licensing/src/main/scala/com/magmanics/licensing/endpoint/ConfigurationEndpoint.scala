package com.magmanics.licensing.endpoint

import com.magmanics.licensing.service.model.Configuration
import com.magmanics.licensing.service.{ConfigurationRepository, CustomerRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RestController}

/**
 * @author James Baxter - 16/08/2014.
 */
@RestController
@RequestMapping(Array("/configurations"))
class ConfigurationEndpoint {

  @Autowired
  var configurationRepository: ConfigurationRepository = _

  @Autowired
  var customerRepository: CustomerRepository = _

  /**
   * Create a configuration
   * @return the persistent configuration (ie includes an id).
   */
  @RequestMapping(method = Array(RequestMethod.POST))
  def create(configuration: Configuration): Configuration = configurationRepository.create(configuration)

  /**
   * Lookup a Configuration by its id
   * @throws NoSuchEntityException If a configuration with the given id cannot be found
   */
  @RequestMapping(value = Array("/id/{id}"), method = Array(RequestMethod.GET))
  def get(@PathVariable id: Long): Configuration = configurationRepository.get(id)

  /**
   * Get configurations for a particular customer. Returns an empty list if none are found.
   */
  @RequestMapping(value = Array("/customer/{customer}"), method = Array(RequestMethod.GET))
  def getByCustomer(@PathVariable customer: String): Seq[Configuration] = configurationRepository.getByCustomer(customer)

  /**
   * Try to get a configuration given a serial.
   */
  @RequestMapping(value = Array("/serial/{serial}"), method = Array(RequestMethod.GET))
  def getBySerial(@PathVariable serial: String): Option[Configuration] = configurationRepository.getBySerial(serial)

  /**
   * Updates a configuration
   */
  @RequestMapping(method = Array(RequestMethod.PUT))
  def update(configuration: Configuration) = configurationRepository.update(configuration)
}
