package main.scala.geometry

import main.scala.geometry.angles.{RadianAngle, Angle}
import Math.{atan2, cos, sin}

/**
 * Represents a two dimensional vector
 * bearings are clockwise with north as 0
 * @since 1.0.0
 */
case class RealVector(x: Double, y: Double) {
  def +(other: RealVector): RealVector = new RealVector(x + other.x, y + other.y)

  def +(other: SimpleVector): RealVector = new RealVector(x + other.x, y + other.y)

  def -(other: RealVector): RealVector = new RealVector(x - other.x, y - other.y)

  def -(other: SimpleVector): RealVector = new RealVector(x - other.x, y - other.y)

  def *(mult: Int): RealVector = new RealVector(x * mult, y * mult)

  def *(mult: Double): RealVector = new RealVector(x * mult, y * mult)

  def /(mult: Int): RealVector = new RealVector(x / mult, y / mult)

  def /(mult: Double): RealVector = new RealVector(x / mult, y / mult)

  def magnitude: Double = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))

  //Bearing from north
  def bearing: Angle = new RadianAngle(atan2(x, y))

  def rotate(angle: Angle): RealVector = new RealVector(
    x * cos(angle.radians) - y * sin(angle.radians),
    x * sin(angle.radians) + y * cos(angle.radians)
  )


}
