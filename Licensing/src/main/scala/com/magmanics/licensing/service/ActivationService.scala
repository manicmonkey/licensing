/**
 * Magmanics Licensing. This web application allows for centralized control
 * of client application activation, with optional configuration parameters
 * to control licensable features, and storage of supplementary information
 * about the client machine. Client applications may interface with this
 * central server (for activation) using libraries licenced under an
 * alternative licence.
 *
 * Copyright (C) 2010 James Baxter <j.w.baxter(at)gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmanics.licensing.service

import com.magmanics.auditing.Auditable
import com.magmanics.licensing.service.exception._
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that handles Activation Requests - updates Configuration with new Activation and performs auditing
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 31-May-2010
 */
trait ActivationService {
  /**
   * Activate licence request by checking the serial and maximum activations
   */
  def activate(licenceActivationRequest: ActivationRequest): ActivationResponse
}

class ActivationServiceImpl(configurationRepository: ConfigurationRepository, customerRepository: CustomerRepository, productRepository: ProductRepository) extends ActivationService {

  val log = LoggerFactory.getLogger(classOf[ActivationService])

  @PreAuthorize("hasRole('CAN_ACTIVATE')")
  @Auditable("audit.activation")
  def activate(licenceActivationRequest: ActivationRequest): ActivationResponse = { //todo auditing

    log.debug("Received ActivationRequest: {}", licenceActivationRequest)

    val configuration = configurationRepository.getBySerial(licenceActivationRequest.serial)
            .getOrElse(throw new NoSuchLicenceException(licenceActivationRequest.serial))

    if (!configuration.enabled)
      throw new ConfigurationDisabledException

    if (!customerRepository.get(configuration.customerId).get.enabled)
      throw new CustomerDisabledException

    if (!productRepository.get(configuration.productId).get.enabled)
      throw new ProductDisabledException

    configuration.addActivation(licenceActivationRequest.machineIdentifier, licenceActivationRequest.productVersion, licenceActivationRequest.extraInfo)
    configurationRepository.update(configuration)

    ActivationResponse(configuration.options)
  }
}

case class ActivationRequest(serial: String, machineIdentifier: String, productVersion: String, extraInfo: Map[String, String] = Map())

case class ActivationResponse(productOptions: Map[String, String])