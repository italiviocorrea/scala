package eti.italiviocorrea.ufs.repository

import eti.italiviocorrea.ufs.models.{Schemas, Uf}
import eti.italiviocorrea.ufs.quill.DBContext

import scala.concurrent.{ExecutionContext, Future}

class UfsRepository (override val ctx: DBContext)(implicit ec: ExecutionContext) extends Schemas{
  import ctx._

  def buscarTodos(): Future[List[Uf]] = {
    def query = quote {
      this.ufs.sortBy(p => p.codigo)
    }
    run(query)
  }

  def buscarPorCodigo(codigo: Int): Future[Option[Uf]] = {

    def query = quote {
      this.ufs.filter(_.codigo == lift(codigo))
    }

    run(query).map(_.headOption)
  }

  def remover(codigo: Int): Future[Long] = {
    def query = quote {
      ufs.filter(_.codigo == lift(codigo)).delete
    }
    run(query)
  }

  def update(codigo: Int, uf: Uf): Future[Int] = {
    def query= quote {
      ufs.filter(_.codigo == lift(codigo)).update(lift(uf)).returning(_.codigo)
    }
    run(query)
  }

  def save(uf: Uf): Future[Int] = {
    def query = quote {
      ufs.insert(lift(uf)).returning(_.codigo)
    }
    run(query)
  }
}
