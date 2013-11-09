package main.scala.gameObjects.entity.classes

import main.scala.gameObjects.entity.classes.Entity
import main.scala.gameObjects.entity.traits.collisions.{BinarySearchCollisionResolution, Colliding}
import main.scala.graphics.images.{SectionImageRef, DefaultImageStore, ImageStore, MyImageRef}
import main.scala.geometry.{Rect, SimpleVector}
import main.scala.gameObjects.entity.CollisionPriority
import java.awt.{Graphics, Image}
import main.scala.gameObjects.entity.traits.Visible
import main.scala.GameConstants
import java.util.UUID


/**
 * Defines properties unique to a player  entity
 */
class CharacterEntity(private var _playerImageId: String, imageStore: ImageStore = DefaultImageStore, characterId: String = UUID.randomUUID().toString) extends Entity(SimpleVector(0, 0), characterId) with Visible with BinarySearchCollisionResolution {
  var animationSequenceCount = 0

  def playerImageId = _playerImageId

  def playerImageId_=(newImageId: String) = {

    _playerImageId = newImageId
    _height = imageStore.getImage(_playerImageId).get.getHeight(null)
    _width = imageStore.getImage(_playerImageId).get.getWidth(null)
  }
  var _height = imageStore.getImage(_playerImageId).get.getHeight(null)
  def height = _height
  var _width = imageStore.getImage(_playerImageId).get.getWidth(null)
  def width = _width

  def animationSequenceIndex = animationSequenceCount / framesPerAnimation

  var framesPerAnimation = 40
  val playerImageWidth = 96
  val playerImageHeight = 128
  val animationFrameSize = playerImageWidth / 3
  val directionImageSetSize = playerImageHeight / 4

  var walkSpeed = 1
  var runSpeed = 2
  var running = false
  var left = false
  var right = false
  var up = false
  var down = false

  def leftVector = SimpleVector(-1 * (if (running) runSpeed else walkSpeed), 0)

  def rightVector = SimpleVector((if (running) runSpeed else walkSpeed), 0)

  def downVector = SimpleVector(0, -1 * (if (running) runSpeed else walkSpeed))

  def upVector = SimpleVector(0, (if (running) runSpeed else walkSpeed))

  override def priority = CollisionPriority.NEUTRAL

  override def image: Image = {
    val offsetY = playerImageHeight - (directionImageSetSize * direction)
    val offsetX = animationFrameSize * animationSequenceIndex
    animationSequenceCount = (animationSequenceCount + 1) % (3 * framesPerAnimation)
    SectionImageRef(
      playerImageId,
      SimpleVector(offsetX, offsetY),
      SimpleVector(offsetX + animationFrameSize, offsetY - directionImageSetSize),
      imageStore
    ).image
  }

  override def draw(graphics: Graphics, adjustedLocation: SimpleVector) {
    val offsetY = playerImageHeight - (directionImageSetSize * direction)
    val offsetX = animationFrameSize * animationSequenceIndex
    animationSequenceCount = (animationSequenceCount + 1) % (3 * framesPerAnimation)
    SectionImageRef(
      playerImageId,
      SimpleVector(offsetX, offsetY),
      SimpleVector(offsetX + animationFrameSize, offsetY - directionImageSetSize),
      imageStore
    ).draw(graphics, adjustedLocation)
  }

  override def boundary = {
    Rect(
      SimpleVector(location.x, location.y + directionImageSetSize),
      SimpleVector(location.x + animationFrameSize, location.y)
    )
  }

  def go(direction: Int) {
    var dirty = false
    direction match {
      case GameConstants.Up => if (!up) {
        up = true
        dirty = true
      }
      case GameConstants.Left => if (!left) {
        left = true
        dirty = true
      }
      case GameConstants.Right => if (!right) {
        right = true
        dirty = true
      }
      case GameConstants.Down => if (!down) {
        down = true
        dirty = true
      }
    }

    if (dirty)
      velocity = determineVelocity
  }

  def stop(direction: Int) {
    var dirty = false
    direction match {
      case GameConstants.Up => if (up) {
        up = false
        dirty = true
      }
      case GameConstants.Left => if (left) {
        left = false
        dirty = true
      }
      case GameConstants.Right => if (right) {
        right = false
        dirty = true
      }
      case GameConstants.Down => if (down) {
        down = false
        dirty = true
      }
    }

    if (dirty)
      velocity = determineVelocity
  }

  def run {
    if (!running) {
      running = true
      velocity = determineVelocity
    }
  }

  def stopRunning {
    if (running) {
      running = false
      velocity = determineVelocity
    }
  }

  /**
   * Add all direction vectors and normalize
   */
  private def determineVelocity: SimpleVector = {
    val vel = SimpleVector(0, 0) +
      (if (left) leftVector else SimpleVector(0, 0)) +
      (if (right) rightVector else SimpleVector(0, 0)) +
      (if (up) upVector else SimpleVector(0, 0)) +
      (if (down) downVector else SimpleVector(0, 0))

    vel.normalize(
      if (running) runSpeed else walkSpeed
    )
  }
}

