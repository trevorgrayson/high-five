//package com.ipsumllc.highfive.users
//
//import org.specs2.mutable.Specification
//import akka.testkit.TestActorRef
//import akka.actor.{ActorSystem, Props}
//
//import scala.concurrent.Await
//import akka.pattern.ask
//import scala.concurrent.duration._
//import akka.util.Timeout
//import com.ipsumllc.highfive.services.SlapServices
//import spray.routing.HttpService
//
///**
// * Created by tgrayson on 8/18/14.
// */
//class UserSpec extends Specification
////  with SlapServices
//  {
//  implicit val system = ActorSystem("UserSpec")
//
//
//  "UserActor" should {
//
//    implicit val timeout = Timeout(1.0 seconds)
//    val contact = "860384759"
//    val user = User(contact, None, None)
//
//    "create a user" in {
//      val ua = TestActorRef(Props(Class[UserActor], user))
//      val result = Await.result(ua ? WhoAreYou, 1.0 seconds).asInstanceOf[User]
//
//      result.contact must be(contact)
//      result.name must be(contact)
//      result.appleId must be(None)
//    }
//
//    //"create a user through supe" in {
//      //val result = Await.result(userSupe ? user, 1.0 seconds)
//      //result.asInstanceOf[UserActor].contact must be(contact)
//    //}
//
//  }
//}