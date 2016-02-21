package com.ipsumllc.highfive

/**
 * Created by tgrayson on 2/19/16.
 */

import com.ipsumllc.highfive.services.SlapServices
import spray.routing._

trait InviteService extends HttpService with SlapServices {

  def body(inviteCode: String) = layout(
  <div class="container">
    <h1>
      Congratulations!
      <span class="small">You made it bro.</span>
    </h1>
    <p>You were pre-approved for iFive Club.  This never happens. You must be someone very special.</p>
    <p>You're about to significantly increased your <strong>high five</strong> radius with the high five elite.
      Just a few more quick steps...
    </p>
    <ol class="steps">
      <li>
        <h2>Download the App</h2>
        <p>If you haven't downloaded the app yet, you can do it
          <a href="#" onclick="alert('I.O.U.')">here</a>.</p>
      </li>
      <li>
        <h2>Join the Club</h2>
        <form method="GET" action="hi5://invite">
          <div class="input-group">
            <div class="input-group-addon">Mobile:</div>
            <input name="invite" value={inviteCode} class="form-control" placeholder="Your Name" readonly="readonly" />
          </div>
          <div class="input-group">
            <div class="input-group-addon">Name:</div>
            <input name="name" class="form-control" placeholder="Your Name"/>
          </div>
          <div class="actions">
            <input type="submit" value="Join" class="btn btn-success"/>
          </div>
        </form>
        <ul>
          <li>You must have the app installed.</li>
          <li>You must join from your phone or mobile device.</li>
        </ul>
      </li>
    </ol>
  </div>
  )

  val inviteRoutes =
      pathPrefix("invite") {
        path(Segment) { inviteCode =>
          complete( body(inviteCode) )
        }
      } ~
      pathPrefix("invite") {
        parameters("contact") { (inviteCode) =>
          complete( body(inviteCode) )
        }
      }
}
