package com.ipsumllc.highfive.services

import akka.actor.{Props, Actor}
import com.ipsumllc.highfive.slappers.SlapMaster
import com.ipsumllc.highfive.users.UserSupe
import spray.routing.HttpService

/**
 * Created by tgrayson on 8/21/14.
 */
trait SlapServices {
  this: HttpService =>

  val slapper  = actorRefFactory.actorOf(Props(new SlapMaster))
  val userSupe = actorRefFactory.actorOf(Props(new UserSupe))

}