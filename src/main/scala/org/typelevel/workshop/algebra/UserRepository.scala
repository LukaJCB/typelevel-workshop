package org.typelevel.workshop.algebra

import org.typelevel.workshop.model.User

trait UserRepository[F[_]] {
  def addUser(username: String, email: String): F[Option[User]]
}

object UserRepository {
  def apply[F[_]: UserRepository]: UserRepository[F] = implicitly
}