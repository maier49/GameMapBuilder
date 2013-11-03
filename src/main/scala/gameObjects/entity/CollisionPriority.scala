package main.scala.gameObjects.entity

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/24/13
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
object CollisionPriority {
  def ALWAYS_FIRST = Int.MaxValue
  def ALWAYS_LAST = Int.MinValue
  def NEUTRAL = 0
  def HIGH = 1
  def LOW = -1
}
