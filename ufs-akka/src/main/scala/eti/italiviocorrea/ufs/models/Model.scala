package eti.italiviocorrea.ufs.models

import java.time.LocalDate

import io.circe._
import io.circe.syntax._

case class Uf(codigo: Int, nome: String, sigla: String, iniciovigencia: LocalDate, fimvigencia: Option[LocalDate] = None) {
  require(codigo > 10 && codigo < 100, "Código deve ser entre 10 e 100")
  require(nome != null && nome.nonEmpty, "O nome não pode ser vazio")
  require(sigla != null && sigla.nonEmpty, "Sigla da UF deve ser informado")
  require(iniciovigencia != null,"Data de inicio de vigência é obrigatório")
}

object Uf {
  implicit val encoder: Encoder[Uf] = (a: Uf) => {
    Json.obj(
      "codigo" -> a.codigo.asJson,
      "nome" -> a.nome.asJson,
      "sigla" -> a.sigla.asJson,
      "iniciovigencia" -> a.iniciovigencia.asJson,
      "fimvigencia"-> a.fimvigencia.asJson
    )
  }

  implicit val decoder: Decoder[Uf] = (c: HCursor) => {
    for {
      codigo <- c.downField("codigo").as[Int]
      nome <- c.downField("nome").as[String]
      sigla <- c.downField("sigla").as[String]
      iniciovigencia <- c.downField("iniciovigencia").as[LocalDate]
      fimvigencia <- c.downField("fimvigencia").as[Option[LocalDate]]
    } yield Uf(codigo, nome, sigla, iniciovigencia, fimvigencia)
  }
}

case class Message(message: String)

object Message {
  implicit val encoder: Encoder[Message] = m => Json.obj("message" -> m.message.asJson)
}
