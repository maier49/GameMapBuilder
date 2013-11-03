package main.scala

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/19/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
package object geometry {
  implicit class CoordinateOps(val coordinate: Int) extends AnyVal {

    def isBetween(left : Int, right: Int) : Boolean = {
      coordinate <= right && coordinate >= left
    }

    def isAbove(target: Int) : Boolean = {
      coordinate >= target
    }

    def isBelow(target: Int) : Boolean = {
      coordinate <= target
    }

  }
}
