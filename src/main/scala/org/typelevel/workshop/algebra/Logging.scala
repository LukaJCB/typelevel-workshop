package org.typelevel.workshop.algebra

trait Logging[F[_]] {

}

object Logging {
  def apply[F[_]: Logging]: Logging[F] = implicitly
}