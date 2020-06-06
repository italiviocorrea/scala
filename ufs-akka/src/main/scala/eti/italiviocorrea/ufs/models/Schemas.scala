package eti.italiviocorrea.ufs.models

import eti.italiviocorrea.ufs.quill.DBContext

trait Schemas {

  val ctx: DBContext

  import ctx._

  val ufs = quote {
    querySchema[Uf]("ufs",
      _.codigo -> "codigo",
      _.nome -> "nome",
      _.sigla -> "sigla",
      _.iniciovigencia -> "iniciovigencia",
      _.fimvigencia -> "fimvigencia"
    )
  }

}
