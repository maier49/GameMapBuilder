package main.scala.gameObjects.entity.classes

import main.scala.gameObjects.entity.traits.collisions.Colliding
import main.scala.gameObjects.entity.traits.{Visible, Eventing}
import main.scala.gameObjects.entity.CollisionPriority
import main.scala.geometry.{Rect, SimpleVector}
import main.scala.graphics.images.MyImageRef
import java.util.UUID
import main.scala.gameControl.events.ChangeMapEvent
import main.scala.gameControl.AreaMap

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 11/2/13
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */
class Door(myLoc: SimpleVector, val dim: SimpleVector, val mapName: String, entityId: String = UUID.randomUUID().toString) extends Wall(myLoc, dim, entityId) with Eventing {
  override def resolveCollision(other: Colliding) = false

  override def collisionSideEffects(other: Colliding) {
    if (other.isInstanceOf[CharacterEntity]) {
      publishEvent(
        new ChangeMapEvent( () => AreaMap.loadFromFile("src/main/resources/maps/" + mapName))
      )
    }
  }


}
