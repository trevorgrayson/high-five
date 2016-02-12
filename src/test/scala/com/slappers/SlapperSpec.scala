package com.slappers

import org.specs2.mutable.Specification
import java.lang.Object
import com.ipsumllc.highfive.slappers.{Slap, Slapper}
import com.ipsumllc.highfive.users.{UserActor, User}
import akka.testkit.TestActorRef
import akka.actor.{ActorSystem, Props}
import com.ipsumllc.highfive.util.ContactNormalization

/**
* Created by tgrayson on 8/21/14.
*/

class SlapperSpec extends Specification
  with ContactNormalization {
  implicit val system = ActorSystem("Slapper")

  val user = User("8603847590", None, None)
  val slapper = TestActorRef(Props(new UserActor(user) with Slapper {
    var count = 0

    override def sendSlap(m: Slap) {
      count += 1
    }
  }))

  val from = User("8603849759", Some("Trevor"), Some("12345"))
  val to = User("1234567789", Some("Paul"), Some("54321"))
  val slapr = new Object with Slapper
  val ferocity = 3.1459


  "POST a slap" in {
    //implicit val timeout = Timeout( 3000 )
    val to = User("+18603849759", Some("Paul"), None)
    //slapper ! Slap(to, 1.0, to)
    slapper.underlyingActor
    "1" must be("1")
  }

  "Makes correct URL" in {
    pending
    val slap = Slap(to, ferocity, from)
    val request = slapr.request(slap)
    val url: String = request.toString

    val expected = s"${Slapper.domain}?" +
      s"from=${from.name}" +
      s"&ferocity=${ferocity}" +
      s"&to=${to.contact}"

    url must_== expected
  }
}
