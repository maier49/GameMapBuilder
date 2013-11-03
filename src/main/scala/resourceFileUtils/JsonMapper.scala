package main.scala.resourceFileUtils

import scala.util.parsing.json.JSONObject
import main.scala.geometry.SimpleVector
import main.scala.resourceFileUtils.AreaMapFileKeys.{CoordinateKey, ImageTypeVal}
import scala.collection.mutable
import scala.collection.immutable.{HashMap, Map}
import main.scala.graphics.images.{TiledImageRef, SimpleImageRef, MyImageRef}
import main.scala.gameObjects.entity.classes._
import main.scala.gameObjects.entity.classes.Scenery
import main.scala.graphics.images.SimpleImageRef
import scala.util.parsing.json.JSONObject
import main.scala.graphics.images.TiledImageRef
import scala.Some
import main.scala.geometry.SimpleVector

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
object JsonMapper {
  def toJson(entity: Entity): JSONObject = {
    entity match {
      case (wall: Wall) => toJson(wall)
      case (scenery: Scenery) => toJson(scenery)
    }
  }


  def toJson(simpleVector: SimpleVector): JSONObject = {
    new JSONObject(
      new HashMap[String, Any]() + ((CoordinateKey.X, simpleVector.x)) + ((CoordinateKey.Y, simpleVector.y))
    )
  }

  def simpleVectorFromJson(json: JSONObject): Option[SimpleVector] = {
    val toReturn = json.obj.get(CoordinateKey.X).flatMap {
      case (xCoord: Double) =>

        json.obj.get(CoordinateKey.Y).flatMap {
          case (yCoord: Double) =>
            Some(SimpleVector(xCoord.toInt, yCoord.toInt))
        }
    }

    toReturn
  }


  def toJson(image: MyImageRef): JSONObject = {
    var hashMap = new HashMap[String, Any]
    image match {
      case tiled: TiledImageRef => {
        hashMap ++= ((AreaMapFileKeys.VectorType.Size, toJson(SimpleVector(tiled.width, tiled.height))) ::
          (AreaMapFileKeys.ImageFileKey, tiled.tileImageFileKey) ::
          (AreaMapFileKeys.ImageType, ImageTypeVal.TiledImage) :: Nil)
        new JSONObject(hashMap)
      }
      case simple: SimpleImageRef =>
        hashMap ++= ((AreaMapFileKeys.ImageFileKey, simple.jImage) ::
          (AreaMapFileKeys.ImageType, ImageTypeVal.SimpleImage) :: Nil)
        new JSONObject(hashMap)
    }
  }

  def toJson(wall: Wall): JSONObject = {
    var hashMap = new HashMap[String, Any]
    hashMap ++= ((AreaMapFileKeys.VectorType.Size,
      toJson(SimpleVector(wall.boundary.right - wall.boundary.left, wall.boundary.top - wall.boundary.bottom))) ::
      (AreaMapFileKeys.VectorType.Location, toJson(wall.location)) :: Nil)

    if (wall.isInstanceOf[VisibleWall]) {
      hashMap ++:= toJson(wall.asInstanceOf[VisibleWall].imageRef).obj
    } else {
      hashMap ++= ((AreaMapFileKeys.ImageFileKey, AreaMapFileKeys.NoImage) ::
        (AreaMapFileKeys.ImageType, AreaMapFileKeys.NoImage) :: Nil)
    }

    new JSONObject(hashMap)
  }

  def toJson(entity: Entity, layer: String): JSONObject = {
    val jsonObject = toJson(entity)

    new JSONObject(jsonObject.obj + ((AreaMapFileKeys.Layer, layer)) + ((AreaMapFileKeys.EntityId, entity.id)))
  }

  def toJson(character: CharacterEntity): JSONObject = {
    var hashMap = new HashMap[String, Any]
    hashMap ++= (
      (AreaMapFileKeys.VectorType.Location, toJson(character.location)) ::
        (AreaMapFileKeys.CharacterEntityKeys.RunSpeed, character.runSpeed.toString) ::
        (AreaMapFileKeys.CharacterEntityKeys.WalkSpeed, character.walkSpeed.toString) ::
        (AreaMapFileKeys.ImageFileKey, character.playerImageId) ::
        Nil
      )
    new JSONObject(hashMap)
  }

  def toJson(scenery: Scenery): JSONObject = {
    val jsonObject = toJson(scenery.imageRef)

    new JSONObject(jsonObject.obj +
      ((AreaMapFileKeys.VectorType.Location, toJson(scenery.location))))
  }
}
