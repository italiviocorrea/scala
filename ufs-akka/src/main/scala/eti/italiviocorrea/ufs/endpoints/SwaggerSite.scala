package eti.italiviocorrea.ufs.endpoints

import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives
import com.typesafe.config.ConfigFactory

class SwaggerSite extends Directives {

  val config = ConfigFactory.load()
  val api = config.getConfig("api")
  val swagger_path = api.getString("swagger.ui")

  val swaggerSiteRoute: server.Route =
    path("swagger-ui") {
    getFromResource("swagger-ui/index.html")} ~ getFromResourceDirectory("swagger-ui")

}
