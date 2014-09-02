package com.ipsumllc.highfive.users

import akka.actor.Actor
import com.ipsumllc.highfive.slappers.Slap
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

class UserActor(var state: User) extends /*Persistent*/Actor {
  /*override*/def persistenceId = s"user-${state.contact}"

  val receive/*Command*/: Receive = {
    case WhoAreYou => sender ! state
    case Update(u:User) => state = u
    case user: User => //persist(user)(updateState)
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