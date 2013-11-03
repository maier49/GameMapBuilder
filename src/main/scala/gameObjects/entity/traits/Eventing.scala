package main.scala.gameObjects.entity.traits

import main.scala.gameControl.events.{EventQueue, MyEvent}
import main.scala.gameObjects.entity.classes.Entity

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 11/2/13
 * Time: 9:39 AM
 * To change this template use File | Settings | File Templates.
 */
trait Eventing extends Entity {
  def publishEvent(event: MyEvent) = EventQueue.enqueue(event)
}
