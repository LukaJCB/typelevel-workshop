package workshop

import java.io.File

import workshop.monoids.{EffectFunction, FailFunction}

import scala.io.{Source, StdIn}

object util {
  def readLine: EffectFunction[Unit, String] =
    EffectFunction(_ => StdIn.readLine)

  def printLine: EffectFunction[String, Unit] =
    EffectFunction(s => println(s))

  def readFile: FailFunction[File, String] =
    FailFunction(file => try {
      Right(Source.fromFile(file).getLines.mkString)
    } catch {
      case t: Throwable => Left(t)
    })
}
