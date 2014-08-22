package com.ipsumllc.highfive.users


import akka.actor.{Props, ActorRef, Actor}

/**
 * Created by tgrayson on 8/21/14.
 * Users exist just to complete requests.
 * Send in a partial user, get the rest.
 */
class UserSupe extends Actor {
  var users = Map.empty[String, ActorRef]

  def receive = {
    case u: User => getUser(u)
    case msg@Update(u: User) => getUser(u) forward msg
  }

  def getUser(u: User): ActorRef = {
    val id: String = u.contact
//    .getOrElse({
//      throw new Exception("Missing user Id!!!")
//    })

    users.getOrElse(id, {
      users = users ++ Map(id -> context.actorOf(
        Props(new UserActor(u)), u.name)
      )
    })

    users.getOrElse(id, throw new Exception("NUTS!"))
  }
}