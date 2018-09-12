package org.typelevel.workshop.repository

import org.typelevel.workshop.model.{Project, User}
import cats.effect.IO
import cats.implicits._
import doobie.implicits._

object ProjectRepository {

  def allProjects: IO[List[Project]] =
    sql"""
      SELECT p.id, p.name, p.description, u.id, u.username, u.email
      FROM project p JOIN user u ON p.owner = u.id
    """.query[Project].to[List].transact(Database.xa)

  def findByName(name: String): IO[Option[Project]] =
    sql"""
      SELECT p.id, p.name, p.description, u.id, u.username, u.email
      FROM project p JOIN user u ON p.owner = u.id
      WHERE p.name = $name
    """.query[Project].option.transact(Database.xa)

  def updateProject(oldName: String, newName: String, newDescription: String): IO[Unit] =
    sql"""
      UPDATE project
      SET name = $newName, description = $newDescription
      WHERE name = $oldName
    """.update.run.void.transact(Database.xa)

  def deleteProject(name: String): IO[Unit] = (for {
    projectId <- sql"SELECT id FROM project WHERE name = $name".query[Int].unique
    _ <- sql"DELETE FROM star WHERE projectId = $projectId".update.run
    _ <- sql"DELETE FROM project WHERE id = $projectId".update.run
  } yield ()).transact(Database.xa).attempt.void

  def stargazers(name: String): IO[List[User]] =
    sql"""
      SELECT u.id, u.username, u.email
      FROM user u
      JOIN star s ON u.id = s.userId
      JOIN project p ON s.projectId = p.id
      WHERE p.name = $name
    """.query[User].to[List].transact(Database.xa)

}
