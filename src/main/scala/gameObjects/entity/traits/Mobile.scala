package main.scala.gameObjects.entity.traits

import main.scala.geometry.SimpleVector
import main.scala.gameObjects.entity.classes.Entity
import main.scala.GameConstants

/**
 * Contains basic movement functionality
 */
trait Mobile extends Entity {
  var velocity: SimpleVector = SimpleVector(0, 0)
  var direction = GameConstants.Down

  def move(): Mobile = {
    location += velocity
    direction = if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
      if (velocity.x > 0)
        GameConstants.Right
      else if (velocity.x < 0)
        GameConstants.Left
      else
        direction
    } else {
      if (velocity.y > 0)
        GameConstants.Up
      else if (velocity.y < 0)
        GameConstants.Down
      else
        direction
    }
    this
  }

  def moveBy(distance: SimpleVector): Mobile = {
    location += distance
    this
  }

  def moveTo(newLocation: SimpleVector): Mobile = {
    location = newLocation
    this
  }

}
