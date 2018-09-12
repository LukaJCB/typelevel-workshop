package org.typelevel.workshop.http

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.HttpService
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityCodec._
import org.typelevel.workshop.repository.UserRepository

object UserService {

  case class CreateUserRequest(name: String, email: String)

  val service: HttpService[IO] = HttpService[IO] {

    case req @ POST -> Root => for {
      createUser <- req.as[CreateUserRequest]
      userOption <- UserRepository.addUser(createUser.name, createUser.email)
      result <- userOption match {
        case Some(user) => Created(user)
        case None => BadRequest("User already exists".asJson)
      }
    } yield result
  }
}
