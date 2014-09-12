package com.ipsumllc.highfive.users

import akka.actor.{Props, Actor}
import com.ipsumllc.highfive.slappers.{SlapActor, Slapper, Slap}
import scalaj.http.Http

//import akka.actor._
//import akka.persistence._

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

class UserActor(var state: User) extends /*Persistent*/Actor  {
  /*override*/def persistenceId = s"user-${state.contact}"

  val receive/*Command*/: Receive = {
    case WhoAreYou => sender ! state
    case Update(u:User) => state = u
    case user: User => { //optimistically updating, why?
      if(state._name == None || state.appleId == None) {
        val appleId = if( user.appleId != None ) {
          user.appleId
        } else {
          state.appleId
        }

        val _name = if( user.name != None ) {
          user._name
        } else {
          state._name
        }

        state = User(state.contact, _name, appleId)
        println(state)
      }

      sender ! state
    }
    //persist(user)(updateState)
    case s: Slap => println("SLAP:" + state);slapWorker forward Slap(state, s.intensity, s.from)

    //case "snap" => saveSnapshot(state)
    case "print" => println(state)
  }

  def updateState(s: User): Unit =
    state = s

  def slapWorker = (context.actorOf(Props(new SlapActor)))

  val receiveRecover: Receive = {
    case user: User  => updateState(user)
    //case SnapshotOffer(_, snapshot: User) => state = snapshot
  }
}