import akka.http.scaladsl.Http
import eti.italiviocorrea.ufs.modules.AllModules

import scala.util.{Failure, Success}

object Application extends App {
  //with SwaggerSite{
//  val config = ConfigFactory.load()
//  val api = config.getConfig("api")
//  val pathPrefixUFs = api.getString("ufs.pathPrefix")
//  println(pathPrefixUFs)

  val modules = new AllModules

  import modules._

//  val routes = swaggerSiteRoute ~ modules.endpoints.routes

  Http().bindAndHandle(modules.endpoints.routes, "0.0.0.0", 8080).onComplete {
    case Success(b) => system.log.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
    case Failure(e) => system.log.error(s"could not start application: {}", e.getMessage)
  }
}
