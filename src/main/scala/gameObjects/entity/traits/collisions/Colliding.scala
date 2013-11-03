package main.scala.gameObjects.entity.traits.collisions

import main.scala.geometry.{SimpleVector, Rect}
import main.scala.gameObjects.entity.traits.Mobile

/**
 * A trait to be mixed in to entities that can have some sort of interaction.
 * Requires that any implementors provide a boundary for interaction, and methods
 * for handling that interaction
 *
 */
trait Colliding {
  def boundary : Rect

  /**
   * Determines which Colliding's handleCollision method will be called.
   * The Colilding with the higher priority is the one whose method is called.
   * The other colliding will still have it's collisionSideEffects method called.
  */
  def priority : Int

  /**
   * Checks for a collision, and returns whether to check again for a collision.
   * If there is a collision this calls the appropriate handler and returns the value
   * returned by the handler. This is instead of returning true so that if an entity
   * can collide but does not reverse the collision it can return false, and not cause an infinite
   * loop
   * @param other
   * @return
   */
  def checkForAndHandleCollision(other: Colliding) : Boolean = {
    if (boundary intersectsWith other.boundary) {
      if (priority >= other.priority)  {
        val toReturn = handleCollision(other)
        other.collisionSideEffects(this)
        toReturn
      }
      else {
        val toReturn = other.handleCollision(this)
        collisionSideEffects(other)
        toReturn
      }
    } else false
  }

  def handleCollision(other: Colliding): Boolean = {
    val toReturn = resolveCollision(other)
    collisionSideEffects(other)
    toReturn
  }

  /**
   * Resolves the collision
   * The default behavior is to just return false
   * @param other The entity this entity has collided with
   * @return Whether or not this should be counted as a collision for collision resolution purposes on the map.
   *         In other words, if this returns true, then keep trying to resolve until the entities are not touching,
   *         and if it returns false it's okay that they're touching
   */
  def resolveCollision(other: Colliding) : Boolean

  def collisionSideEffects(other: Colliding) {
    //Does nothing, but provides an implementation so subclasses can stack
  }


}
