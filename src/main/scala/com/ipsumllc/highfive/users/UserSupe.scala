package com.ipsumllc.highfive.users


import akka.actor.{Props, ActorRef, Actor}

/**
 * Created by tgrayson on 8/21/14.
 */
class UserSupe extends Actor {
  var users = Map.empty[String, ActorRef]

  def receive = {
    case u: User => getUser(u)
    case msg @ Update(u:User) => getUser(u) forward msg
  }

  def getUser(u: User): ActorRef = {
    val appleId: String = u.appleId.getOrElse({
      throw new Exception("Missing user Id!!!")
    })

    users.getOrElse(appleId, {
      users = users ++ Map(appleId -> context.actorOf(
        Props(new UserActor(u)), u.name )
      )
    })

    users.getOrElse(appleId, throw new Exception("NUTS!"))
  }
}