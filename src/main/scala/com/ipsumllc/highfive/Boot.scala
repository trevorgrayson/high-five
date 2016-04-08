package com.ipsumllc.highfive

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("slapnets")

  // create and start our service actor
  val service = system.actorOf(Props[WebServiceActor], "demo-service")
  val port = scala.util.Properties.envOrElse("PORT", "8080").toInt


  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = "0.0.0.0", port = port)
}
