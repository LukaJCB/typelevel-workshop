package org.typelevel.workshop.repository

import org.typelevel.workshop.model.User
import cats.effect.IO
import doobie.implicits._

object UserRepository {

  def addUser(username: String, email: String): IO[Option[User]] =
    sql"INSERT INTO user (username, email) VALUES ($username, $email)"
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .attemptSql
      .map(_.toOption.map(id => User(id, username, email)))
      .transact(Database.xa)

}
