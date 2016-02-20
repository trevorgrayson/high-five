package com.example

/**
 * Created by tgrayson on 2/19/16.
 */

import spray.routing._
import com.ipsumllc.highfive.services.SlapServices

trait InviteService extends HttpService with SlapServices {

  val inviteRoutes =
      pathPrefix("invite") {
        path(Segment) { inviteCode =>
          complete(
            <html style="width: 320px">
              <head>
                <title>High Five!!</title>
              </head>
              <body style="width:300px;margin: 0 auto">
                <h1>You made it bro.</h1>
                <p>You're about to significantly increased your <strong>high five</strong> radius.</p>
                <p>Just a few more quick steps...</p>
                <ol>
                  <li>
                    <h2>Download the App</h2>
                    <p>If you haven't downloaded the app yet, you can do it here.</p>
                  </li>
                  <li>
                    <h2>Accept, and Get SLAPPIN!</h2>
                    <ul>
                      <li>You must accept this from your phone or device.</li>
                      <li>The app must be installed.</li>
                    </ul>

                    <p>Accept below</p>

                    <form method="GET" action="hi5://invite">
                      <input name="invite" value={inviteCode} type="hidden" />
                      <input name="name" placeholder="Your Name" />
                      <input type="submit" value="Get Slappin'"/>
                    </form>
                  </li>
                </ol>
              </body>
            </html>
          )
        }
      }
}
