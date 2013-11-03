package main.scala.gameObjects.entity.classes

import main.scala.geometry.{Rect, SimpleVector}
import java.util.UUID
import main.scala.gameObjects.entity.traits.Mobile
import main.scala.gameObjects.entity.traits.collisions.{BinarySearchCollisionResolution, Colliding}
import main.scala.gameObjects.entity.CollisionPriority

/**
 * A basic implementation of a wall. The main properties of a wall are that it
 * always resolves collisions first, and that it resolves collisions by moving the object that collided
 * with it
 */
class Wall(loc: SimpleVector, dimensions: SimpleVector, entityId: String = UUID.randomUUID().toString) extends Entity(loc, entityId) with Colliding {
  override def boundary = new Rect(SimpleVector(location.x, location.y + dimensions.y), SimpleVector(location.x + dimensions.x, location.y))
  override def priority = CollisionPriority.ALWAYS_FIRST

  override def resolveCollision(other: Colliding): Boolean = {
    if (!(other.isInstanceOf[Mobile])) {
      //This is bad
      throw new RuntimeException("Two Immobile Objects Were in a collision")
    } else {
      if (other.isInstanceOf[BinarySearchCollisionResolution]) {
        other.resolveCollision(this)
      } else {

        val newCollisionResolver = new Entity(other.asInstanceOf[Mobile].location) with BinarySearchCollisionResolution{
          override def boundary = null
          override def priority = 0
        }
        newCollisionResolver.velocity = other.asInstanceOf[Mobile].velocity

        newCollisionResolver.resolveCollision(this)
        other.asInstanceOf[Mobile].velocity = newCollisionResolver.velocity

      }
    }

    true
  }

}
