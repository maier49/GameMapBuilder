package main.scala.geometry.angles

/**
 * Defines the basic operations on an angle. This will be extended
 * by <code>RadianAngle</code> and <code>DegreeAngle</code> so that the
 * only time you have to worry about whether your angle is in radians or
 * degrees is when you make it.
 */
import Math.PI
abstract class Angle {
  def degrees : Double
  def radians : Double
  def +(other : Angle) : Angle
  def -(other: Angle) : Angle
  def behind : Angle
  def right : Angle
  def left : Angle


}

object Angle {
  val HalfPi : Double = PI/2
  val TwoPi : Double = PI*2
}