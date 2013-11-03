package main.scala.gameObjects.entity.traits.collisions

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/24/13
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
trait StopsOnCollision extends SlowsOnCollision {
  override def dampeningFactor: Double = 0.0
}
