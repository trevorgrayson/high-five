package com.ipsumllc.highfive.services

import akka.actor.{Actor, Props}
import com.ipsumllc.highfive.slappers.SlapMaster
import com.ipsumllc.highfive.users.UserSupe

/**
 * Created by tgrayson on 9/12/14.
 */
trait CoreActors {
  this: Actor =>

  val slapper  = context.actorSelection("/user/slapper")
  val userSupe = context.actorSelection("/user/userSupe")

}
