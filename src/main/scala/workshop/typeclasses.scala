package workshop

import workshop.adts._
import workshop.model.rawText
import simulacrum.typeclass
import scala.concurrent.Future
import workshop.typeclasses.Show.ops._
import workshop.typeclasses.Eq.ops._
import workshop.typeclasses.Monoid.ops._
import workshop.typeclasses.Functor.ops._
import scala.concurrent.ExecutionContext.Implicits.global

object typeclasses {

  //Show

  @typeclass trait Show[A] {
    def show(a: A): String
  }

  implicit def showInt: Show[Int] = new Show[Int] {
    def show(a: Int): String = a.toString
  }

  implicit def showString: Show[String] = new Show[String] {
    def show(a: String): String = a
  }

  implicit def showChessPiece: Show[ChessPiece] = ???

  implicit def showOption[A: Show]: Show[Option[A]] = ???




  //Eq

  @typeclass trait Eq[A] {
    def eqv(x: A, y: A): Boolean
    def ===(x: A)(y: A): Boolean = eqv(x, y)
  }

  implicit def eqInt: Eq[Int] = new Eq[Int] {
    def eqv(x: Int, y: Int): Boolean = x == y
  }

  implicit def eqString: Eq[String] = new Eq[String] {
    def eqv(x: String, y: String): Boolean = x.equals(y)
  }

  implicit def eqOption[A: Eq]: Eq[Option[A]] = ???

  implicit def eqEither[A: Eq, B: Eq]: Eq[Either[A, B]] = ???





  //Monoid

  @typeclass trait Monoid[A] {
    def empty: A
    def combine(x: A, y: A): A

    def |+|(x: A)(y: A): A = combine(x, y)
  }

  implicit def intMonoid: Monoid[Int] = new Monoid[Int] {
    def empty: Int = 0

    def combine(x: Int, y: Int): Int = x + y
  }

  implicit def stringMonoid: Monoid[String] = ???

  implicit def timespanMonoid: Monoid[TimeSpan] = ???

  implicit def listMonoid[A]: Monoid[List[A]] = ???

  // The intMonoid further up uses `addition` as its method of combination, but that is not the only monoid for `Int`!
  // We can also define one for multiplication, though if we just define it for `Int` again the compiler won't know which to pick
  // What we can do instead is define a small wrapper that allows us to multiply
  case class Mult(value: Int)

  implicit def multMonoid: Monoid[Mult] = ???

  def combineAll[A: Monoid](list: List[A]): A = ???

  def foldMap[A, B: Monoid](list: List[A])(f: A => B): B = ???

  implicit def tupleMonoid[A: Monoid, B: Monoid]: Monoid[(A, B)] = ???

  implicit def tuple3Monoid[A: Monoid, B: Monoid, C: Monoid]: Monoid[(A, B, C)] = ???

  implicit def mapMonoid[A, B: Monoid]: Monoid[Map[A, B]] = ???

  implicit def futureMonoid[A: Monoid]: Monoid[Future[A]] = ???


  //Monoid word count
  //Use foldMap with a Monoid to count the number of words, the number of characters and the number of occurences of each word
  //Tip: the Map and Tuple3 Monoid can help

  val words: List[String] = rawText.split(" ").toList






  //Now that you have the word count let's extend it with the ability to get the longest word of the text.
  //Tip: Define a Maximum Monoid to do so







  //Functor

  @typeclass trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  implicit def optionFunctor: Functor[Option] = ???

  implicit def listFunctor: Functor[List] = ???



  sealed trait Tree[+A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  case class Leaf[A](value: A) extends Tree[A]

  implicit def treeFunctor: Functor[Tree] = ???






  //Cardinality

  @typeclass trait Cardinality[A] {
    def cardinality: BigInt
  }

  implicit def cardinalityUnit: Cardinality[Unit] = ???

  implicit def cardinalityBoolean: Cardinality[Boolean] = ???

  implicit def cardinalityByte: Cardinality[Byte] = ???

  implicit def cardinalityShort: Cardinality[Short] = ???

  implicit def cardinalityInt: Cardinality[Int] = ???

  implicit def cardinalityTuple[A: Cardinality, B: Cardinality]: Cardinality[(A, B)] = ???

  implicit def cardinalityEither[A: Cardinality, B: Cardinality]: Cardinality[Either[A, B]] = ???

  implicit def cardinalitySize: Cardinality[Size] = ???

  implicit def cardinalityNothing: Cardinality[Nothing]= ???

  implicit def cardinalityFunction[A: Cardinality, B: Cardinality]: Cardinality[A => B] = ???



}
