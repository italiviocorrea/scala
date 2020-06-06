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

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

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
