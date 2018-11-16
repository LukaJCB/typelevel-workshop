import simulacrum.typeclass


type IntBoolean = (Int, Boolean)

val foo: IntBoolean = (42, true)

type IntOrBoolean = Int Either Boolean

val bar: IntOrBoolean = Right(true)


case class Person(name: String, age: Int)

val person = Person("john", 33)

val newJohn = person.copy(age = 34)


sealed trait Suit
case class Heart(number: Int, s: String) extends Suit
case object Club extends Suit
case object Spade extends Suit
case object Cross extends Suit

val card: Suit = Club

card match {
  case Heart(n, s) => s
  case Club => 1
  case Spade => 2
}


val u: Unit = ()





