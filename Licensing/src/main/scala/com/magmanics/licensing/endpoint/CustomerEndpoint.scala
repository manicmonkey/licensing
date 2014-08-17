package com.magmanics.licensing.endpoint

import com.magmanics.licensing.service.CustomerRepository
import com.magmanics.licensing.service.model.Customer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * @author James Baxter - 16/08/2014.
 */
@RestController
@RequestMapping(Array("/customers"))
class CustomerEndpoint {

  @Autowired
  var customerRepository: CustomerRepository = _

  /**
   * Create a Customer, returning the persistent Customer (id populated)
   */
  @RequestMapping(method = Array(RequestMethod.POST))
  def create(customer: Customer): Customer = customerRepository.create(customer)

  /**
   * Get enabled Customers
   */
  @RequestMapping(value = Array("/enabled"), method = Array(RequestMethod.GET))
  def getEnabled: Seq[Customer] = customerRepository.getEnabled

  /**
   * Get all Customers within the system
   */
  @RequestMapping(method = Array(RequestMethod.GET))
  def get(): Seq[Customer] = customerRepository.get()

  /**
   * Gets a Customer given the specified id
   */
  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.GET))
  def get(@PathVariable id: Long): Option[Customer] = customerRepository.get(id)

  /**
   * Update the given Customer
   */
  @RequestMapping(method = Array(RequestMethod.PUT))
  def update(customer: Customer) = customerRepository.update(customer)
}
