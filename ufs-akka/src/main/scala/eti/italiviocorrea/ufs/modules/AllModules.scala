package eti.italiviocorrea.ufs.modules

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import com.typesafe.config.ConfigFactory
import eti.italiviocorrea.ufs.endpoints.{Endpoints, HealthCheckEndpoint, SwaggerDocEndpoint, SwaggerSite, UfsEndpoint}
import eti.italiviocorrea.ufs.quill.DBContext
import eti.italiviocorrea.ufs.repository.UfsRepository

import scala.concurrent.ExecutionContext

class AllModules extends EndpointModule

trait EndpointModule extends AkkaModule with RepositoryModule {
  lazy val healthCheckEndpoint = wire[HealthCheckEndpoint]
  lazy val ufsEndpoint = wire[UfsEndpoint]
  lazy val swaggerDocEndpoint = wire[SwaggerDocEndpoint]
  lazy val swaggerSite = wire[SwaggerSite]

  lazy val endpoints = wire[Endpoints]
}

trait QuillModule extends ConfigModule {
  lazy val ctx = wire[DBContext]
}

trait RepositoryModule extends AkkaModule with QuillModule {
  lazy val ufsRepository = wire[UfsRepository]
}

trait AkkaModule {
  implicit lazy val system = ActorSystem("api-ufs-akka")
  implicit lazy val materializer = ActorMaterializer()
  implicit lazy val executor: ExecutionContext = system.dispatcher
}

trait ConfigModule {
  lazy val config = ConfigFactory.load()
}
