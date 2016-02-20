package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import com.ipsumllc.highfive.users.User
import com.ipsumllc.highfive.services.SlapServices
import akka.util.Timeout

import scala.concurrent.{Future, Await, ExecutionContext}
import ExecutionContext.Implicits.global
import com.ipsumllc.highfive.slappers.Slap
import com.typesafe.config.ConfigFactory
import com.ipsumllc.highfive.util.ContactNormalization

// mightyslap.com
// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class WebServiceActor extends Actor
  with WebService
  with SlapServices {
  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute
  )
}

// this trait defines our service behavior independently from the service actor
trait WebService extends HttpService with SlapServices
  with RegisterService
  with InviteService
  with ContactNormalization
  {

  //MEHHH can we not be asking?
  import scala.concurrent.duration._
  import akka.pattern.ask

  val config = ConfigFactory.load()
  val inviteUrl = config.getString("links.invite")
  val download  = config.getString("links.download")

  val myRoute =
  path("") {
    get {
      respondWithMediaType(`text/html`) {
        complete( io.Source.fromFile("index.html").mkString )
      }
    }
  } ~
  pathPrefix("slap") {
    pathPrefix(Segment) { from =>
      pathPrefix(Segment) { to =>
        path(DoubleNumber) { jerk =>
          implicit val timeout = Timeout(10 seconds)
          val fU = User(from, None, None)
          val tU = User(to, None, None)

          val response: Future[Any] = for {
            r  <- userSupe ? Slap(tU, jerk, fU)
          } yield r

          val out = Await.result(response, 10 seconds)
          complete(out.toString)
        }
      }
    }
  } ~
  registerRoutes ~
  inviteRoutes
}
