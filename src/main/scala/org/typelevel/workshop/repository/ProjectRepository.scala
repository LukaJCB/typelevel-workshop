package org.typelevel.workshop.repository

import org.typelevel.workshop.model.Project
import cats.effect.IO
import cats.implicits._
import doobie.implicits._

object ProjectRepository {

  def findByName(name: String): IO[Option[Project]] =
    sql"""
      SELECT p.id, p.name, p.description, u.id, u.username, u.email
      FROM project p JOIN user u ON p.owner = u.id
      WHERE p.name = $name
    """.query[Project].option.transact(Database.xa)


  def deleteProject(name: String): IO[Unit] = (for {
    projectId <- sql"SELECT id FROM project WHERE name = $name".query[Int].unique
    _ <- sql"DELETE FROM star WHERE projectId = $projectId".update.run
    _ <- sql"DELETE FROM project WHERE id = $projectId".update.run
  } yield ()).transact(Database.xa).attempt.void

}
