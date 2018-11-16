lazy val Http4sVersion     = "0.18.14"
lazy val CirceVersion      = "0.9.3"
lazy val DoobieVersion     = "0.5.3"
lazy val H2Version         = "1.4.196"
lazy val LogbackVersion    = "1.2.3"
lazy val ScalaTestVersion  = "3.0.3"
lazy val ScalaCheckVersion = "1.14.0"

lazy val root = (project in file("."))
  .settings(
    organization := "org.typelevel",
    name := "typelevel-workshop",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.4",
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8"),
    libraryDependencies += "com.github.mpilquist" %% "simulacrum" % "0.14.0",
    scalacOptions := Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-Ypartial-unification",
      "-Ywarn-value-discard"
    ),
    libraryDependencies ++= Seq(
      "org.scalatest"   %% "scalatest"           % ScalaTestVersion  % Test,
      "org.scalacheck"  %% "scalacheck"          % ScalaCheckVersion % Test
    ),
    initialCommands in console := """import workshop.adts._
      import workshop.typeclasses._
      import workshop.abstractions._
      import workshop.model._
      import workshop.monoids._
    """


  )

