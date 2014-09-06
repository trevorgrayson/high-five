package com.ipsumllc.highfive.users

import akka.actor.Actor
import com.ipsumllc.highfive.slappers.{Slapper, Slap}
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

class UserActor(var state: User) extends /*Persistent*/Actor
  with Slapper {
  /*override*/def persistenceId = s"user-${state.contact}"

  val receive/*Command*/: Receive = {
    case WhoAreYou => sender ! state
    case Update(u:User) => state = u
    case user: User => { //optimistically updating, why?
      if(state._name == None || state.appleId == None) {
        val appleId = if( state.appleId == None ) {
          user.appleId
        } else {
          state.appleId
        }

        val _name = if( state._name == None ) {
          user._name
        } else {
          state._name
        }

        state = User(state.contact, _name, appleId)
      }

      sender ! state
    }
    //persist(user)(updateState)
    case s: Slap => {
      sendSlap( Slap(state, s.intensity, s.from) )
      sender ! "OK"
    }
    //case "snap" => saveSnapshot(state)
    case "print" => println(state)
  }

  def updateState(s: User): Unit =
    state = s

  val receiveRecover: Receive = {
    case user: User  => updateState(user)
    //case SnapshotOffer(_, snapshot: User) => state = snapshot
  }
}