package main.scala.gameControl.events

import main.scala.ScalaPane
import main.scala.gameObjects.entity.classes.{CharacterEntity, Entity}

/**
 * Basic trait that confers one method, handling events
 */
trait EventHandler {
  def handleEvents(display: ScalaPane, importantEntities: List[CharacterEntity])
}
