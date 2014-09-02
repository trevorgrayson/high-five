package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class MyServiceSpec extends Specification with Specs2RouteTest with MyService {
  def actorRefFactory = system
  
  "MyService" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> myRoute ~> check {
        responseAs[String] must contain("Say hello")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }

    "return on register" in {
      Post("/register") ~> myRoute ~> check {
        status === OK
      }
    }

    "SLAP THE HIGH 5!!!" in {
      val from = "8603849759"
      val to = "8605559759"
      Post(s"/slap/$from/$to") ~> myRoute ~> check {
        status === OK
        responseAs[String] must contain(s"$from slapped $to")
      }
    }

//    "HIGH 5!!! Second notation" in {
//      Post("/then/8603849759/highfived/8605559759") ~> myRoute ~> check {
//        status === OK
//        responseAs[String] must contain("OK")
//      }
//    }
  }
}
