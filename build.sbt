ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "weaver-cats-retry",
    libraryDependencies ++= Seq(
      "com.disneystreaming" %% "weaver-cats" % "0.8.4",
      "com.disneystreaming" %% "weaver-scalacheck" % "0.8.4",
      "com.github.cb372" %% "cats-retry" % "3.1.0",
    ),
      testFrameworks +=new TestFramework("weaver.framework.CatsEffect")

)
