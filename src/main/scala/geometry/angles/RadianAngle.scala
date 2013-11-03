package main.scala.geometry.angles

import java.lang.Math.PI
import main.scala.geometry.angles.Angle._

/**
 * An angle with Radian units normalized between
 * 0 and 2 PI
 */
class RadianAngle(value : Double) extends Angle{
  private final val rads = {
    var temp = value
    while(temp > TwoPi) temp -= TwoPi
    while(temp < 0) temp += TwoPi
    temp
  }

  private final val deg = rads * (180/PI)
  override def degrees = deg
  override def radians = rads
  override def +(other : Angle) = new RadianAngle(other.radians + rads)
  override def -(other : Angle) = new RadianAngle(rads - other.radians)
  override def behind = new RadianAngle(rads + PI)
  override def left = new RadianAngle(rads - HalfPi)
  override def right = new RadianAngle(rads + HalfPi)

}
