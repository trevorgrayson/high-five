package com.example

import akka.actor.{Actor}
import spray.routing._
import spray.http._
import MediaTypes._
import com.ipsumllc.highfive.users.{NewUser, User}
import com.ipsumllc.highfive.services.SlapServices
import akka.util.Timeout

import scala.concurrent.{Future, Await, ExecutionContext}
import ExecutionContext.Implicits.global
import com.ipsumllc.highfive.slappers.Slap
import com.typesafe.config.ConfigFactory
import com.ipsumllc.highfive.util.ContactNormalization

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
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait WebService extends HttpService with SlapServices
  with ContactNormalization {
  //MEHHH can we not be asking?
  import scala.concurrent.duration._
  import akka.pattern.ask
  implicit val timeout = Timeout(3 seconds)

  val config = ConfigFactory.load()
  val inviteUrl = config.getString("links.invite")
  val download  = config.getString("links.download")

  val myRoute =
  path("") {
    get { complete("What's up bro?!") }
  } ~
  pathPrefix("register") {
    path(Segment) { invite =>
      val resp = Await.result( userSupe ? NewUser(invite), 3 seconds)
      complete( resp.toString )
    }
  } ~
  pathPrefix("invite") {
    path(Segment) { inviteCode =>
      complete(
<html style="width: 320px">
  <head>
    <title>High Five!!</title>
  </head>
  <body style="width:300px;margin: 0 auto">
    <h1>You made it bro.</h1>
    <p>You're about to significantly increased your <strong>high five</strong> radius.</p>
    <p>Just a few more quick steps...</p>
    <ol>
      <li>
        <a href={download}>Download the App</a><br/>
        This link will only work for you if you have been invited.
      </li>
      <li>
        <a href={inviteUrl + inviteCode}>Accept this invitation</a><br/>
        You must touch from the device, and the app must be installed.
      </li>
    </ol>
  </body>
</html>)
    }
  } ~
  pathPrefix("slap") {
    pathPrefix(Segment) { from =>
      pathPrefix(Segment) { to =>
        path(DoubleNumber) { jerk =>
          implicit val timeout = Timeout(10 seconds)
          val fU = User(from.toString, None, None)
          val tU = User(to.toString, None, None)

          val resp: Future[Any] = for {
            r  <- userSupe ? Slap(tU, jerk, fU)
          } yield r

          val out = Await.result(resp, 10 seconds)
          complete(out.toString)
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
  pathPrefix("users") {
    path(Segment) { contact =>
      implicit val timeout = Timeout(3 seconds)
      val user = User(contact, None, None)
      val user1 = Await.result((userSupe ? user).mapTo[User], 3 seconds)
      complete(user1.toString)
    }
  } ~
  pathPrefix("users") { //@"%@/user/%@/%@/%@", contact, name, deviceToken
    pathPrefix(Segment) { appleId =>
      pathPrefix(Segment) { contact =>
        path(Segment) { name =>
          implicit val timeout = Timeout(3 seconds)
          val user = User(contact,Some(name), Some(appleId))
          val user1 = Await.result((userSupe ? user).mapTo[User], 3 seconds)
          complete(user1.toString)
        }
      } ~
      path(Segment) { contact =>
        implicit val timeout = Timeout(3 seconds)
        val user = User(contact, None, Some(appleId))
        val user1 = Await.result((userSupe ? user).mapTo[User], 3 seconds)
        complete(user1.toString)
      }
    }
  }

}