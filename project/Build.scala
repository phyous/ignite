import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "ignite"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "org.twitter4j" % "twitter4j-async" % "3.0.3",
    "org.twitter4j" % "twitter4j-stream" % "3.0.3",
    "org.twitter4j" % "twitter4j-media-support" % "3.0.3",
    "com.twitter" % "util-eval_2.10" % "6.3.0",
    "com.twitter" % "util-collection_2.10" % "6.3.0",
    "com.twitter" % "util-core_2.10" % "6.3.0",
    "com.twitter" % "util-logging_2.10" % "6.3.0",
    "org.seleniumhq.selenium" % "selenium-java" % "2.33.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
