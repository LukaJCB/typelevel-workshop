package org.typelevel.workshop

object model {
  case class User(id: Int, username: String, email: String)
  case class Project(id: Int, name: String, description: String, owner: User)
}
