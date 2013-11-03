package main.scala.geometry

import Math.{min, max}

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
case class Rect(topLeft: SimpleVector, bottomRight: SimpleVector) {
  assert(topLeft.x < bottomRight.x)
  assert(topLeft.y > bottomRight.y)
  def bottom = bottomRight.y
  def top = topLeft.y
  def left = topLeft.x
  def right = bottomRight.x

  def intersectsWith(other: Rect): Boolean = {
    (
        !(bottom isAbove other.top) &&
        ! (top isBelow other.bottom) &&
        (
          right.isBetween(other.left, other.right) ||
            left.isBetween(other.left, other.right) ||
            other.left.isBetween(left, right) ||
            other.right.isBetween(left, right)
          )
      )
  }


  //Translates the rectangle by a vector
  def + (vector: SimpleVector): Rect = Rect(topLeft + vector, bottomRight + vector)

  //Translates the rectangle by a vector
  def - (vector: SimpleVector): Rect = Rect(topLeft - vector, bottomRight - vector)


  //Returns the rectangle that contains both rectangles
  def + (other: Rect): Rect = Rect(
    SimpleVector(
      min(topLeft.x, other.topLeft.x),
      max(topLeft.y, other.topLeft.y)
    ),
    SimpleVector(
      max(bottomRight.x, other.bottomRight.x),
      min(bottomRight.y, other.bottomRight.y)
    )
  )


}

