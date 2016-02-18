package com.ipsumllc.highfive.slappers

import com.notnoop.apns.{ApnsService, APNS}
import spray.http.Uri
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * Created by tgrayson on 7/23/14.
 *
 * This contains the interface for talking to the "real delivery service."
 * This could be SMS, Push notification, or whatever.  That is the only concern.
 */

object Slapper {
  val domain = "http://ipsumllc.com/hi5/"
  val APNSCertPath = "/etc/highfive/hi5.p12"
  val APNSCertPassword = "high5"

  val sound = "highfive-0.m4a"

  case object Ok
}

trait Slapper  {
  import Slapper._

  val service: ApnsService = APNS.newService.withCert(APNSCertPath, APNSCertPassword)
                               .withSandboxDestination.build

  def sendSlap(m: Slap) {

    val aps = Map(
      "alert" -> s"${m.from.name} High Five!",
      "badge" -> 1.toString,
      "sound" -> "highfive-0.m4a"
    )
    val slapFacts = Map(
      "id"   -> m.from.contact.string.substring(2),
      "from" -> m.from.name,
      "to"   -> m.to.contact.string.substring(2),
      "name" -> m.to.name,
      "jerk" -> m.intensity.toString
    )

    //APNS.newPayload().alertBody("Can't be simpler than this!").build();

    val payload = Map(

      "aps" -> aps,
      "slap" -> slapFacts

    ).toJson.toString()

    println(m.from)
    println(m.to)
    println(s"attempting to send a slap ${payload}")

    m.to.appleId.map {
      case appleId =>
        println(s"Sending to: ${appleId}")
        push(appleId, payload)
    }

    //TODO consider push queue https://code.google.com/p/javapns/wiki/PushNotificationAdvanced
  }

  def push(appleId: String, payload: String): Unit = {
    val response = service.push(appleId, payload)
    println(response)
  }

}

