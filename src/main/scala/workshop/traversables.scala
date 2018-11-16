package workshop

import simulacrum.typeclass
import workshop.typeclasses.{Functor, Monoid}
import workshop.abstractions.{Foldable, Monoidal, Traverse}
import workshop.monoids.{IO, Monad}
import workshop.typeclasses.Monoid.ops._
import workshop.abstractions.Traverse.ops._
import workshop.abstractions.Monoidal.ops._
import workshop.monoids.Monad.ops._

object traversables {

  // This function is like traverse, but ignores all the results and can therefore be implemented with fold
  // For this you might need to create a custom `Monoid` derived from a `Monoidal`
  def traverse_[T[_]: Foldable, F[_]: Monoidal, A, B](ta: T[A])(f: A => F[B]): F[Unit] = ???


  case class User(name: String, age: Int)
  case class Address(city: String, street: String, streetNo: Int)

  val users: List[User] = List(
    User("john doe", 33),
    User("jane doe", 35),
    User("buddy friend", 29),
    User("john kennedy", 101)
  )

  def getAddressForUser(user: User): IO[Option[Address]] = // Fake http request
    IO { () =>
      if (user.name.contains("doe")) Some(Address("Anytown", "Main St", 123))
      else if (user.name.contains("kennedy")) Some(Address("Berlin", "Ebert St", 42))
      else None
    }

  // Now implement a function that takes a list of users and returns a list of addresses
  def addressesForUsers(users: List[User]): IO[List[Address]] = ???




  val userOver40: Option[User] = users.find(_.age > 40)

  // Find the address for the `userOver40` defined above
  def addressOver40: IO[Option[Address]] = ???


  // This function can map one user to multiple addresses
  def getAddressesForUser(user: User): IO[List[Address]] = // Fake http request
    IO { () =>
      if (user.name.contains("doe")) List(
        Address("Anytown", "Main St", 123),
        Address("Ghost town", "No St", 0)
      )
      else if (user.name.contains("kennedy")) List(
        Address("Berlin", "Ebert St", 42),
        Address("Washington", "Pennsylvania Ave", 1600)
      )
      else List.empty
    }

  // Next get all addresses for all users
  def allAddresses: IO[List[Address]] = ???




  def flatTraverse[T[_]: Traverse: Monad, F[_]: Monoidal, A, B](ta: T[A])(f: A => F[T[B]]): F[T[B]] =
    ta.traverse(f).map(_.flatten)

  // Using flatTraverse, see if you can reimplement `addressOver40` and `allAddresses`
  def addressOver40FlatTraversed: IO[Option[Address]] = ???
  def allAddressesFlatTraversed: IO[List[Address]] = ???



  @typeclass trait FunctorFilter[F[_]] extends Functor[F] {
    def mapFilter[A, B](fa: F[A])(f: A => Option[B]): F[B]

    override def map[A, B](fa: F[A])(f: A => B): F[B] = mapFilter(fa)(a => Some(f(a)))
  }

  //Implement a `FunctorFilter` instance for `List`
  implicit def listFunctorFilter: FunctorFilter[List] = ???

  //Implement a `FunctorFilter` instance for `Option`
  implicit def optionFunctorFilter: FunctorFilter[Option] = ???


  // Implement a function that takes a list of integers, filters all the odd numbers out and multiplies all the even numbers by two.
  ???




  // This function is a combination of `mapFilter` and `foldMap`
  def collectSomeFold[F[_]: Foldable, A, M: Monoid](fa: F[A])(f: A => Option[M]): M = ???


  // Use the least amount of operations to implement a word count like we did earlier,
  // but also filtering all stop words (e.g. 'if', 'and', 'or', 'the' etc.)


  //implement this function using `collectSomeFold`
  def filterFold[F[_]: Foldable, A, M: Monoid](fa: F[A])(f: A => Boolean): M = ???





  @typeclass trait TraverseFilter[T[_]] extends Traverse[T] {
    def traverseFilter[F[_]: Monoidal, A, B](ta: T[A])(f: A => F[Option[B]]): F[T[B]]

    override def traverse[G[_] : Monoidal, A, B](fa: T[A])(f: A => G[B]): G[T[B]] =
      traverseFilter(fa)(a => f(a).map(Some(_)))
  }

  //Implement a `FunctorFilter` instance for `List`
  implicit def listTraverseFilter: TraverseFilter[List] = ???

  //Implement a `FunctorFilter` instance for `Option`
  implicit def optionTraverseFilter: TraverseFilter[Option] = ???


  // See if you can reimplement the `addressesForUsers` function using `traverseFilter`

}
