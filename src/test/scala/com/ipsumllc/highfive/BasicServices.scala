package com.ipsumllc.highfive

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import com.example.MyService
import com.ipsumllc.highfive.users.User

/**
 * Created by tgrayson on 9/1/14.
 */
class BasicServices extends Specification
  with Specs2RouteTest with MyService {

  def actorRefFactory = system

  val fromContact = "8603849759"
  val contact     = "9195551234"
  val applId      = "x1y2z3"
  val name        = "Tre"
  val intensity   = 1.9

  "highfive.ipsumllc.com" should {

    "register an apple id" in {
      val contact = "8605559759"
      val name = "paul"
      val appleId = "1234567890"

      register(contact,name, appleId)

      Get(s"/users/$contact") ~> myRoute ~> check {
        status === OK
        responseAs[String] === User(contact,Some(name),Some(appleId)).toString
      }
    }

    "send a slap to someone who is not registered" in {
      //Post(s"/$fromContact/slaps/$contact") ~> myRoute ~> check {
      Post(s"/slap/$fromContact/$contact/$intensity") ~> myRoute ~> check {
        status === OK
        responseAs[String] === "NOT_REGISTERED"
      }
    }

    "send a slap to someone who is registered" in {
      val contact = "8605559759"
      val name = "paul"
      val appleId = "1234567890"

      register(contact,name, appleId)
      register("2123138080",name, appleId)

      Post(s"/slap/$contact/$contact/$intensity") ~> myRoute ~> check {
        status === OK
        responseAs[String] === "OK"
      }
    }

  }

  def register(contact: String = "8603849749", name: String = "tre", appleId: String = "123567890" ) {
    Post(s"/users/$contact/$name/$appleId") ~> myRoute ~> check {
      status === OK
      responseAs[String] === User(contact,Some(name),Some(appleId)).toString
    }
  }

//  def slap(fromContact: String = fromContact, contact: String = contact, intensity: Double = intensity) {
//
//  }
}
