package com.ipsumllc.highfive.slappers

import akka.actor.{PoisonPill, Actor}
import scalaj.http.Http
import com.ipsumllc.highfive.slappers.Slapper.Ok
import javapns.Push
import scala.io.Source
import javapns.notification.PushNotificationPayload
import javapns._
import scala.util.parsing.json.JSONObject

/**
 * Created by tgrayson on 7/23/14.
 *
 * This contains the interface for talking to the "real delivery service."
 * This could be SMS, Push notification, or whatever.  That is the only concern.
 */

object Slapper {
  val domain = "http://ipsumllc.com/hi5/"
  val phrase = "high5"
  val p12key = "hi5.p12"

  case object Ok
}

trait Slapper  {
  import Slapper._
  //config

  def invite(m: Slap) {

  }

  def sendSlap(m: Slap) {
    val prod = false

    println(jsonPayload(m).toString())

    //println(Push.test(p12key, "high5", true, m.to.appleId.get))
    //Push.alert(message, p12key, phrase, prod, m.to.appleId.get)
    val payload = PushNotificationPayload.fromJSON( jsonPayload(m).toString() )
//    val payload = PushNotificationPayload.complex()
//    payload.addAlert(message)
//    payload.addSound(sound)
//    payload.addBadge(1)
//    payload.addCustomDictionary( "slap", jsonPayload(m) )


//    payload.addCustomDictionary( "from", m.from.name          )
//    payload.addCustomDictionary( "to", m.to.contact.string    )
//    payload.addCustomDictionary( "name", m.to.name            )
//    payload.addCustomDictionary( "jerk", m.intensity.toString )

    Push.payload(payload, p12key, phrase, prod, m.to.appleId.get)
    //TODO consider push queue https://code.google.com/p/javapns/wiki/PushNotificationAdvanced
  }

  def jsonPayload(m: Slap): JSONObject = {
    val message = s"${m.from.name} High Five!"
    val sound = "highfive-0.m4a"

    JSONObject( Map(
      "aps"  -> JSONObject( Map(
        "alert" -> message,
        "sound" -> sound,
        "badge" -> 1
      ) ),
      "slap" -> JSONObject( Map(
          "id"   -> m.from.contact.string,
          "from" -> m.from.name,
          "to"   -> m.to.contact.string,
          "name" -> m.to.name,
          "jerk" -> m.intensity.toString
        ) )
      )
    )
  }

  def request(m: Slap) = {
    var http = Http(domain).
      param("to", m.to.contact.string).
      param("from", m.from.contact.string).
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
}

