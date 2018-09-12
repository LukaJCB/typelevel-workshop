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
  case class RenameUserRequest(newName: String)
  case class CreateStarRequest(projectName: String)
  case class CreateProjectRequest(projectName: String, description: String)

  val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root =>
      UserRepository.allUsers.flatMap(users => Ok(users))

    case req @ POST -> Root => for {
      createUser <- req.as[CreateUserRequest]
      userOption <- UserRepository.addUser(createUser.name, createUser.email)
      result <- userOption match {
        case Some(user) => Created(user)
        case None => BadRequest("User already exists".asJson)
      }
    } yield result

    case GET -> Root / username =>
      UserRepository.findByName(username).flatMap {
        case Some(user) => Ok(user)
        case None => NotFound(s"No user found: $username".asJson)
      }

    case req @ PUT -> Root / username => for {
      renameUser <- req.as[RenameUserRequest]
      _ <- UserRepository.renameUser(username, renameUser.newName)
      result <- NoContent()
    } yield result

    case req @ DELETE -> Root / username =>
      UserRepository.deleteUser(username).flatMap(_ => NoContent())

    case GET -> Root / username / "projects" =>
      UserRepository.projectsForUser(username).flatMap(projects => Ok(projects))

    case req @ POST -> Root / username / "projects" => for {
      createProject <- req.as[CreateProjectRequest]
      _ <- UserRepository.addProject(username, createProject.projectName, createProject.description)
      result <- Created()
    } yield result

    case GET -> Root / username / "stars" =>
      UserRepository.starredProjectsForUser(username).flatMap(projects => Ok(projects))

    case req @ POST -> Root / username / "stars" => for {
      createStar <- req.as[CreateStarRequest]
      _ <- UserRepository.starProject(username, createStar.projectName)
      result <- Created()
    } yield result

  }
}
