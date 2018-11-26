package workshop

import workshop.monoids.Monad
import workshop.typeclasses.Show

object state {

  case class State[S, A](run: S => (S, A)) {
    def map[B](f: A => B): State[S, B] =
      State { s =>
        val (s2, a) = run(s)
        (s2, f(a))
      }

    def flatMap[B](f: A => State[S, B]): State[S, B] =
      State { s =>
        val (s2, a) = run(s)
        f(a).run(s2)
      }
  }

  object State {
    def get[S]: State[S, S] = State(a => (a, a))

    def set[S](s: S): State[S, Unit] = State(_ => (s, ()))

    def modify[S](f: S => S): State[S, Unit] = State(s => (f(s), ()))
  }


  //Implement the monad instance for State
  implicit def stateMonad[S]: Monad[State[S, ?]] = ???






  sealed trait Player {
    def next: Player = this match {
      case Cross => Circle
      case Circle => Cross
    }
  }
  case object Cross extends Player
  case object Circle extends Player

  case class TicTacToe(board: List[List[Option[Player]]], turn: Player) {
    def isGameOver: Boolean =
      gridComplete || hasPlayerWon.isDefined

    def hasPlayerWon: Option[Player] =
      threeInARow(List(board(0)(0), board(0)(1), board(0)(2))) orElse
        threeInARow(List(board(1)(0), board(1)(1), board(1)(2))) orElse
        threeInARow(List(board(2)(0), board(2)(1), board(2)(2))) orElse
        threeInARow(List(board(0)(0), board(1)(0), board(2)(0))) orElse
        threeInARow(List(board(0)(1), board(1)(1), board(2)(1))) orElse
        threeInARow(List(board(0)(2), board(1)(2), board(2)(2))) orElse
        threeInARow(List(board(0)(2), board(1)(1), board(2)(0))) orElse
        threeInARow(List(board(0)(0), board(1)(1), board(2)(2)))

    private def threeInARow(row: List[Option[Player]]): Option[Player] =
      row.headOption.flatten.filter(_ => row.forall(_.isDefined) && row.distinct.size == 1)

    def gridComplete: Boolean =
      board.forall(_.forall(_.isDefined))

    def canPlace(posX: Int, posY: Int): Boolean =
      board(posY)(posX).isEmpty

    def place(posX: Int, posY: Int): TicTacToe =
      if (canPlace(posX, posY)) {
        val boardY = board(posY)
        TicTacToe(board.updated(posY, boardY.updated(posX, Some(turn))), turn.next)
      } else this
  }

  object TicTacToe {
    def empty: TicTacToe = TicTacToe(0.until(3).map(_ => 0.until(3).map(_ => None).toList).toList, Cross)
  }

  // Implement a Show instance for TicTacToe that prints out three rows and columns and uses `O` and `X` for the players
  implicit def showTicTacToe: Show[TicTacToe] = ???


  // Use the State data type to model the state transitions of a tic tac toe game
  def place(posX: Int, posY: Int): State[TicTacToe, Unit] = ???


  def isGameOver: State[TicTacToe, Boolean] = ???


  def crossHasWon: State[TicTacToe, Boolean] = ???

  def circleHasWon: State[TicTacToe, Boolean] = ???

  // Define a full game using a for comprehension and return the winner
  def fullGame: State[TicTacToe, Option[Player]] = ???


  // Now we run the state using `run` to get the final board and the player that won



}
