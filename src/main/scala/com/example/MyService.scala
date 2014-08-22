package com.example

import akka.actor.{ActorSystem, Props, Actor}
import spray.routing._
import spray.http._
import MediaTypes._
import com.ipsumllc.highfive.slappers.{Slap, SlapMaster}
import com.ipsumllc.highfive.users.User

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

//
//  val slapMaster = context.actorOf(Props(new SlapMaster), "master")

  val myRoute =
    path("") {
      get {
        complete {
          "Say hello"
        }
      }
////      get {
//        parameters("from".! , "to".!) { (from: String, to: String) =>
//          respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
//            //slapMaster ! Slap( User("Trevor","8603849759"), 1.2, User("Trevor","8603849759") )
//            complete {
//
//              <html>
//                <body>
//                  <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
//                </body>
//              </html>
//            }
//          }
//        }
////      }
    } ~
    path("/register") {
      get {
        complete {
          "Gettin'"
        }
      }
        post {
          complete {
            "OK"
          }
        }
    }
}