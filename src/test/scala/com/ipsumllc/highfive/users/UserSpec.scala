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
import com.ipsumllc.highfive.util.ContactNormalization

/**
* Created by tgrayson on 8/18/14.
*/
class UserSpec extends Specification
  with ContactNormalization
//  with SlapServices
  {
  implicit val system = ActorSystem("UserSpec")


  "UserActor" should {

    implicit val timeout = Timeout(5.0 seconds)
    val contact: Contact = "8603849759"
    val user = User(contact, None, None)

    "create a user" in {
      val ua = system.actorOf(Props(new UserActor(user)))
      val result = Await.result(ua ? WhoAreYou, 10.0 seconds).asInstanceOf[User]

      ua ! DELETE

      result.contact must be(contact)
      result.name must be(contact.string)
      result.appleId must be(None)

    }

    "update a user" in {
      val name = "tre"
      val appleId = "123567890"
      val ua = system.actorOf(Props(new UserActor(user)))
      val user1 = User(contact, Some(name),Some(appleId))
      Await.result(ua ? user1, 10.0 seconds)
      val result = Await.result(ua ? WhoAreYou, 5.0 seconds).asInstanceOf[User]

      ua ! DELETE

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