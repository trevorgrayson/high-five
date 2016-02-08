package com.ipsumllc.highfive.slappers

import akka.actor.Actor

/**
 * Created by tgrayson on 7/23/14.
 * Slap action worker class, he should be managed from above on failure.
 * Receives a slap message, "sendSlaps."
 */

class SlapActor extends Actor with Slapper {
  def receive = {
    case slap@Slap(to, intensity, from) => {
      if( to.appleId != None ) {
        //not waiting to see if slap succeeds
        sender ! "OK"
        println("Attempting Slap!: " + slap)
        sendSlap( slap )

      } else {
        println("A WHO IS THIS GUY?")
        sender ! "NOT_REGISTERED"
      }
      context.stop(self)
    }
  }
}

