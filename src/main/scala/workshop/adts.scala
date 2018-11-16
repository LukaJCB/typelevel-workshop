package workshop

object adts {

  // Design a data type for coffee sizes, should have small medium and large
  sealed trait Size
  case object Small extends Size
  case object Medium extends Size
  case object Large extends Size

  // Model a data type for a contact that can either be an email or a phone number
  type Contact = Unit

  // Design a data type for a chess piece and its position on the chess board
  type ChessPiece = Unit

  // Write a function using pattern mathcing that takes a square and returns whether it can move there or not

  // Model a data type that stores time spans
  type TimeSpan = Unit

  // Write a function that adds two TimeSpan values together

  // List all values of the type `Unit`
  def allValuesUnit: Set[Unit] = ???

  // List all values of the type `Nothing`
  def allValuesNothing: Set[Nothing] = ???

  // List all values of the type `Boolean`
  def allValuesBoolean: Set[Boolean] = ???

  // List all values of the type `Size`
  def allValuesSize: Set[Size] = ???

  // List all values of the type `(Size, Boolean)`
  def allValuesTuple: Set[(Size, Boolean)] = ???

  // List all values of the type `Either[Size, Boolean]`
  def allValuesEither: Set[Either[Size, Boolean]] = ???

  // List all values of the type `(Size, Unit)`
  def allValuesTupleUnit: Set[(Size, Unit)] = ???

  // List all values of the type `Either[Boolean, Nothing]`
  def allValuesEitherNothing: Set[Either[Boolean, Nothing]] = ???

}
