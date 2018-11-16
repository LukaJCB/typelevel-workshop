package workshop

import org.scalacheck.{Arbitrary, Gen, Prop, Properties}
import org.scalacheck.Prop.forAll
import workshop.adts.ChessPiece
import workshop.typeclasses.Monoid

object properties extends Properties("Workshop") {

  // Implement a Generator for generating only pawns
  def pawnGen: Gen[ChessPiece] = ???

  // Write a Generator that generates kings and queens
  def kingQueenGen: Gen[ChessPiece] = ???

  // Implement a generator rooks, bishops and knights
  def rookBishopKnightGen: Gen[ChessPiece]  = ???


  // Implement an Arbitrary instance for ChessPiece using the generators earlier
  implicit def chessPieceArbitrary: Arbitrary[ChessPiece] = ???




  // write a property that checks that Pawns can only move forward
  // to do so we'll use the generator that generates pawns only
  property("Pawns move forward") = Prop.forAll(pawnGen) { (pawn: ChessPiece) =>
    ???
  }


  // Next let's think about the various type class laws we learned about, can you implement properties for them?
  def monoidAssociativity[M: Monoid: Arbitrary] = ???
}
