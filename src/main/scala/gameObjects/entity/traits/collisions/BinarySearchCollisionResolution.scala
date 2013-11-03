package main.scala.gameObjects.entity.traits.collisions

import main.scala.geometry.SimpleVector
import main.scala.gameObjects.entity.traits.Mobile
import main.scala.gameObjects.entity.classes.Entity

/**
 * Provides an implementation of collision resolution in which, the infringing object
 * is returned to its original position, and then moved again half the distance it moved before, until it
 * finds a distance where it does not collide. This looks perfectly fine for almost all entities, accept those
 * that move very quickly.
 */
trait BinarySearchCollisionResolution extends Entity with Colliding with Mobile {
  override def resolveCollision(other: Colliding): Boolean = {
    resolveCollision(other, velocity)
    true
  }

  private def resolveCollision(collidingEntity: Colliding, velocity: SimpleVector) {
    if (boundary intersectsWith collidingEntity.boundary) {
      moveBy(velocity * -1)
      while(boundary intersectsWith collidingEntity.boundary)
          moveBy(velocity * -1)



      moveBy(velocity / 2)
      resolveCollision(collidingEntity, velocity / 2)
    }
  }

  override def move(): BinarySearchCollisionResolution = {
    super.move()
    this
  }

  override def moveBy(distance: SimpleVector): BinarySearchCollisionResolution = {
    super.moveBy(distance)
    this
  }

  override def moveTo(location: SimpleVector): BinarySearchCollisionResolution = {
    super.moveTo(location)
    this
  }
}
