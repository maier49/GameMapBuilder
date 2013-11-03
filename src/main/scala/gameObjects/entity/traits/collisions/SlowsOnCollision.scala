package main.scala.gameObjects.entity.traits.collisions

import main.scala.geometry.SimpleVector
import main.scala.gameObjects.entity.traits.Mobile
import main.scala.gameObjects.entity.classes.Entity

/**
 * When this entity collides, first its last motion is reversed, and then it's velocity is multiplied by the
 * damping factor and rounded to the nearest integer. The damping factor defaults to .5
 */
trait SlowsOnCollision extends Entity with Colliding with Mobile {

  def dampeningFactor : Double = .5

  abstract override def collisionSideEffects(other: Colliding) {
    val dampedVelocity = velocity * dampeningFactor
    velocity = SimpleVector(dampedVelocity.x.toInt, dampedVelocity.y.toInt)

    super.collisionSideEffects(other)
  }

}
