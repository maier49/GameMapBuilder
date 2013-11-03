package main.scala.gameControl

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/19/13
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */

import main.scala.geometry.{Rect, RealVector, SimpleVector}
import java.awt.{Image, Graphics}
import scala.collection.mutable
import java.awt.image.{RenderedImage, BufferedImage}
import main.scala.resourceFileUtils.{JsonMapper, AreaMapFileKeys}
import javax.imageio.ImageIO
import java.io.{BufferedWriter, FileWriter, File}
import javax.swing.{JOptionPane, ImageIcon}
import scala.util.parsing.json.{JSONArray, JSONObject}
import oracle.jrockit.jfr.settings.JSONElement
import main.scala.graphics.images._
import scala.util.parsing.json.JSONArray
import main.scala.graphics.images.SimpleImageRef
import scala.util.parsing.json.JSONObject
import scala.Some
import main.scala.geometry.SimpleVector
import main.scala.gameObjects.entity._
import main.scala.graphics.images.imageUtil.ImageUtil
import scala.collection.mutable.ListBuffer
import scala.util.parsing.json.JSONArray
import main.scala.graphics.images.SimpleImageRef
import scala.util.parsing.json.JSONObject
import main.scala.graphics.images.TiledImageRef
import scala.Some
import main.scala.geometry.SimpleVector
import scala.collection.immutable.HashMap
import sun.awt.image.ToolkitImage
import main.scala.gameControl.events.MyEvent
import main.scala.gameObjects.entity.traits.{Visible, Mobile}
import main.scala.gameObjects.entity.classes._
import main.scala.gameObjects.entity.traits.collisions.Colliding
import main.scala.graphics.images.SimpleImageRef
import main.scala.graphics.images.TiledImageRef
import scala.Some
import main.scala.gameObjects.entity.classes.Scenery
import scala.util.parsing.json.JSONArray
import scala.util.parsing.json.JSONObject
import main.scala.geometry.SimpleVector


class AreaMap(var resolution: SimpleVector = SimpleVector(800, 600))(var center: SimpleVector = resolution / 2, val startPosition: SimpleVector = resolution * 3 / 4) extends GameScreen {

  var originOfVisibleArea = center - (resolution / 2)

  val background: mutable.HashMap[String, Entity] = new mutable.HashMap[String, Entity]()

  val foreground: mutable.HashMap[String, Entity] = new mutable.HashMap[String, Entity]()

  val overlay: mutable.HashMap[String, Entity] = new mutable.HashMap[String, Entity]()

  var drawBackGround: Boolean = true
  var drawForeGround: Boolean = true
  var drawOverlay: Boolean = true
  var drawWalls: Boolean = false

  def moveViewTo(point: SimpleVector) {
    center = point
    originOfVisibleArea = center - (resolution / 2)
  }

  def moveViewBy(dist: SimpleVector) {
    center = center + dist
    originOfVisibleArea = center - (resolution / 2)
  }

  override def draw: Image = {
    val baseImage = new BufferedImage(resolution.x, resolution.y, BufferedImage.TYPE_INT_ARGB)
    val visibleEntities = {
      (if (drawBackGround) background.values else Nil) ++
        (if (drawForeGround) foreground.values else Nil) ++
        (if (drawOverlay) overlay.values else Nil)

    }.flatMap(_ match {
      case visibleEntity: Visible =>
        visibleEntity :: Nil
      case _ =>
        Nil
    })
    val adjustedPositionsAndImages = visibleEntities.map(entity => {
      for (position <- getAdjustedPosition(entity.location, entity.width, entity.height)) yield
        (position, entity.image)
    }).flatten
    ImageUtil.drawImages(adjustedPositionsAndImages.map(_._2).toList, adjustedPositionsAndImages.map(_._1).toList, baseImage)

    if (drawWalls) {
      val walls = {
        (if (drawBackGround) background.values else Nil) ++
          (if (drawForeGround) foreground.values else Nil) ++
          (if (drawOverlay) overlay.values else Nil)

      }.flatMap(_ match {
        case wall: Wall => wall :: Nil
        case _ => Nil
      })
      val adjustedRects = walls.map(wall => {
        wall.boundary - originOfVisibleArea
      })

      ImageUtil.drawRectangles(adjustedRects.toList, baseImage)

    }
    baseImage
  }


  override def update() {
    //Handle collisions by layer
    updateLayer(background.values.toList)
    updateLayer(foreground.values.toList)
    updateLayer(overlay.values.toList)
  }

  def updateLayer(layer: List[Entity]) {
    val movingEntities = layer.flatMap(_ match {
      case mobile: Mobile => mobile :: Nil
      case _ => Nil
    }).toList

    val collidingEntities = layer.flatMap(_ match {
      case colliding: Colliding => colliding :: Nil
      case _ => Nil
    }).toList

    for (movingEntity <- movingEntities) moveAndHandleCollisions(movingEntity, collidingEntities)
  }


  def moveAndHandleCollisions(movingEntity: Mobile, collidingEntities: List[Colliding]) {
    movingEntity.move()

    //Repeatedly resolve collisions until this entity collides with nothing.
    //If we loop more than 10 times through the list just give up and log whatever
    //we can't resolve
    if (movingEntity.isInstanceOf[Colliding]) {
      val handleCollisionEntities =
        for {otherEntity <- collidingEntities if (!(movingEntity == otherEntity))} yield
          if (movingEntity.asInstanceOf[Colliding].checkForAndHandleCollision(otherEntity))
            otherEntity
      if (handleCollisionEntities.size > 0)
        moveAndHandleCollisions(movingEntity.asInstanceOf[Colliding], handleCollisionEntities.asInstanceOf[List[Colliding]], 1)
    }
  }

  def moveAndHandleCollisions(movingEntity: Colliding, collidingEntities: List[Colliding], attempts: Int) {
    if (attempts >= 10) {
      System.out.println("Entity: " + movingEntity + " is still colliding with entities:")

      for {otherEntity <- collidingEntities if (!(movingEntity == otherEntity))}
        if (movingEntity.checkForAndHandleCollision(otherEntity))
          System.out.println("\t" + otherEntity)
    }
    //Repeatedly resolve collisions until this entity collides with nothing.
    //If we loop more than 10 times through the list just give up and log whatever
    //we can't resolve
    val handleCollisionEntities =
      for {otherEntity <- collidingEntities if (!(movingEntity == otherEntity))} yield
        if (movingEntity.checkForAndHandleCollision(otherEntity))
          otherEntity
    if (handleCollisionEntities.size > 0)
      moveAndHandleCollisions(movingEntity, handleCollisionEntities.asInstanceOf[List[Colliding]], attempts + 1)
  }

  private def getAdjustedPosition(point: SimpleVector, width: Int, height: Int): Option[SimpleVector] = {
    val adjustedPosition = point - originOfVisibleArea
    if (
      Rect(
        SimpleVector(adjustedPosition.x, adjustedPosition.y),
        SimpleVector(adjustedPosition.x + width, adjustedPosition.y - height)
      ) intersectsWith Rect(
        SimpleVector(0, resolution.y),
        SimpleVector(resolution.x, 0)
      )
    ) {
      Some[SimpleVector](adjustedPosition)
    }
    else
      None
  }

}


object AreaMap {

  import io.Source
  import io.Codec
  import AreaMapFileKeys._


  var imageStore: ImageStore = DefaultImageStore

  def loadFromFile(filePath: String): AreaMap = {
    val file = new File(filePath)
    val pathToResources = if (file.isDirectory)
      filePath + AreaMapFileKeys.ImagesDirectoryPath
    else
      "src/main/resources/"

    val lines = if (file.isDirectory)
      Source.fromFile(filePath + AreaMapFileKeys.MapDataFile).getLines
    else
      Source.fromFile(filePath).getLines
    val areaMap = new AreaMap()()

    for {line <- lines
         jsonObject <- util.parsing.json.JSON.parseRaw(line)
    } jsonObject match {
      case (mapJson: JSONObject) => {

        areaMap.resolution = getSimpleVectorFromJson(mapJson, Resolution) match {
          case Some(res: SimpleVector) => res
          case _ => areaMap.resolution
        }
        areaMap.center = getSimpleVectorFromJson(mapJson, Center) match {
          case Some(center: SimpleVector) => center
          case _ => areaMap.center
        }

        mapJson.obj.get(AreaMapFileKeys.Scenery) match {
          case Some(entitiesJson: JSONArray) => fillEntities(entitiesJson, areaMap, AreaMapFileKeys.Scenery, pathToResources)
          case _ =>
        }

        mapJson.obj.get(AreaMapFileKeys.Wall) match {
          case Some(entitiesJson: JSONArray) => fillEntities(entitiesJson, areaMap, AreaMapFileKeys.Wall, pathToResources)
          case _ =>
        }

      }
      case _ =>
    }
    areaMap
  }

  def fillEntities(entitiesJson: JSONArray, areaMap: AreaMap, entityType: String, pathToResources: String): Unit = {
    entitiesJson.list.foreach(_ match {
      case (entityJson: JSONObject) => entityJson.obj.get(Layer) match {
        case Some(layerVal) if (layerVal == LayerVal.Foreground) =>
          putEntity(areaMap.foreground, entityJson, entityType, pathToResources)
        case Some(layerVal) if (layerVal == LayerVal.Background) =>
          putEntity(areaMap.background, entityJson, entityType, pathToResources)
        case Some(layerVal) if (layerVal == LayerVal.Overlay) =>
          putEntity(areaMap.overlay, entityJson, entityType, pathToResources)
      }
      case _ =>
    })
  }

  def putEntity(entityMap: mutable.HashMap[String, Entity], entityJson: JSONObject, entityType: String, pathToResources: String) {
    entityJson.obj.get(ImageFileKey).flatMap {
      case (fileName: String) =>
        storeImageAndReturnEntity(fileName, entityJson, entityType, pathToResources).flatMap {
          case (entity: Entity) =>
            entityJson.obj.get(EntityId) match {
              case Some(imageId: String) => entityMap.put(imageId, entity)
              case _ => entityMap.put(fileName, entity)
            }
        }
    }
  }

  def storeImageAndReturnEntity(fileName: String, entityJson: JSONObject, entityType: String, pathToResources: String): Option[Entity] = {
    fileName match {
      case NoImage =>
      case _ => imageStore.storeImage(fileName, pathToResources + fileName)
    }

    for {location <- getSimpleVectorFromJson(entityJson, VectorType.Location)
         imageType <- entityJson.obj.get(ImageType)
         if (imageType.isInstanceOf[String])
         entity <- getEntity(imageType.asInstanceOf[String], entityJson, location, fileName, entityType)
    } yield entity
  }

  def getEntity(imageType: String, entityJson: JSONObject, location: SimpleVector, fileName: String, entityType: String): Option[Entity] = {

    entityType match {
      case AreaMapFileKeys.Scenery => imageType match {
        case ImageTypeVal.TiledImage =>
          for {size <- getSimpleVectorFromJson(entityJson, VectorType.Size)
               entityId <- entityJson.obj.get(EntityId)
               if (entityId.isInstanceOf[String])} yield
            new Scenery(location, TiledImageRef(size.x, size.y, fileName, imageStore), entityId.asInstanceOf[String])
        case ImageTypeVal.SimpleImage =>
          for {entityId <- entityJson.obj.get(EntityId) if (entityId.isInstanceOf[String])} yield
            new Scenery(location, SimpleImageRef(fileName, imageStore), entityId.asInstanceOf[String])
        case _ => None
      }


      case AreaMapFileKeys.Wall =>
        for {size <- getSimpleVectorFromJson(entityJson, VectorType.Size)
             entityId <- entityJson.obj.get(EntityId)
             if (entityId.isInstanceOf[String])} yield
          imageType match {
            case ImageTypeVal.TiledImage =>
              new VisibleWall(location, size, TiledImageRef(size.x, size.y, fileName, imageStore), entityId.asInstanceOf[String])
            case ImageTypeVal.SimpleImage => new VisibleWall(location, size, SimpleImageRef(fileName, imageStore), entityId.asInstanceOf[String])
            case AreaMapFileKeys.NoImage => new Wall(location, size, entityId.asInstanceOf[String])

          }

      case AreaMapFileKeys.Character =>
        for {entityId <- entityJson.obj.get(EntityId) if (entityId.isInstanceOf[String])} yield {
          val character = new CharacterEntity(fileName)
          character.location = location
          character
        }
      case _ => None
    }

  }

  def getSimpleVectorFromJson(json: JSONObject, key: String): Option[SimpleVector] = {
    for {jsonVector <- json.obj.get(key)
         if (jsonVector.isInstanceOf[JSONObject])
         simpleVector <- JsonMapper.simpleVectorFromJson(jsonVector.asInstanceOf[JSONObject])
    } yield simpleVector
  }


  def exportToJson(fileName: String, areaMap: AreaMap) {

    val imagesDirectory: File = new File(fileName + AreaMapFileKeys.ImagesDirectoryPath)
    imagesDirectory.mkdirs()
    for (imageAndFileName <- imageStore.getAllImages()) {
      val file = new File(imagesDirectory.getPath + "/" + imageAndFileName._1)

      ImageIO.write(imageAndFileName._2 match {
        case toolkitImage: ToolkitImage => {
          val bufferedImage = new BufferedImage(toolkitImage.getWidth(), toolkitImage.getHeight(), BufferedImage.TYPE_INT_ARGB)
          bufferedImage.getGraphics.drawImage(toolkitImage, 0, 0, null)

          bufferedImage.getGraphics.dispose()
          bufferedImage
        }
        case anyImage: Image => anyImage.asInstanceOf[RenderedImage]
      },
        imageAndFileName._1.split("[.]")(1),
        file
      )

    }
    val output = new BufferedWriter(new FileWriter(fileName + AreaMapFileKeys.MapDataFile))
    output.write(new JSONObject(
      new HashMap[String, Any] + ((Resolution, JsonMapper.toJson(areaMap.resolution)))
    ).toString() + "\n")

    output.write(new JSONObject(
      new HashMap[String, Any] + ((Center, JsonMapper.toJson(areaMap.center)))
    ).toString() + "\n")

    //Export Background entities
    output.write(new JSONObject(
      new HashMap[String, Any] + ((AreaMapFileKeys.Scenery, new JSONArray(
        areaMap.background.values.toList.flatMap {
          _ match {
            case (scenery: Scenery) =>
              JsonMapper.toJson(scenery, AreaMapFileKeys.LayerVal.Background) :: Nil
            case _ => Nil
          }
        }
      )))
    ).toString() + "\n")

    output.write(new JSONObject(
      new HashMap[String, Any] + ((AreaMapFileKeys.Wall, new JSONArray(
        areaMap.background.values.toList.flatMap {
          _ match {
            case (wall: Wall) =>
              JsonMapper.toJson(wall, AreaMapFileKeys.LayerVal.Background) :: Nil
            case _ => Nil
          }
        }
      )))
    ).toString() + "\n")

    //Export Foreground entities
    output.write(new JSONObject(
      new HashMap[String, Any] + ((AreaMapFileKeys.Scenery, new JSONArray(
        areaMap.foreground.values.toList.flatMap {
          _ match {
            case (scenery: Scenery) =>
              JsonMapper.toJson(scenery, AreaMapFileKeys.LayerVal.Foreground) :: Nil
            case _ => Nil
          }
        }
      )))
    ).toString() + "\n")

    output.write(new JSONObject(
      new HashMap[String, Any] + ((AreaMapFileKeys.Wall, new JSONArray(
        areaMap.foreground.values.toList.flatMap {
          _ match {
            case (wall: Wall) =>
              JsonMapper.toJson(wall, AreaMapFileKeys.LayerVal.Foreground) :: Nil
            case _ => Nil
          }
        }
      )))
    ).toString() + "\n")

    //Export Overlay Entities
    output.write(new JSONObject(
      new HashMap[String, Any] + ((AreaMapFileKeys.Scenery, new JSONArray(
        areaMap.overlay.values.toList.flatMap {
          _ match {
            case (scenery: Scenery) =>
              JsonMapper.toJson(scenery, AreaMapFileKeys.LayerVal.Overlay) :: Nil
            case _ => Nil
          }
        }
      )))
    ).toString() + "\n")

    output.write(new JSONObject(
      new HashMap[String, Any] + ((AreaMapFileKeys.Wall, new JSONArray(
        areaMap.overlay.values.toList.flatMap {
          _ match {
            case (wall: Wall) =>
              JsonMapper.toJson(wall, AreaMapFileKeys.LayerVal.Overlay) :: Nil
            case _ => Nil
          }
        }
      )))
    ).toString() + "\n")


    output.flush()
    output.close()
  }

}
