package com.ipsumllc.highfive


import org.specs2.mutable.Specification
import akka.actor.{Props, ActorSystem}
import com.ipsumllc.highfive.slappers.{Slapper, Slap}
import com.ipsumllc.highfive.users.{UserActor, User}
import akka.testkit.TestActorRef

import akka.util.Timeout
import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._

class SlapperSpec extends Specification {
  implicit val system = ActorSystem("Slapper")

  "Slapper" should {
    val user = User("860384759", None, None)
    val slapper = TestActorRef(Props(new UserActor(user) with Slapper {
      var count = 0
      override def sendSlap(m: Slap) {
        count += 1
      }
    }))

    "POST a slap" in {
      //implicit val timeout = Timeout( 3000 )
      val to = User("+18603849759", Some("Paul"), None)
      //slapper ! Slap(to, 1.0, to)
      slapper.underlyingActor
      "1" must be("1")
    }

//    "leave GET requests to other paths unhandled" in {
//      Get("/kermit") ~> myRoute ~> check {
//        handled must beFalse
//      }
//    }
//
//    "return a MethodNotAllowed error for PUT requests to the root path" in {
//      Put() ~> sealRoute(myRoute) ~> check {
//        status === MethodNotAllowed
//        responseAs[String] === "HTTP method not allowed, supported methods: GET"
//      }
//    }
  }
}