package main.scala.gameObjects.entity.classes

import main.scala.geometry.SimpleVector
import main.scala.graphics.images.MyImageRef
import java.util.UUID
import main.scala.gameObjects.entity.traits.Visible

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/25/13
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
class VisibleWall(myLoc: SimpleVector, dim: SimpleVector, val imageRef: MyImageRef, entityId: String = UUID.randomUUID().toString) extends Wall(myLoc, dim, entityId) with Visible {
  override def image = imageRef.image
  val _height = image.getHeight(null)
  val _width = image.getWidth(null)

  override def height = _height
  override def width = _width
}
