package com.magmanics.licensing

import org.scalatest.{BeforeAndAfterEach, FeatureSpec, GivenWhenThen}

/**
 * @author James Baxter <j.w.baxter@gmail.com>
 * @since 11-Jul-2010
 */
class LicensingSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfterEach {

  override protected def beforeEach() {
    //setup blank db
  }

  override protected def afterEach() {
    //need to empty db?
  }

  feature("Products can be managed by a user") {
    scenario("a product is created with a textfield") (pending)
    scenario("a product is created with a dropdown") (pending)
    scenario("a product is created with a checkbox") (pending)
    scenario("a product is disabled") (pending)
    scenario("a product is enabled") (pending)
  }

  feature("Licence configurations can be managed by a user") {
    scenario("a configuration is created") (pending)
    scenario("a configuration is disabled") (pending)
    scenario("a configuration is enabled") (pending)
  }

  feature("Activations fail if a parent is disabled") {
    scenario("an activation request for a disabled configuration") (pending)
    scenario("an activation request for a disabled product") (pending)
    scenario("an activation request for a disabled customer") (pending)
  }

  feature("An activation can contain additional information") {
    scenario("the activation contains extra info") (pending)
    scenario("the activation contains no extra info") (pending)
  }

  feature("An existing activation can be reactivated without cost") {
    scenario("there are available activations") (pending)
    scenario("there are no available activations") (pending)
  }

  feature("Activations are transported as xml") {
    scenario("an activation request is deserialised from xml") (pending)
    scenario("an activation response is serialised to xml") (pending)
  }
}