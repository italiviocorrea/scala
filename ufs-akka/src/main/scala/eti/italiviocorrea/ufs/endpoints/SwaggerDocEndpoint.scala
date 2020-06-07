package eti.italiviocorrea.ufs.endpoints

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import com.typesafe.config.ConfigFactory

class SwaggerDocEndpoint extends SwaggerHttpService{

  val config = ConfigFactory.load()
  val api = config.getConfig("api")
  val host_name  = api.getString("host")
  val host_port  = api.getString("port")
  val api_docs = api.getString("swagger.docs")

  override val apiClasses: Set[Class[_]] = Set(classOf[UfsEndpoint])
  override val host = host_name+":"+host_port
  override val apiDocsPath = api_docs

  override val info = Info(version = "1.0", title ="API-UFS-AKKA")
  //override val externalDocs = Some(new ExternalDocumentation().description("Core Docs").url("http://acme.com/docs"))
  //override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  //override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")

}
