package com.ipsumllc.highfive.util

/**
 * Created by tgrayson on 8/18/14.
 */
object ContactNormalizer {
  val BasicFormat = "^[1]{0,1}([0-9]{10})$".r
  val EmailFormat = "^(\\d{10,11})@.*".r
  val TooShort    = "^(\\d{0,9})".r
  val TooLong     = "^(\\d{12,999})".r
  val EryBudy     = ".*".r

  def telephone(phone: String): String = {
    phone match {
      //case EryBudy(m) => throw new Exception("WORKS!")
      case TooShort(m) => throw new InvalidTelephone("Too Short")
      case TooLong(m)  => throw new InvalidTelephone("Too Long")

      case BasicFormat(m) => s"+1$m"
      case EmailFormat(m) => telephone( m.split("@")(0) )
      case m: String if m.length > 12 => telephone( numericsOnly(m.replaceAll("[^0-9]","")) )
      case m => m.toString
    }
  }

  def numericsOnly(s: String) = s.replaceAll("[^\\d.]","")

  class InvalidTelephone(m: String) extends Exception(s"Invalid Telephone String: $m")
}