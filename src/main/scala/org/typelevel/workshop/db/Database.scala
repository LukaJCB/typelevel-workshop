package org.typelevel.workshop.db

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor

object Database {
  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.h2.Driver", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", ""
  )

  val schemaDefinition: IO[Unit] = List(
    sql"""
      CREATE TABLE user (
        id       INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR UNIQUE,
        email    VARCHAR UNIQUE
      )
    """.update.run,
    sql"""
      CREATE TABLE project (
        id          INT AUTO_INCREMENT PRIMARY KEY,
        name        VARCHAR UNIQUE,
        description VARCHAR,
        owner       INT,
        FOREIGN KEY (owner) REFERENCES user(id)
      )
    """.update.run
  ).traverse_(_.transact(xa))



  val insertions: IO[Unit] = List(
    sql"""
      INSERT INTO user (id, username, email)
      VALUES  (0, 'Luka', 'luka.jacobowitz@gmail.com');
    """.update.run,
    sql"""
      INSERT INTO user (id, username, email)
      VALUES  (1, 'Typelevel', 'info@typelevel.org');
    """.update.run,
    sql"""
      INSERT INTO project (id, name, description, owner)
      VALUES  (0, 'Cats', 'Functional abstractions for Scala', 1);
    """.update.run
  ).traverse_(_.transact(xa))
}
