package com.ipsumllc.highfive.services

import akka.actor.{Props}
import akka.util.Timeout
import com.ipsumllc.highfive.slappers.{SlapMaster}
import com.ipsumllc.highfive.users.UserSupe
import spray.routing.HttpService

import scala.xml.Elem

/**
 * Created by tgrayson on 8/21/14.
 */
trait SlapServices {
  this: HttpService =>

  import scala.concurrent.duration._
  implicit val timeout = Timeout(3 seconds)

  val slapper  = actorRefFactory.actorOf(Props(new SlapMaster), "slapper")
  val userSupe = actorRefFactory.actorOf(Props(new UserSupe),  "userSupe")

  def layout(body: Elem) = {
    <html>
      <head>
        <title>iFive.Club</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
        <link rel="stylesheet" href="http://necolas.github.io/normalize.css/"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="/css/ifive-club.css"/>
        <script src="http://code.jquery.com/jquery-1.12.0.min.js"></script>
      </head>
      <body>
        {body}
      </body>
    </html>
  }
}