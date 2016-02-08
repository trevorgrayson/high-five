package com.ipsumllc.highfive.slappers

import akka.actor.Actor
import com.ipsumllc.highfive.users.User

/**
 * Created by tgrayson on 7/23/14.
 * Manages SlapActor workers.  This should be registering workers, and
 * handling error conditions.  Presently this is just fire and forget.
 */
case class Slap( to: User, intensity: Double, from: User )

class SlapMaster extends Actor {

  def receive = {
    //case m: Slap => slapper(m) forward(m)
    case m @ (to: User, from: User, intensity: Double) =>
      context.actorSelection("/user/userSupe") ! Slap(to, intensity, from)
    case _ => throw new Exception("SlapMaster miss ERROR")
  }
}
