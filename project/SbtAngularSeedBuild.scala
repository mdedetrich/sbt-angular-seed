import sbt._
import Keys._

object SbtAngularSeedBuild extends Build {

  lazy val sbtAngularSeed = Project("sbt-angular-seed",file("."),settings = Defaults.defaultSettings ++
    Seq(
      name := "sbt-angular-seed",
      version := "1.0.0",
      organization := "com.mdedetrich",
      scalaVersion := "2.10.3",
      sbtPlugin := true,
      libraryDependencies ++= Seq(
        "org.json4s" %% "json4s-jackson" % "3.2.8"
      )
    )

  )
}