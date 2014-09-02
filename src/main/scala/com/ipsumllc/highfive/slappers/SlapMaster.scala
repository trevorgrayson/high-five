package com.ipsumllc.highfive.slappers

import akka.actor.{Props, Actor}
import com.ipsumllc.highfive.users.{UserActor, User}

/**
 * Created by tgrayson on 7/23/14.
 */
case class Slap( to: User, intensity: Double, from: User )

class SlapMaster extends Actor {

  def receive = {
    case m: Slap => {
      println("SENDING SLAP MAS")
      slapper(m) forward(m)
    }
  }

  def slapper(m: Slap) = context.actorOf(Props(new UserActor(m.to) with Slapper))
}
