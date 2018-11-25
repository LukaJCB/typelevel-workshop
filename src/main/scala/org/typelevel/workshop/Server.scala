package org.typelevel.workshop

import cats.effect.IO
import cats.implicits._
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import org.http4s.server.blaze.BlazeBuilder
import org.typelevel.workshop.http.{ProjectService, UserService}
import org.typelevel.workshop.db.Database
import org.typelevel.workshop.interpreters.ProjectRepositoryIO._
import org.typelevel.workshop.interpreters.UserRepositoryIO._

import scala.concurrent.ExecutionContext.Implicits.global

object Server extends StreamApp[IO] {

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    Stream.eval(Database.schemaDefinition *> Database.insertions) *>
      BlazeBuilder[IO]
        .bindHttp()
        .mountService(new UserService[IO].service, "/users")
        .mountService(new ProjectService[IO].service, "/projects")
        .serve

}
