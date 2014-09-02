package com.ipsumllc.highfive

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import com.example.MyService

/**
 * Created by tgrayson on 9/1/14.
 */
class BasicServices extends Specification
  with Specs2RouteTest with MyService {

  def actorRefFactory = system

  val fromContact = "8603849759"
  val contact = "9195551234"
  val applId  = "x1y2z3"
  val name    = "Tre"

  "highfive.ipsumllc.com" should {

    "register an apple id" in {
      Post("/users") ~> myRoute ~> check {
        status === OK
      }
    }

    "send a slap to someone who is not registered" in {
      Post(s"/$fromContact/slaps/$contact") ~> myRoute ~> check {
        status === OK
      }
    }

    "send a slap to someone who is registered" in {
      Post(s"/$fromContact/slaps/$contact") ~> myRoute ~> check {
        status === OK
      }
    }
  }
}
