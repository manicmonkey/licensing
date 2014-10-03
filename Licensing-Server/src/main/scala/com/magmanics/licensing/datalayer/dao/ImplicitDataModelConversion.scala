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
import com.magmanics.licensing.model._
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
 * Facilitates the conversion of Entity representations of the model to their detached equivalents 
 *
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 03-Aug-2010
 */
object ImplicitDataModelConversion {

  val log = LoggerFactory.getLogger("com.magmanics.licensing.datalayer.dao.ImplicitEntityModelConversion")

  //implicit activation conversions
  implicit def activationEntityToActivation(ac: ActivationEntity): Activation = {
    log.trace("Converting ActivationEntity: {}", ac)
    Activation(Some(ac.id), ac.created, ac.machineIdentifier, ac.productVersion, ac.configurationId, ActivationType.withName(ac.activationType), ac.getActivationInfo().asScala.map(ai => ai.key -> ai.value).toMap)
  }
  implicit def activationEntitySeqToActivationSet(activations: Seq[ActivationEntity]): Set[Activation] = {
    log.trace("Converting Seq[ActivationEntity]: {}", activations)
    activations.map(a => activationEntityToActivation(a)).toSet
  }

  //implicit configuration conversions
  implicit def configurationEntityToConfiguration(configuration: ConfigurationEntity): Configuration = {
    log.trace("Converting ConfigurationEntity: {}", configuration)
    Configuration(
      Some(configuration.id),
      configuration.user,
      configuration.product.id,
      configuration.customerId,
      configuration.created,
      Some(configuration.serial),
      Map(configuration.options.asScala.map(a => a.key -> a.value): _*),
      configuration.enabled,
      configuration.maxActivations,
      configuration.activations.asScala)
  }
  implicit def configurationEntitySeqToConfigurationSet(configurations: Seq[ConfigurationEntity]): Set[Configuration] = {
    log.trace("Converting Seq[ConfigurationEntity]: {}", configurations)
    configurations.map(a => configurationEntityToConfiguration(a)).toSet
  }

  //implicit customer conversions
  implicit def customerEntityToCustomer(customerEntity: CustomerEntity): Customer = {
    log.trace("Converting CustomerEntity: {}", customerEntity)
    Customer(Some(customerEntity.id), customerEntity.name, customerEntity.enabled)
  }
  implicit def customerEntityOptionToCustomerOption(customerEntity: Option[CustomerEntity]): Option[Customer] = {
    log.trace("Converting CustomerEntity: {}", customerEntity)
    customerEntity match {
      case Some(c) => Some(customerEntityToCustomer(c))
      case _ => None
    }
  }
  implicit def customerEntitySeqToCustomerSet(customers: Seq[CustomerEntity]): Set[Customer] = {
    log.trace("Converting Seq[CustomerEntity]: {}", customers)
    customers.map(a => customerEntityToCustomer(a)).toSet
  }

  //implicit product conversions
  implicit def productEntityToProduct(product: ProductEntity): Product = {
    log.trace("Converting ProductEntity: {}", product)
    Product(Some(product.id), product.name, product.description, product.enabled, product.getOptions)
  }
  implicit def productEntityOptionToProductOption(productEntity: Option[ProductEntity]): Option[Product] = {
    log.trace("Converting ProductEntity: {}", productEntity)
    productEntity match {
      case Some(c) => Some(productEntityToProduct(c))
      case _ => None
    }
  }
  implicit def productEntitySeqToProductSet(products: Seq[ProductEntity]): Set[Product] = {
    log.trace("Converting Seq[ProductEntity]: {}", products)
    products.map(p => productEntityToProduct(p)).toSet
  }

  //implicit product option conversions
  implicit def productOptionEntityToProductOption(option: ProductOptionEntity[_]): ProductOption[_] = {
    log.trace("Converting ProductOptionEntity: {}", option)
    option match {
      case option: TextProductOptionEntity => TextOption(Some(option.id), option.name, option.default)
      case option: RadioProductOptionEntity => BoolOption(Some(option.id), option.name, option.default)
      case option: ListProductOptionEntity => ListOption(Some(option.id), option.name, option.default, option.optionValues.asScala.map(_.value).toSet)
    }
  }
  implicit def productOptionEntitySetToProductOptionSet(options: Set[ProductOptionEntity[_]]): Set[ProductOption[_]] = {
    log.trace("Converting Seq[ProductOptionEntity]: {}", options)
    options.map(o => productOptionEntityToProductOption(o))
  }
}