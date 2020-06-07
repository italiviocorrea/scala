package eti.italiviocorrea.ufs.endpoints

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import eti.italiviocorrea.ufs.models._
import eti.italiviocorrea.ufs.repository._
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import javax.ws.rs.core.MediaType
import javax.ws.rs._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

@Path("/dfe/api/v1/ufs")
class UfsEndpoint(repository: UfsRepository)(implicit ec: ExecutionContext, mat: Materializer) {

  val config = ConfigFactory.load()
  val api = config.getConfig("api")
  val pathPrefixUFs  = api.getString("ufs.pathPrefix")

  val ufsRoutes =
    buscarTodos ~
    incluir ~
    buscarPorCodigo ~
    atualizar ~
    remover ~
    versao

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Buscar todas as UFs",
    description = "Returna todas as UFs cadastradas.",
    method = "GET",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Lista de UF retornada com sucesso",
        content = Array(new Content(schema = new Schema(implementation = classOf[Uf])))),
      new ApiResponse(responseCode = "500", description = "Erro interno no servidor"))
  )
  def buscarTodos =
    pathPrefix(separateOnSlashes(pathPrefixUFs)) {
      pathEndOrSingleSlash {
        get {
          onComplete(repository.buscarTodos()) {
            case Success(ufs) =>
              complete(Marshal(ufs).to[ResponseEntity].map { e => HttpResponse(entity = e, headers = List(Location(s"/dfe/api/v1/ufs/"))) })
            case Failure(e) =>
              complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
          }
        }
      }
    }

  @POST
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Incluir uma nova UF",
    description = "Permite incluir uma nova UF.",
    method = "POST",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Uf])))),
    responses = Array(
      new ApiResponse(responseCode = "201", description = "UF incluida com sucesso",
        content = Array(new Content(schema = new Schema(implementation = classOf[Uf])))),
      new ApiResponse(responseCode = "500", description = "Erro Interno no servidor"))
  )
  def incluir =
    pathPrefix(separateOnSlashes(pathPrefixUFs)) {
      pathEndOrSingleSlash {
        post {
          entity(as[Uf]) { uf =>
            onComplete(repository.save(uf)) {
              case Success(codigo) =>
                complete(HttpResponse(status = StatusCodes.Created, headers = List(Location(s"/dfe/api/v1/ufs/$codigo"))))
              case Failure(e) =>
                complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
            }
          }
        }
      }
    }

  @GET
  @Path("{codigo}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Buscar UF pelo código",
    description = "Permite buscar uma UF informando apenas o código",
    method = "GET",
    parameters = Array(new Parameter(name = "codigo",
      in = ParameterIn.PATH,
      description = "codigo da UF",
      schema = new Schema(`type` = "integer",format = "int32",minimum = "10", maximum = "100",required = true,example = "50"))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "UF encontrada com sucesso",
        content = Array(new Content(schema = new Schema(implementation = classOf[Uf])))),
      new ApiResponse(responseCode = "404", description = "UF não encontrada!"),
      new ApiResponse(responseCode = "500", description = "Erro interno no servidor"))
  )
  def buscarPorCodigo =
    pathPrefix(separateOnSlashes(pathPrefixUFs)) {
      path(IntNumber) { codigo =>
        get {
          onComplete(repository.buscarPorCodigo(codigo)) {
            case Success(Some(ufs)) =>
              complete(Marshal(ufs).to[ResponseEntity].map { e => HttpResponse(entity = e) })
            case Success(None) =>
              complete(HttpResponse(status = StatusCodes.NotFound))
            case Failure(e) =>
              complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
          }
        }
      }
    }

  @PUT
  @Path("{codigo}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Atualizar dados de uma UF",
    description = "Permite modificar os dados de uma UF.",
    method = "PUT",
    parameters = Array(new Parameter(name = "codigo",
      in = ParameterIn.PATH,
      description = "codigo da UF",
      schema = new Schema(`type` = "integer",format = "int32",minimum = "10", maximum = "100",required = true,example = "50"))),
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Uf])))),
    responses = Array(
      new ApiResponse(responseCode = "204", description = "UF atualizada com sucesso",
        content = Array(new Content(schema = new Schema(implementation = classOf[Uf])))),
      new ApiResponse(responseCode = "500", description = "Erro Interno no servidor"))
  )
  def atualizar =
    pathPrefix(separateOnSlashes(pathPrefixUFs)) {
      path(IntNumber) { codigo =>
        put {
          entity(as[Uf]) { uf =>
            onComplete(repository.update(codigo, uf)) {
              case Success(id) =>
                complete(HttpResponse(status = StatusCodes.NoContent, headers = List(Location(s"/dfe/api/v1/ufs/$codigo"))))
              case Failure(e) =>
                complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
            }
          }
        }
      }
    }

  @DELETE
  @Path("{codigo}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(summary = "Excluir a UF pelo código", description = "Permite excluir uma UF informando apenas o código",
    method = "DELETE",
    parameters = Array(new Parameter(name = "codigo",
      in = ParameterIn.PATH,
      description = "codigo da UF",
      schema = new Schema(`type` = "integer",format = "int32",minimum = "10", maximum = "100",required = true,example = "50"))),
    responses = Array(
      new ApiResponse(responseCode = "204", description = "UF excluída com sucesso",
        content = Array(new Content(schema = new Schema(implementation = classOf[Uf])))),
      new ApiResponse(responseCode = "500", description = "Erro interno no servidor"))
  )
  def remover =
    pathPrefix(separateOnSlashes(pathPrefixUFs)) {
      path(IntNumber) { codigo =>
        delete {
          onComplete(repository.remover(codigo)) {
            case Success(id) =>
              complete(HttpResponse(status = StatusCodes.NoContent, headers = List(Location(s"/dfe/api/v1/ufs/"))))
            case Failure(e) =>
              complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
          }
        }
      }
    }

  def versao =
    pathPrefix("api" / "version") {
      get {
        complete(StatusCodes.OK -> "API-UFS-AKKA v1")
      }
    }
}
