package main.scala.gameControl.events

import main.scala.{ScalaPane}
import main.scala.GameConstants._
import main.scala.gameObjects.entity.classes.{CharacterEntity, Entity}
import scala.swing.event.{KeyReleased, Key, KeyPressed}
import main.scala.gameControl.AreaMap

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 11/2/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
class DefaultEventHandler extends EventHandler {

  override def handleEvents(display: ScalaPane, party: List[CharacterEntity]) {
    if (display.map.isInstanceOf[AreaMap])
      party match {
        case hero :: tail => display.map.asInstanceOf[AreaMap] moveViewTo (hero.location)
        case _ =>
      }
    for (event <- EventQueue.dequeueAll(_ => true))
      handleEvent(event, display, party)
  }

  /**
   * Handles an event
   * @param event
   */
  def handleEvent(event: MyEvent, display: ScalaPane, party: List[CharacterEntity]) {
    event match {
      case (keyEvent: MyKeyEvent) => handleKeyEvent(keyEvent, party)
      case _ =>
    }
  }


  def handleKeyEvent(keyEvent: MyKeyEvent, party: List[CharacterEntity]) {
    keyEvent.swingKeyPressEvent match {
      case keyPressedEvent: KeyPressed =>
        keyPressedEvent.key match {
          case Key.Up =>
            for (character <- party) character go Up
          case Key.Down =>
            for (character <- party) character go Down
          case Key.Left =>
            for (character <- party) character go Left
          case Key.Right =>
            for (character <- party) character go Right
          case Key.Shift =>
            for (character <- party) character run
          case _ =>
        }

      case keyReleasedEvent: KeyReleased =>
        keyReleasedEvent.key match {
          case Key.Up =>
            for (character <- party) character stop Up
          case Key.Left =>
            for (character <- party) character stop Left
          case Key.Right =>
            for (character <- party) character stop Right
          case Key.Down =>
            for (character <- party) character stop Down
          case Key.Shift =>
            for (character <- party) character stopRunning
          case _ =>
        }

      case _ =>

    }
  }
}
