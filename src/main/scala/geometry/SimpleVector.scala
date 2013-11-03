package main.scala.geometry

import main.scala.geometry.angles.{RadianAngle, Angle}
import java.lang.Math._
import scala.util.parsing.json.JSONObject

/**
 * A lot of objects don't need to be represented by decimals, and along with that don't need an angle to define bearing
 * as they are more easily represented by just an x and y value. That's what this class is for
 * @since 1.0
 */
case class SimpleVector (x : Int, y: Int) {
  def addX(distX : Int) : SimpleVector = copy(x =  distX + x)

  def addY(distY: Int) : SimpleVector = copy(y =  y + distY)

  def +(other: SimpleVector) : SimpleVector = SimpleVector(x + other.x, y + other.y)

  def +(other: RealVector) : RealVector = RealVector(x + other.x, y + other.y)

  def -(other : SimpleVector) : SimpleVector = SimpleVector(x - other.x, y - other.y)

  def -(other : RealVector) : RealVector= RealVector(x - other.x, y - other.y)

  def / (mult: Int) : SimpleVector = SimpleVector(x/mult, y/mult)

  def / (mult: Double) : RealVector = RealVector(x/mult, y/mult)

  def * (mult : Int) : SimpleVector = SimpleVector(x * mult, y * mult)

  def * (mult : Double) : RealVector = RealVector(x * mult, y * mult)

  def magnitude: Double = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))

  //Bearing from north
  def bearing : Angle = new RadianAngle(atan2(x,y))

  def normalize(max: Int): SimpleVector = {
    val mag = magnitude.toInt

    if (mag > 0 && mag > max)
      this/(mag/max)
    else
      this
  }

}
