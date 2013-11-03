package main.scala.gameObjects.entity.traits.collisions

import main.scala.geometry.SimpleVector
import main.scala.gameObjects.entity.traits.Mobile
import main.scala.gameObjects.entity.classes.Entity

/**
 * An entity with this collision trait will rebound from a collision
 * in the opposite direction at the same velocity. For a 'bounce' effect with some consideration for physics,
 * see <code>BouncesOnCollision</code>
 *
 */
trait ReflectsOnCollision extends Entity with Colliding with Mobile {

  abstract override def collisionSideEffects(other: Colliding) {
    //Attempt to simulate some degree of realistic reflection
    val intersectsHorizontal = (this.boundary + velocity.copy(y = 0)) intersectsWith other.boundary
    val intersectsVertical = (this.boundary + velocity.copy(x = 0)) intersectsWith other.boundary

    if (!intersectsHorizontal && intersectsVertical)
      velocity = SimpleVector(velocity.x, velocity.y * -1)
    else if (intersectsHorizontal && !intersectsVertical)
      velocity = SimpleVector(velocity.x * -1, velocity.y)
    else if (intersectsHorizontal || intersectsVertical)
      velocity = velocity * -1

    super.collisionSideEffects(other)
  }

}
