package main.scala.geometry.angles

/**
 * Implementation of an angle with degrees as its units
 */

import Math.PI
class DegreeAngle(value : Double) extends Angle{
  private final val deg = {
    var temp = value
    while (temp > 360) temp -= 360
    while (temp < 0) temp += 360

    temp
  }

  private final val rads = deg * (PI/180)
  override def degrees : Double = deg
  override def radians : Double = rads
  override def +(other : Angle) : Angle = new DegreeAngle(deg + other.degrees)
  override def -(other : Angle) : Angle = new DegreeAngle(deg - other.degrees)
  override def behind : Angle = new DegreeAngle(deg + 180)
  override def left: Angle = new DegreeAngle(deg - 90)
  override def right: Angle = new DegreeAngle(deg + 90)

}
