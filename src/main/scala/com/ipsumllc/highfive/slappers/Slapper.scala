package com.ipsumllc.highfive.slappers

import akka.actor.{PoisonPill, Actor}
import scalaj.http.Http
import com.ipsumllc.highfive.slappers.Slapper.Ok

/**
 * Created by tgrayson on 7/23/14.
 */
object Slapper {
  case object Ok
}

trait Slapper  {

  val domain = "http://ipsumllc.com/hi5/"

  def invite(m: Slap) {

  }

  def sendSlap(m: Slap) {
    println( "Slap Response: " + request(m).responseCode )
//    val result = Http.postData("http://example.com/url", """{"id":"12","json":"data"}""")
//      .header("Content-Type", "application/json")
//      .header("Charset", "UTF-8")
//      .option(HttpOptions.readTimeout(10000))
//      .responseCode
  }

  def request(m: Slap) = {
    var http = Http(domain).
      param("to", m.to.contact).
      param("from", m.from.contact).
      param("name", m.from.name).
      param("jerk", m.intensity.toString)

    if( m.to.appleId != None ) {
      http = http.param("appleId", m.to.appleId.get)
    } else {
      http
    }
    println("URL:" + http.getUrl.toString)
    http
  }
//    param("from", m.from.name).
//    param("ferocity", m.intensity.toString).
//    param("to", m.to.contact)
}

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
    }
  }
}
