scalaVersion in ThisBuild := "2.13.4"

name := "twitch"

version := "0.1"

val http4sVersion                   = "0.21.7"
val catsVersion                     = "2.1.1"
val catsEffectVersion               = "2.1.1"
val circeVersion                    = "0.12.2"
val specs2Version                   = "4.7.0"
val scalaTestVersion                = "3.0.8"
val pureConfigVersion               = "0.14.0"
val fs2Version                      = "2.1.0"
val doobieVersion                   = "0.9.0"
val fuuidVersion                    = "0.4.0"
val http4sJwtVersion                = "0.0.5"
val testContainersPostgresqlVersion = "1.10.7"
val testContainersVersion           = "0.38.8"
val flyWayVersion                   = "6.0.8"
val enumeratumScalacheckVersion     = "1.5.16"
val log4CatsVersion                 = "1.1.1"
val tsecVersion                     = "0.2.0"

libraryDependencies ++= Seq(
  "org.http4s"            %% "http4s-dsl"                      % http4sVersion,
  "org.http4s"            %% "http4s-blaze-server"             % http4sVersion,
  "org.http4s"            %% "http4s-blaze-client"             % http4sVersion,
  "org.typelevel"         %% "cats-core"                       % catsVersion,
  "org.typelevel"         %% "cats-effect"                     % catsEffectVersion,
  "io.circe"              %% "circe-generic"                   % circeVersion,
  "io.circe"              %% "circe-generic-extras"            % circeVersion,
  "io.circe"              %% "circe-parser"                    % circeVersion,
  "io.circe"              %% "circe-refined"                   % circeVersion,
  "io.circe"              %% "circe-config"                    % "0.7.0",
  "org.http4s"            %% "http4s-circe"                    % http4sVersion,
  "org.scalatest"         %% "scalatest"                       % scalaTestVersion % Test,
  "org.specs2"            %% "specs2-core"                     % specs2Version % Test,
  "org.specs2"            %% "specs2-matcher"                  % specs2Version % Test,
  "org.specs2"            %% "specs2-scalacheck"               % specs2Version % Test,
  "com.github.pureconfig" %% "pureconfig"                      % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-http4s"               % pureConfigVersion,
  "co.fs2"                %% "fs2-io"                          % fs2Version,
  "io.chrisdavenport"     %% "log4cats-slf4j"                  % log4CatsVersion,
  "io.chrisdavenport"     %% "log4cats-noop"                   % log4CatsVersion
)
addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")
