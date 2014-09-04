package com.ipsumllc.highfive.slappers

import akka.actor.{Props, Actor}
import com.ipsumllc.highfive.users.{UserActor, User}

/**
 * Created by tgrayson on 7/23/14.
 */
case class Slap( to: User, intensity: Double, from: User )

class SlapMaster extends Actor {

  def receive = {
    case m: Slap => slapper(m) forward(m)
    case m @ (to: User, from: User, intensity: Double) =>
      slapper(to) forward Slap(to, intensity, from)
  }

  def slapper(m: Slap) = context.actorOf(Props(new UserActor(m.to) with Slapper))
  def slapper(to: User) = context.actorOf(Props(new UserActor(to) with Slapper))
}
