package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._


class SeleniumTest extends Specification {

  val app = FakeApplication()
  "Application" should {


    "works from within a browser" in new WithBrowser(webDriver = Helpers.FIREFOX, app = app) {

      running(FakeApplication()) {
        browser.goTo("/")
        browser.$("#tog").getText() must equalTo("Toggle Heatmap")
       


      }
    }
  }
}