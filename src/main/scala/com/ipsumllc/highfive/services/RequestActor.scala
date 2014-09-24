package com.ipsumllc.highfive.services

import akka.actor.Actor
import spray.routing.HttpService
import com.ipsumllc.highfive.slappers.Slap

/**
 * Created by tgrayson on 9/12/14.
 */
class RequestActor(service: HttpService) extends Actor with CoreActors {
  def receive = awaitingRequest

  def awaitingRequest: Receive = {
    case slap:Slap => userSupe ! slap
    case m => context.become(awaitingResponse)
  }

  def awaitingResponse: Receive = {
    case m: Any => {
      service.complete(m.toString)
      context.stop(self)
    }

  }
}
