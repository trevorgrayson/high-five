package com.ipsumllc.highfive.slappers

import akka.actor.{PoisonPill, Actor}
import org.json.JSONObject
import spray.can.Http
import spray.http.{Uri, HttpRequest}
import spray.json._
import DefaultJsonProtocol._
import javapns.Push
import javapns.notification.PushNotificationPayload
import javapns._
import spray.http._
import HttpMethods._

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

  def jsonPayload(m: Slap): String = {
    val message = s"${m.from.name} High Five!"
    val sound = "highfive-0.m4a"

    Map(
      "aps"  -> Map[String,String](
        "alert" -> message,
        "sound" -> sound,
        "badge" -> 1.toString
      ).toJson,
      "slap" -> Map(
          "id"   -> m.from.contact.string,
          "from" -> m.from.name,
          "to"   -> m.to.contact.string,
          "name" -> m.to.name,
          "jerk" -> m.intensity.toString
      ).toJson
    ).toJson.toString
  }

  def request(m: Slap) = {
    var uri = Uri(domain)
    uri.withQuery(Map(
      "to" -> m.to.contact.string,
      "from" -> m.from.contact.string,
      "name" -> m.from.name,
      "jerk" -> m.intensity.toString
    ))

    if( m.to.appleId != None ) {
      uri.withQuery(Map("appleId" -> m.to.appleId.get))
    } else {
      uri
    }
  }
}

