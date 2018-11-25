package workshop

import java.io.{File, BufferedReader, FileReader}
import collection.JavaConverters._
import cats.effect._
import cats.implicits._
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.global

object effect {


  // IO.apply is used to capture synchronous side effects
  def printLine(s: String): IO[Unit] = IO(println(s))


  def sideEffectinglongRunningComputation(callback: Int => Unit): Unit = {
    Thread.sleep(3000) //Simulate computation; BAD this blocks!

    callback(42)
  }

  // IO.async is used to capture asynchronous side effects
  def safeLongRunningComputation: IO[Int] =
    IO.async { k =>
      // Right here signals that everything went well
      sideEffectinglongRunningComputation(i => k(Right(i)))
    }


  // Build a function that takes a Future as a by-name argument and returns an IO
  def toIO[A](future: => Future[A])(implicit ec: ExecutionContext): IO[A] = ???

  implicit val timer: Timer[IO] = IO.timer(global)

  implicit val ctx: ContextShift[IO] = IO.contextShift(global)

  // User `IO#start` to implement a "fire-and-forget" function
  def fireAndForget[A](io: IO[A]): IO[Unit] = ???


  // Use IO.sleep to create IO values that return after a given amount of time (this won't block)
  IO.sleep(5.seconds)

  // Write a function that starts an `IO` and cancels it if it's not completed after 2 seconds
  def timeoutAfter[A](io: IO[A]): IO[A] = ???



  // `Resource` automatically releases resources such as file handles in the reverse order of acquisition
  def readFile(file: File): Resource[IO, List[String]] = {
    val acquire: IO[BufferedReader] =
      printLine(s"Acquring file: $file") *> IO(new BufferedReader(new FileReader(file)))

    Resource.make(acquire)(in => printLine(s"Releasing file handle of: $file") *> IO(in.close()))
      .map(_.lines().iterator().asScala.toList)
  }


  // Try reading 3-4 different files after each other and observe how they get released
  def severalFiles: Resource[IO, List[String]] = ???


  // Next let's run the `Resource` and turn it into an IO[List[String]]
  def severalFilesIO: IO[List[String]] =
    severalFiles.use((list: List[String]) => IO.pure(list))
}
