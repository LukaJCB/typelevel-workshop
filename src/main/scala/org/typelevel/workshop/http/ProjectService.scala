package org.typelevel.workshop.http

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.HttpService
import org.http4s.dsl.io._
import org.http4s.circe.CirceEntityCodec._
import org.typelevel.workshop.repository.ProjectRepository

object ProjectService {

  case class UpdateProjectRequest(newName: String, newDescription: String)

  val service: HttpService[IO] = HttpService[IO] {

    case GET -> Root =>
      ProjectRepository.allProjects.flatMap(projects => Ok(projects))

    case GET -> Root / name =>
      ProjectRepository.findByName(name).flatMap {
        case Some(project) => Ok(project)
        case None => NotFound(s"No project found: $name".asJson)
      }

    case req @ PUT -> Root / name => for {
      updateProject <- req.as[UpdateProjectRequest]
      _ <- ProjectRepository.updateProject(name, updateProject.newName, updateProject.newDescription)
      result <- NoContent()
    } yield result

    case req @ DELETE -> Root / name =>
      ProjectRepository.deleteProject(name).flatMap(_ => NoContent())


    case GET -> Root / name / "stargazers" =>
      ProjectRepository.stargazers(name).flatMap(users => Ok(users))
  }
}
