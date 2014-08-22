package com.ipsumllc.highfive.users

import akka.actor.Actor
import com.ipsumllc.highfive.slappers.Slap
import scalaj.http.Http

/**
 * Created by tgrayson on 7/23/14.
 */
case object WhoAreYou
case class Update(u: User)

case class User( contact: String, _name: Option[String], appleId: Option[String]) {
  def name = {
    _name.getOrElse(contact)
  }
}

class UserActor(var user: User) extends Actor {

  def receive = {
    case WhoAreYou => sender ! user
    case Update(u:User) => user = u
  }

}