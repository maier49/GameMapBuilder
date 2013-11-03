package main.scala.gameObjects.entity.traits

import java.awt.Image
import main.scala.gameObjects.entity.classes.Entity

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/25/13
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
trait Visible extends Entity {
  def image: Image
  def height: Int
  def width: Int
}
