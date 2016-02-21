package com.ipsumllc.highfive

/**
 * Created by tgrayson on 2/19/16.
 */

import com.ipsumllc.highfive.services.SlapServices
import spray.routing._

trait StaticAssetService extends HttpService with SlapServices {

  val staticAssetRoutes =
      pathPrefix("images") {
        getFromDirectory("public")
      } ~
      pathPrefix("css") {
        getFromDirectory("public/css")
      }
}
