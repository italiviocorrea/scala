name := "ufs-akka"

version := "0.1"

scalaVersion := "2.12.11"

resolvers += Resolver.jcenterRepo


val akkaHttp = "10.1.12"
val akka = "2.6.5"
val circe = "0.12.3"
val macwire = "2.3.5"
val quill = "3.5.1"
val jacksonVersion = "2.11.0"
val swaggerVersion = "2.1.1"

libraryDependencies ++= Seq(
  "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",
  "com.github.swagger-akka-http" %% "swagger-akka-http" % "2.0.5",
  "com.github.swagger-akka-http" %% "swagger-scala-module" % "2.1.0",
  "com.github.swagger-akka-http" %% "swagger-enumeratum-module" % "2.0.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
  "pl.iterators" %% "kebs-spray-json" % "1.7.1",
  "io.swagger.core.v3" % "swagger-core" % swaggerVersion,
  "io.swagger.core.v3" % "swagger-annotations" % swaggerVersion,
  "io.swagger.core.v3" % "swagger-models" % swaggerVersion,
  "io.swagger.core.v3" % "swagger-jaxrs2" % swaggerVersion,

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