package org.typelevel.workshop.interpreters

import org.typelevel.workshop.model.User
import org.typelevel.workshop.algebra.UserRepository
import cats.effect.IO
import doobie.implicits._
import org.typelevel.workshop.db.Database

object UserRepositoryIO {
  implicit def userRepoInterpreter: UserRepository[IO] = new UserRepository[IO] {
    def addUser(username: String, email: String): IO[Option[User]] =
      sql"INSERT INTO user (username, email) VALUES ($username, $email)"
        .update
        .withUniqueGeneratedKeys[Int]("id")
        .attemptSql
        .map(_.toOption.map(id => User(id, username, email)))
        .transact(Database.xa)
  }
}
