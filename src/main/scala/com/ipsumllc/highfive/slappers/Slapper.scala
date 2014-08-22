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

  def sendSlap(m: Slap) {
      request(m).responseCode
//    val result = Http.postData("http://example.com/url", """{"id":"12","json":"data"}""")
//      .header("Content-Type", "application/json")
//      .header("Charset", "UTF-8")
//      .option(HttpOptions.readTimeout(10000))
//      .responseCode
  }

  def request(m: Slap) = Http(domain).
    param("from", m.from.name).
    param("ferocity", m.intensity.toString).
    param("to", m.to.contact)
}