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
  implicit val timeout = Timeout(3 seconds)

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
    path(Segment) { invite =>
      val inviteUrl = s"hi5://invite/$invite"
      complete(
<html>
  <head>
    <title>Welcome to the party.</title>
  </head>
  <body>
    <h1>Nooo way!</h1>
    <p>You made it bro.  You're about to significantly increased your high five radius.</p>
    <p>Just a few more quick steps...</p>
    <ol>
      <li>Download the App. [You must know Trevor]</li>
      <li><a href={inviteUrl}>Accept this invitation</a>. (You must touch from the device, and the app must be installed.)</li>
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