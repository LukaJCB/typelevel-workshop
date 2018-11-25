package org.typelevel.workshop.http

import cats.effect._
import cats.implicits._
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.HttpService
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.typelevel.workshop.algebra.UserRepository

class UserService[F[_]: Sync: UserRepository] extends Http4sDsl[F] {

  case class CreateUserRequest(name: String, email: String)

  val service: HttpService[F] = HttpService[F] {

    case req @ POST -> Root => for {
      createUser <- req.as[CreateUserRequest]
      userOption <- UserRepository[F].addUser(createUser.name, createUser.email)
      result <- userOption match {
        case Some(user) => Created(user)
        case None => BadRequest("User already exists".asJson)
      }
    } yield result
  }
}
