package com.magmanics.licensing.datalayer.dao

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

import com.magmanics.licensing.datalayer.model._
import org.slf4j.LoggerFactory
import com.magmanics.licensing.service.model._

/**
 * Facilitates the conversion of Circumflex representations of the model to their detached equivalents 
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 03-Aug-2010
 */
object ImplicitCircumflexModelConversion {

  val log = LoggerFactory.getLogger("com.magmanics.licensing.datalayer.dao.ImplicitCircumflexModelConversion")

  //implicit activation conversions
  implicit def activationCircumflexToActivation(ac: ActivationCircumflex): Activation = {
    log.trace("Converting ActivationCircumflex: {}", ac)
    Activation(Some(ac.PRIMARY_KEY()), ac.created(), ac.machineIdentifier(), ac.productVersion(), ActivationType.withName(ac.activationType()), Map(ac.extraInfo.get.map(ai => ai.key() -> ai.value()): _*))
  }
  implicit def activationCircumflexSeqToActivationSeq(activations: Seq[ActivationCircumflex]): Seq[Activation] = {
    log.trace("Converting Seq[ActivationCircumflex]: {}", activations)
    activations.map(a => activationCircumflexToActivation(a))
  }

  //implicit configuration conversions
  implicit def configurationCircumflexToConfiguration(configuration: ConfigurationCircumflex): Configuration = {
    log.trace("Converting ConfigurationCircumflex: {}", configuration)
    Configuration(
      Some(configuration.PRIMARY_KEY()),
      configuration.user(),
      configuration.product().PRIMARY_KEY(),
      configuration.customer().PRIMARY_KEY(),
      configuration.created(),
      Some(configuration.serial()),
      Map(configuration.options.get.map(a => a.key() -> a.value()): _*),
      configuration.enabled(),
      configuration.maxActivations(),
      configuration.activations.get)
  }
  implicit def configurationCircumflexSeqToConfigurationSeq(configurations: Seq[ConfigurationCircumflex]): Seq[Configuration] = {
    log.trace("Converting Seq[ConfigurationCircumflex]: {}", configurations)
    configurations.map(a => configurationCircumflexToConfiguration(a))
  }

  //implicit customer conversions
  implicit def customerCircumflexToCustomer(customerCircumflex: CustomerCircumflex): Customer = {
    log.trace("Converting CustomerCircumflex: {}", customerCircumflex)
    Customer(Some(customerCircumflex.PRIMARY_KEY()), customerCircumflex.name(), customerCircumflex.enabled())
  }
  implicit def customerCircumflexOptionToCustomerOption(customerCircumflex: Option[CustomerCircumflex]): Option[Customer] = {
    log.trace("Converting CustomerCircumflex: {}", customerCircumflex)
    customerCircumflex match {
      case Some(c) => Some(customerCircumflexToCustomer(c))
      case _ => None
    }
  }
  implicit def customerCircumflexSeqToCustomerSeq(customers: Seq[CustomerCircumflex]): Seq[Customer] = {
    log.trace("Converting Seq[CustomerCircumflex]: {}", customers)
    customers.map(a => customerCircumflexToCustomer(a))
  }

  //implicit product conversions
  implicit def productCircumflexToProduct(product: ProductCircumflex): Product = {
    log.trace("Converting ProductCircumflex: {}", product)
    Product(Some(product.PRIMARY_KEY()), product.name(), product.description(), product.enabled(), product.getOptions) //todo convert product option...
  }
  implicit def productCircumflexOptionToProductOption(productCircumflex: Option[ProductCircumflex]): Option[Product] = {
    log.trace("Converting ProductCircumflex: {}", productCircumflex)
    productCircumflex match {
      case Some(c) => Some(productCircumflexToProduct(c))
      case _ => None
    }
  }
  implicit def productCircumflexSeqToProductSeq(products: Seq[ProductCircumflex]): Seq[Product] = {
    log.trace("Converting Seq[ProductCircumflex]: {}", products)
    products.map(p => productCircumflexToProduct(p))
  }

  //implicit product option conversions
  implicit def productOptionCircumflexToProductOption(option: ProductOptionCircumflex[Any]): ProductOption[_] = {
    log.trace("Converting ProductOptionCircumflex: {}", option)
    option match {
      case option: TextProductOptionCircumflex => TextOption(Some(option.PRIMARY_KEY()), option.name(), option.default())
      case option: RadioProductOptionCircumflex => BoolOption(Some(option.PRIMARY_KEY()), option.name(), option.default())
      case option: ListProductOptionCircumflex => ListOption(Some(option.PRIMARY_KEY()), option.name(), option.default(), option.values.apply.map(_.value()))
    }
  }
  implicit def productOptionCircumflexSeqToProductOptionSeq(options: Seq[ProductOptionCircumflex[_]]): Seq[ProductOption[_]] = {
    log.trace("Converting Seq[ProductOptionCircumflex]: {}", options)
    options.map(o => productOptionCircumflexToProductOption(o))
  }
}