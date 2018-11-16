package workshop

import workshop.monoids.{Monad, IO}

object tagless {

  trait Console[F[_]] {
    def printLine(s: String): F[Unit]
    def readLine: F[String]
  }

  object Console {
    def apply[F[_]](implicit ev: Console[F]): Console[F] = ev
  }

  // Rewrite the `monoids.consoleProgram` from earlier using tagless final style
  def consoleProgram[F[_]: Monad: Console]: F[Unit] = ???


  //create a Console interpreter for IO
  implicit def consoleIO: Console[IO] = ???

  type Id[A] = A

  //create a Console interpreter for Id that does nothing for printing and returns a static string for reading
  implicit def consoleId: Console[Id] = ???


  // Create a Tagless algebra for accesing the file system
  trait FileSystem[F[_]]

  object FileSystem {
    def apply[F[_]](implicit ev: FileSystem[F]): FileSystem[F] = ev
  }


  // Rewrite the `monoids.fileProgram` from earlier using tagless final style
  def fileProgram = ???
}
