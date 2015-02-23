//package com.ipsumllc.highfive.users
//
//import org.specs2.mutable.Specification
//import scala.concurrent.Await
//import scala.concurrent.duration._
//import akka.pattern.ask
//import akka.testkit.TestActorRef
//import akka.actor.ActorSystem
//import akka.util.Timeout
//import com.ipsumllc.highfive.util.ContactNormalization
//import scala.concurrent.duration._
//
///**
//* Created by tgrayson on 8/28/14.
//*/
//class UserSupeSpec extends Specification
//  with ContactNormalization {
//
//  implicit val system = ActorSystem("UserSupeSpec")
//  implicit val timeout = Timeout(3000 microseconds)
//  val supe = TestActorRef[UserSupe]
//
//  "UserSupe" should {
//    "should complete users" in {
//      val tele = "8603849759"
//      val user = User(tele,Some("Trevor"),Some("1234567"))
//      val partialUser = User(tele, None, None)
//
//      Await.result(supe ? user, 3000 microseconds)
//      val result = Await.result(supe ? partialUser, 3000 microseconds).asInstanceOf[User]
//      result.name must be("Trevor")
//      result.appleId.get must be("1234567")
//    }
//  }
//}
