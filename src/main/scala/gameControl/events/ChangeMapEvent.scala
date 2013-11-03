package main.scala.gameControl.events

import main.scala.gameControl.{GameScreen}

/**
 * A simple event that holds an executable function that will return a new game screen,
 * and an optional new KeyPressHandler.
 */
case class ChangeMapEvent(mapLoader: () => GameScreen) extends MyEvent


