package com.ipsumllc.highfive.users

import akka.actor.{Actor, Props}
import akka.persistence.PersistentActor
import com.ipsumllc.highfive.slappers.{SlapActor, Slapper, Slap}

/**
 * Created by tgrayson on 7/23/14.
 */
case class Update(u: User)
case class Event(u: User)
case object WhoAreYou
case object DELETE

case class Contact(string: String)
case class User( contact: Contact, _name: Option[String], appleId: Option[String]) {
  def name = {
    _name.getOrElse(contact.string)
  }

  def update(user: User): User = {
    if(_name == None || appleId == None) {
      User(
        contact,
        if (user.name != None) {
          user._name
        } else {
          _name
        },
        if (user.appleId != None) {
          user.appleId
        } else {
          appleId
        }
      )
    } else {
      this
    }
  }
}

class UserActor(var state: User) extends Actor { //PersistentActor {
  def persistenceId = s"user-${state.contact}"

  def receiveCommand: Receive = {
    case Update(u:User) =>
      //persist(Event(u))
      state = u
      sender ! state
  }

  //def receiveRecover: Receive = {
  //  case _ =>
  //}

  def receive = {
    case WhoAreYou => sender ! state
    case Update(u:User) =>
      state = u
      sender ! state
    case user: User => { //optimistically updating, why?
      state = state.update(user)
      sender ! state
      //persist(user)(updateState)
    }
    case s: Slap => println("SLAP:" + state);slapWorker forward Slap(state, s.intensity, s.from)
    //case DELETE => deleteMessages(999999)
    //case "snap" => saveSnapshot(state)
    case "print" => println(state)
  }

  def updateState(s: User): Unit =
    state = s

  def slapWorker = context.actorOf(Props(new SlapActor))

  val receiveRecover: Receive = {
    case user: User  => updateState(user)
    //case SnapshotOffer(_, snapshot: User) => state = snapshot
  }
}
