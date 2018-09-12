package org.typelevel.workshop.repository

import org.typelevel.workshop.model.{Project, User}
import cats.effect.IO
import cats.data.OptionT
import cats.implicits._
import doobie.implicits._

object UserRepository {

  def addProject(username: String, projectName: String, description: String): IO[Unit] = (for {
    userId <- sql"SELECT id FROM user WHERE username = $username".query[Int].unique
    _ <- sql"INSERT INTO project (name, description, owner) VALUES ($projectName, $description, $userId)".update.run
  } yield ()).transact(Database.xa)

  def addUser(username: String, email: String): IO[Option[User]] =
    sql"INSERT INTO user (username, email) VALUES ($username, $email)"
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .attemptSql
      .map(_.toOption.map(id => User(id, username, email)))
      .transact(Database.xa)

  def renameUser(oldName: String, newName: String): IO[Unit] =
    sql"""
      UPDATE user
      SET username = $newName
      WHERE username = $oldName
    """.update.run.transact(Database.xa).void

  def deleteUser(username: String): IO[Unit] = (for {
    userId <- sql"SELECT id FROM user WHERE username = $username".query[Int].unique
    _ <- sql"DELETE FROM star WHERE userId = $userId".update.run
    _ <- sql"DELETE FROM project WHERE owner = $userId".update.run
    _ <- sql"DELETE FROM user WHERE id = $userId".update.run
  } yield ()).transact(Database.xa).attempt.void

  def starProject(username: String, projectName: String): IO[Option[User]] = (for {
    user <- OptionT(sql"SELECT * FROM user WHERE username = $username".query[User].option)
    projectId <- OptionT(sql"SELECT id FROM project WHERE name = $projectName".query[Int].option)
    _ <- OptionT.liftF(sql"INSERT INTO star (projectId, userId) VALUES ($projectId, ${user.id})".update.run.void)
  } yield user).value.transact(Database.xa)

  def allUsers: IO[List[User]] =
    sql"SELECT * FROM user"
      .query[User].to[List].transact(Database.xa)

  def findByName(username: String): IO[Option[User]] =
    sql"""
      SELECT * FROM user
      WHERE username = $username
    """.query[User].option.transact(Database.xa)

  def projectsForUser(username: String): IO[List[Project]] =
    sql"""
      SELECT p.id, p.name, p.description, u.id, u.username, u.email
      FROM project p JOIN user u ON p.owner = u.id
      WHERE u.username = $username
    """.query[Project].to[List].transact(Database.xa)

  def starredProjectsForUser(username: String): IO[List[Project]] =
    sql"""
      SELECT p.id, p.name, p.description, u.id, u.username, u.email
      FROM project p
      JOIN star s ON p.id = s.projectId
      JOIN user u ON s.userId = u.id
      WHERE u.username = $username
    """.query[Project].to[List].transact(Database.xa)
}
