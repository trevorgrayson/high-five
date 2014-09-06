package com.example

import akka.actor.{ActorSystem, Props, Actor}
import spray.routing._
import spray.http._
import MediaTypes._
import com.ipsumllc.highfive.slappers.{Slap, SlapMaster}
import com.ipsumllc.highfive.users.User
import com.ipsumllc.highfive.services.SlapServices
import akka.util.Timeout
import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor
  with MyService
  with SlapServices {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with SlapServices {
  //MEHHH can we not be asking?
  import scala.concurrent.duration._
  import akka.pattern.ask

  val myRoute =
    path("") {
      get {
        complete {
          "What's up bro?!"
        }
      }
    } ~
    path("register") {
      post {
        complete {
          "OK"
        }
      }
    } ~
  pathPrefix("slap") {
    pathPrefix(Segment) { from =>
      pathPrefix(Segment) { to =>
        path(DoubleNumber) { jerk =>
          implicit val timeout = Timeout(10 seconds)
          val fU = User(from.toString, None, None)
          val tU = User(to.toString, None, None)

          for {
            r  <- slapper ? (tU, fU, jerk)
          } yield r

          complete("OK")
          //complete(s"$from slapped $to")
        }
      }
    }
  } ~
  pathPrefix("then") {
    pathPrefix(IntNumber) { from =>
      pathPrefix("highfived") {
        path(IntNumber) { to =>
//          implicit val timeout = Timeout(3 seconds)
//          val fU = User(from.toString, None, None)
//          val tU = User(to.toString, None, None)
//          val result = for {
//            to <- userSupe ? tU
//            a  <- slapper ? Slap(tU, 1.0, fU)
//          } yield "OK"

          complete("OK")
        }
      }
    }
  } ~
  path("users") {
    val contact, name, appleId = "1"

    implicit val timeout = Timeout(3 seconds)
    val user = User(contact,Some(name), Some(appleId))
    userSupe ? user
    complete("OK")
  }
}