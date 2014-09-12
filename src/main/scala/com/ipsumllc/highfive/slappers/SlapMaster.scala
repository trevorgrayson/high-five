package com.ipsumllc.highfive.slappers

import akka.actor.{Props, Actor}
import com.ipsumllc.highfive.users.{UserActor, User}
import com.ipsumllc.highfive.services.SlapServices

/**
 * Created by tgrayson on 7/23/14.
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
