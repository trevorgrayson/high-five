package com.ipsumllc.highfive.users

import org.specs2.mutable.Specification
import akka.testkit.TestActorRef
import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import com.ipsumllc.highfive.services.SlapServices
import spray.routing.HttpService

/**
* Created by tgrayson on 8/18/14.
*/
class UserSpec extends Specification
//  with SlapServices
  {
  implicit val system = ActorSystem("UserSpec")


  "UserActor" should {

    implicit val timeout = Timeout(1.0 seconds)
    val contact = "860384759"
    val user = User(contact, None, None)

    "create a user" in {
      val ua = TestActorRef(Props(new UserActor(user)))
      val result = Await.result(ua ? WhoAreYou, 1.0 seconds).asInstanceOf[User]

      result.contact must be(contact)
      result.name must be(contact)
      result.appleId must be(None)
    }

    "update a user" in {
      val name = "tre"
      val appleId = "123567890"
      val ua = TestActorRef(Props(new UserActor(user)))
      val user1 = User(contact, Some(name),Some(appleId))
      Await.result(ua ? user1, 1.0 seconds)
      val result = Await.result(ua ? WhoAreYou, 1.0 seconds).asInstanceOf[User]

      result.contact     must be(contact)
      result.name        must be(name)
      result.appleId.get must be(appleId)
    }

//    def updateUser(ua:ActorRef, contact:String = contact, name:String = "tre", appleId:String = "1234567890") {
//      Await.result(ua ? User(contact, Some(name),Some(appleId)), 1.0 seconds)
//    }

    //"create a user through supe" in {
      //val result = Await.result(userSupe ? user, 1.0 seconds)
      //result.asInstanceOf[UserActor].contact must be(contact)
    //}

  }
}