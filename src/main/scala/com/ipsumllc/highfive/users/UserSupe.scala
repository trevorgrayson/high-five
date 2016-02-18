package com.ipsumllc.highfive.users


import java.lang.Long._
import akka.actor.{Props, ActorRef, Actor}
import com.ipsumllc.highfive.slappers.Slap
import com.ipsumllc.highfive.util.ContactNormalization

//import scalaj.http.Base64

/**
 * Created by tgrayson on 8/21/14.
 * Users exist just to complete requests.
 * Send in a partial user, get the rest.
 */
case class NewUser(invite: String, name:Option[String]=None, deviceId:Option[String]=None)

class UserSupe extends Actor
  with ContactNormalization {
  var users = Map.empty[Contact, ActorRef]

  def receive = {
    case m @ NewUser(invite,name, deviceId) => {
      val contact = invite //parseLong(invite, 16).toString
      println(s"Decoded String is: $contact")
      self.tell(User(contact, name, deviceId), sender)
    }
    case m @ (to: User, from: User, intensity: Double) => self.tell(Slap(to, intensity, from), sender)
    case m : Slap => getUserActor(m.to) forward m
    case u : User => getUserActor(u) forward u
    case m @ Update(u: User) => getUserActor(u) forward m
  }

  def getUserActor(u: User): ActorRef = {
    val id: Contact = u.contact

    users.getOrElse(id, {
      users = users ++ Map(id -> userWorker(u))
    })
    users.getOrElse(id, throw new Exception("NUTS!"))
  }

  def userWorker(u: User):ActorRef = context.actorOf(Props(new UserActor(u)), u.contact.string)
}