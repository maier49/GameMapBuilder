package main.scala.gameObjects.entity.classes

import main.scala.geometry.{Rect, SimpleVector}
import java.awt.{Rectangle, Image}
import java.util.UUID

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/23/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class Entity(var location: SimpleVector, val id: String  = UUID.randomUUID().toString) {


  override def equals(other: Any): Boolean = {
    other.isInstanceOf[Entity] && id == other.asInstanceOf[Entity].id
  }
}
