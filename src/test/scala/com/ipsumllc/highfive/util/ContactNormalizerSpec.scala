package com.ipsumllc.highfive.util

import org.specs2.mutable.Specification

/**
 * Created by tgrayson on 8/18/14.
 */
class ContactNormalizerSpec extends Specification {
  "Contact Normalizer" should {
    val expectation: String = "+18603849759"

    val tests = Map(
      "basic" -> "8603849759",
      "fancy" -> "(860) 384 - 9759",
      "expectation" -> "+18603849759",
      "email" -> s"$expectation@example.com"
    )


    val basic = "8603849759"
    val fancy = "(860) 384 - 9759"

    "turn emails into phone" in {
      ContactNormalizer.telephone(s"$basic@example.com") must_== expectation
    }

    "turn 10 digit numbers into phone" in {
      ContactNormalizer.telephone(basic) must_== expectation
    }

    "return +11 digit numbers" in {
      val result: String = ContactNormalizer.telephone(expectation)
      result must_== expectation
    }

    "turn fancy into phone" in {
      val result: String = ContactNormalizer.telephone(fancy)
      result must_== expectation
    }

//    def attempt(name: String, format: String) {
//      s"turn $name into phone" in {
//        val result: String = ContactNormalizer.telephone(format)
//        result must_== expectation
//      }
//    }
  }
}