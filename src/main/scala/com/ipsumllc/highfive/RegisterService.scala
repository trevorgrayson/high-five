package com.ipsumllc.highfive

/**
 * Created by tgrayson on 2/19/16.
 */

import akka.util.Timeout
import com.ipsumllc.highfive.services.SlapServices
import com.ipsumllc.highfive.users.{NewUser, User}
import com.ipsumllc.highfive.util.ContactNormalization
import spray.routing._

import scala.concurrent.Await

trait RegisterService extends HttpService
  with SlapServices
  with ContactNormalization {

  import akka.pattern.ask

  import scala.concurrent.duration._

  val registerRoutes =
    path("register") {
      parameters("invite", "name" ?, "deviceId" ?) { (invite, name, deviceId) =>
        println(s"Registering ${invite} as ${name} (${deviceId}")
        val response = Await.result( userSupe ? NewUser(invite, name, deviceId), 3 seconds)
        complete( response.toString )
      }
    } ~
    pathPrefix("register") {
      path(Segment) { invite =>
        println(s"Registering for ${invite}")
        val response = Await.result( userSupe ? NewUser(invite), 3 seconds)
        complete( response.toString )
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
    //@deprecated
    pathPrefix("users") {
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
