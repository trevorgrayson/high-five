//package com.ipsumllc.highfive
//
//import org.specs2.mutable.Specification
//import spray.testkit.Specs2RouteTest
//import spray.http._
//import StatusCodes._
//import com.example.MyService
//import com.ipsumllc.highfive.users.User
////import sun.misc.{BASE64Decoder, BASE64Encoder}
//
///**
// * Created by tgrayson on 9/1/14.
// */
//class BasicServices extends Specification
//  with Specs2RouteTest with MyService {
//
//  def actorRefFactory = system
//
//  val fromContact = "8603849759"
//  val contact     = "9195551234"
//  val applId      = "x1y2z3"
//  val name        = "Tre"
//  val intensity   = 1.9
//
//  "highfive.ipsumllc.com" should {
//
//    "invite" in {
//      invite()
//
//
//    }
//
//    "register an apple id" in {
//      val contact = "8605559759"
//      val name = "paul"
//      val appleId = "1234567890"
//
//      register(contact,name, appleId)
//
//      Get(s"/users/$contact") ~> myRoute ~> check {
//        status === OK
//        responseAs[String] === User(contact,Some(name),Some(appleId)).toString
//      }
//    }
//
//    "partially register" in {
//      val contact = "1029384756"
//      val appleId = "123AH4567890"
//
//      partialRegister(contact, appleId)
//
//      Get(s"/users/$contact") ~> myRoute ~> check {
//        status === OK
//        responseAs[String] === User(contact,None,Some(appleId)).toString
//      }
//    }
//
//    "send a slap to someone who is not registered" in {
//      //Post(s"/$fromContact/slaps/$contact") ~> myRoute ~> check {
//      Post(s"/slap/$fromContact/$contact/$intensity") ~> myRoute ~> check {
//        status === OK
//        //responseAs[String] === "NOT_REGISTERED"
//      }
//    }
//
//    "send a slap to someone who is registered" in {
//      pending
//      val contact = "8605559759"
//      val name = "paul"
//      val appleId = "1234567890"
//
//      register(contact,name, appleId)
//      register("2123138080",name, appleId)
//
//      Post(s"/slap/$contact/$contact/$intensity") ~> myRoute ~> check {
//        status === OK
//        responseAs[String] === "OK"
//      }
//    }
//
//  }
//
////  "decode base64" in {
////    val contact = "8603849759"
////    val i = contact.toLong
////    val encoded = (new BASE64Encoder()).encode(Array(i.toByte))
////    println("ENCODED:" + encoded)
////    val decoded = (new BASE64Decoder()).decodeBuffer(encoded)
////    println("DECODED" + decoded)
////
////    decoded.toString === contact
////  }
//
//  def invite(contact: String = "8603849759") = {
//    val hex = contact.toLong.toHexString
//    println("HEX INPUT:" + hex)
//    Post(s"/register/$hex") ~> myRoute ~> check {
//      status === OK
//      responseAs[String] === User(contact, None, None).toString
//    }
//  }
//
//  def register(contact: String = "8603849749", name: String = "tre", appleId: String = "123567890" ) {
//    Post(s"/users/$appleId/$contact/$name") ~> myRoute ~> check {
//      status === OK
//      responseAs[String] === User(contact,Some(name),Some(appleId)).toString
//    }
//  }
//
//  def partialRegister(contact: String = "8603849749", appleId: String = "123567890", name: String = "" ) {
//    var url = s"/users/$appleId/$contact"
//
//    if( !name.isEmpty ) url = url + "/" + name
//
//    Post(url) ~> myRoute ~> check {
//      status === OK
//      responseAs[String] === User(contact,None,Some(appleId)).toString
//    }
//  }
//
////  def slap(fromContact: String = fromContact, contact: String = contact, intensity: Double = intensity) {
////
////  }
//}
