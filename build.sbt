val scala3Version = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-http-caliban",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.github.ghostdogpr" %% "caliban" % "0.10.1",
      "dev.zio" %% "zio" % "1.0.9",
      "io.d11" %% "zhttp" % "1.0.0.0-RC17",
      "com.github.ghostdogpr" %% "caliban-zio-http"   % "0.10.1",
      "io.circe" %% "circe-core" % "0.14.1",
      "io.circe" %% "circe-generic" % "0.14.1",
      "io.circe" %% "circe-parser" % "0.14.1"
    )
  )
