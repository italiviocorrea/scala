package eti.italiviocorrea.ufs.endpoints

import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._

class HealthCheckEndpoint {

  val healthCheckRoute: server.Route = {
    pathPrefix("dfe" / "api" / "v1" / "ufs" / "healtcheck") {
      get { complete("ok")}
    }
  }

}
