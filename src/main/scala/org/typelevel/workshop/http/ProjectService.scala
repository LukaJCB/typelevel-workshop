package org.typelevel.workshop.http

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.HttpService
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityCodec._
import org.typelevel.workshop.repository.ProjectRepository

object ProjectService {

  val service: HttpService[IO] = HttpService[IO] {

    case GET -> Root / name =>
      ProjectRepository.findByName(name).flatMap {
        case Some(project) => Ok(project)
        case None => NotFound(s"No project found: $name".asJson)
      }

    case req @ DELETE -> Root / name =>
      ProjectRepository.deleteProject(name).flatMap(_ => NoContent())

  }
}
