package main.scala.geometry.angles

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/19/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */

import Math.PI
import main.scala.geometry.angles.Angle.{HalfPi, TwoPi}
object CardinalDirections {
  final val North = new RadianAngle(0)
  final val East = new RadianAngle(HalfPi)
  final val South = new RadianAngle(PI)
  final val West = new RadianAngle(3*HalfPi)
}
