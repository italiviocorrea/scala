name := "ufs-akka"

version := "0.1"

scalaVersion := "2.12.11"

resolvers += Resolver.jcenterRepo


val akkaHttp = "10.1.12"
val akka = "2.6.3"
val circe = "0.12.3"
val macwire = "2.3.5"
val quill = "3.5.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % akka,
  "com.typesafe.akka" %% "akka-slf4j" % akka,

  "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",

  "io.circe" %% "circe-generic" % circe,

  "com.softwaremill.macwire" %% "macros" % macwire,
  "com.softwaremill.macwire" %% "util" % macwire,

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "io.getquill" %% "quill-async-postgres" % quill,

  //test libraries
  "org.scalatest" %% "scalatest" % "3.1.2" % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",
  "ru.yandex.qatools.embed" % "postgresql-embedded" % "2.9" % "test",
  "org.postgresql" % "postgresql" % "42.1.4" % "test"
)

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)