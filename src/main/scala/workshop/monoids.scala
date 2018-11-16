package workshop

import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode
import simulacrum.typeclass
import workshop.typeclasses._
import workshop.abstractions.Monoidal
import workshop.typeclasses.Monoid.ops._
import workshop.monoids.Category.ops._
import workshop.monoids.Profunctor.ops._
import workshop.monoids.Monad.ops._

import scala.util.Try
import scala.concurrent.Future
import scala.io.StdIn

object monoids {

  // Additive Monoidal

  @typeclass trait AddMonoidal[F[_]] extends Functor[F] {
    def sum[A, B](fa: F[A], fb: F[B]): F[Either[A, B]]

    def zero[A]: F[A]

    def combineK[A](x: F[A], y: F[A]): F[A] =
      map(sum(x, y))(_.merge)
  }

  // Category

  @typeclass trait Category[F[_, _]] {
    def compose[A, B, C](fab: F[A, B], fbc: F[B, C]): F[A, C]

    def identity[A]: F[A, A]

    def >>>[A, B, C](fab: F[A, B], fbc: F[B, C]): F[A, C] = compose(fab, fbc)

    def <<<[A, B, C](fbc: F[B, C], fab: F[A, B]): F[A, C] = compose(fab, fbc)
  }

  implicit def categoryFunction: Category[Function1] = ???

  implicit def monoidEndoCategory[F[_, _]: Category, A]: Monoid[F[A, A]] = ???

  def plusOne: Int => Int = _ + 1

  def times3: Int => Int = _ * 3

  def plusOneTimes3: Int => Int = plusOne |+| times3


  def plusOneTimes3ToString: Int => String = ???


  // Different Category instances
  case class OptionFunction[A, B](apply: A => Option[B])

  case class EffectFunction[A, B](apply: A => B)


  implicit def categoryOptionFunction: Category[OptionFunction] = new Category[OptionFunction] {
    def identity[A]: OptionFunction[A, A] = ???

    def compose[A, B, C](fab: OptionFunction[A, B], fbc: OptionFunction[B, C]): OptionFunction[A, C] =
      OptionFunction { a =>
        fab.apply(a) match {
          case Some(b) => fbc.apply(b)
          case None => None
        }
      }
  }

  implicit def categoryEffectFunction: Category[EffectFunction] = ???


  // We can define real life synchronous programs without breaking referential transparency using EffectFunction

  trait Program {
    def program: EffectFunction[List[String], Unit]

    def main(args: Array[String]): Unit =
      program.apply(args.toList)
  }

  // Profunctor

  @typeclass trait Profunctor[F[_, _]] {
    def dimap[A, B, C, D](fac: F[B, C])(f: A => B)(g: C => D): F[A, D]

    def rmap[A, B, C](fab: F[A, B])(f: B => C): F[A, C] = dimap(fab)(identity[A])(f)

    def lmap[A, B, C](fbc: F[B, C])(f: A => B): F[A, C] = dimap(fbc)(f)(identity)
  }

  implicit def profunctorFunction: Profunctor[Function1] = new Profunctor[Function1] {
    def dimap[A, B, C, D](fac: B => C)(f: A => B)(g: C => D): A => D =
      f >>> fac >>> g
  }

  implicit def profunctorEffectFunction: Profunctor[EffectFunction] = ???

  implicit def profunctorOptionFunction: Profunctor[OptionFunction] = ???



  // Now try to define an EffectFunction that prompts you to type your name,
  // then reads your name from stdin and outputs a greeting with your name.
  // To do so, you can use the `readLine` and `printLine` functions from `util`.
  def consoleProgram = ???


  // We can define functions that might fail with a value

  case class FailFunction[A, B](apply: A => Either[Throwable, B])

  implicit def categoryFailFunction: Category[FailFunction] = ???

  implicit def profunctorFailFunction: Profunctor[FailFunction] = ???


  trait FailProgram {
    def program: FailFunction[List[String], Unit]

    def main(args: Array[String]): Unit =
      program.apply(args.toList) match {
        case Left(t) => throw t
        case _ => ()
      }
  }

  // Next try to define a FailFunction that reads a file name from stdin, then reads from that file and prints out its content
  // You can try using the `data/test.txt` file.
  def fileProgram = ???









  // Tasks

  type Task[A] = FailFunction[Unit, A]

  def newCompose[A, B](ta: Task[A])(f: FailFunction[A, B]): Task[B] = ???


  type OptionTask[A] = OptionFunction[Unit, A]

  def optionCompose[A, B](ta: OptionTask[A])(f: OptionFunction[A, B]): OptionTask[B] = ???




  // Monad

  @typeclass trait Monad[F[_]] extends Monoidal[F] {
    def flatMap[A, B](fa: /* Unit => */ F[A])(f: A => F[B]): /* Unit => */ F[B]

    def flatten[A](ffa: F[F[A]]): F[A] = flatMap(ffa)(identity)

    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
      flatMap(fa)(a => map(fb)(b => (a, b)))

    override def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(a => pure(f(a)))
  }

  implicit def monadOption: Monad[Option] = ???

  implicit def monadTask: Monad[Task] = ???

  implicit def monadEither[E]: Monad[Either[E, ?]] = ???


  def composeMonadFunctions[F[_]: Monad, A, B, C](x: A => F[B], y: B => F[C]): A => F[C] = ???



  // Kleisli

  case class Kleisli[F[_], A, B](apply: A => F[B])

  implicit def categoryKleisli[F[_]: Monad]: Category[Kleisli[F, ?, ?]] = ???

  implicit def profunctorKleisli[F[_]: Monad]: Profunctor[Kleisli[F, ?, ?]] = ???


  // Now that we have Kleisli, go back and redefine OptionFunction and FailFunction as a special case of Kleisli



  // IO

  case class IO[A](unsafeRun: () => A) {
    def map[B](f: A => B): IO[B] = ???
  }

  implicit def monadIO: Monad[IO] = new Monad[IO] {
    def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = ???

    def unit: IO[Unit] = ???
  }

  // Run both effects one after another, but only return the result of the second
  def ignoreFirstResult[A, B](fa: IO[A], fb: IO[B]): IO[B] = ???

  // Run both effects one after another, but only return the result of the first
  def ignoreSecondResult[A, B](fa: IO[A], fb: IO[B]): IO[A] = ???


  // Reimplement fileprogram using `IO` instead
  // Tip: You can use for-comprehensions, you can try writing a version with and without using for-comprehensions
  def fileProgramIO = ???


  // Use IO to print out each of the names given to this function
  // You can test this using `model.userList1`
  def printAll(names: List[String]): IO[Unit] = ???

}
