package main.scala.gameControl

import java.awt.Image
import scala.collection.mutable
import main.scala.gameControl.events.{EventQueue, MyEvent}

/**
 * Basic trait which defines the necessary elements of a game screen.
 * It should be able to generate an image, it should be able to update its
 * own state, and it should be able to publish events to the global queue
 *
 */
trait GameScreen {
  def draw: Image
  def update()
  def addEventToQueue(event: MyEvent) = EventQueue.enqueue(event)

}
