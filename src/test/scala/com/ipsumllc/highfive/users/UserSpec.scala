package com.ipsumllc.highfive.users

import org.specs2.mutable.Specification
import akka.testkit.TestActorRef
import akka.actor.{ActorSystem, Props}

import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout

/**
 * Created by tgrayson on 8/18/14.
 */
class UserSpec extends Specification {
  implicit val system = ActorSystem("UserSpec")


  "UserActor" should {

    implicit val timeout = Timeout(1.0 seconds)

    "create a user" in {
      val contact = "860384759";
      val user = User(contact, None, None)

      val ua = TestActorRef(Props(new UserActor(user)))
      val result = Await.result(ua ? WhoAreYou, 1.0 seconds).asInstanceOf[User]

      result.contact must be(contact)
      result.name must be(contact)
      result.appleId must be(None)
    }

  }
}