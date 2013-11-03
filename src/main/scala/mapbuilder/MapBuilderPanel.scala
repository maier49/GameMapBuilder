package main.scala.mapbuilder

import scala.swing.{Orientation, Label, ScrollPane, BorderPanel}
import java.awt.{Image, Dimension}
import scala.swing.event._
import java.util.UUID
import main.scala.graphics.images.{TiledImageRef, SimpleImageRef, DefaultImageStore}
import main.scala.geometry.{Rect, RealVector, SimpleVector}
import scala.swing.event.MousePressed
import main.scala.graphics.images.SimpleImageRef
import scala.swing.event.MouseDragged
import scala.swing.event.MouseReleased
import main.scala.graphics.images.TiledImageRef
import scala.collection.parallel.mutable
import main.scala.gameObjects.entity.classes.{Entity, Scenery, Wall}
import javax.swing.SwingUtilities

class MapBuilderPanel(val map: MapEditPanel) extends BorderPanel {
  val DragThreshold = 10
  val editOptionsPanel = new EditOptionsPanel(map, Orientation.Vertical)
  val optionsToolbar = new OptionsToolBar(map, editOptionsPanel.updateResolution :: Nil)
  private var pressedAt: SimpleVector = SimpleVector(0, 0)
  private var releasedAt: SimpleVector = SimpleVector(0, 0)

  layout(new ScrollPane(map)) = BorderPanel.Position.Center
  layout(editOptionsPanel) = BorderPanel.Position.West
  layout(optionsToolbar) = BorderPanel.Position.North
  optionsToolbar.visible = true
  preferredSize = new Dimension(800, 800)

  listenTo(map.mouse.clicks)

  reactions += {
    case e: MousePressed =>
      pressedAt = SimpleVector(e.peer.getX, map.areaMap.resolution.y - e.peer.getY)
    case e: MouseReleased =>
      releasedAt = SimpleVector(e.peer.getX, map.areaMap.resolution.y - e.peer.getY)
      editOptionsPanel.imagesOrWalls match {
        case value if value == editOptionsPanel.AddWalls =>
          if (SwingUtilities.isRightMouseButton(e.peer))
            removeWallsFromMap()
          else if (SwingUtilities.isLeftMouseButton(e.peer))
            addWallToMap(releasedAt - pressedAt)
        case value if value == editOptionsPanel.AddImages =>
          if (SwingUtilities.isLeftMouseButton(e.peer))
            for (selectedImagePanel <- optionsToolbar.imageSelection) {
              addImageToMap(selectedImagePanel.image, releasedAt - pressedAt)
            }
          else if (SwingUtilities.isRightMouseButton(e.peer))
            removeImagesFromMap()
      }
      repaint()

    case _ =>
  }

  private def addWallToMap(dragVector: SimpleVector) {
    if (dragVector.magnitude > DragThreshold)
      (editOptionsPanel.selectedLayer match {
        case value if value == editOptionsPanel.Background => map.areaMap.background
        case value if value == editOptionsPanel.Foreground => map.areaMap.foreground
        case value if value == editOptionsPanel.Overlay => map.areaMap.overlay
        case _ => map.areaMap.background
      }).put(
        UUID.randomUUID().toString,
        new Wall(
          SimpleVector(
            Math.min(releasedAt.x, pressedAt.x),
            Math.min(releasedAt.y, pressedAt.y)
          ),
          SimpleVector(
            Math.max(releasedAt.x, pressedAt.x) - Math.min(releasedAt.x, pressedAt.x),
            Math.max(releasedAt.y, pressedAt.y) - Math.min(releasedAt.y, pressedAt.y)
          ),
          UUID.randomUUID().toString
        )
      )
  }

  private def addImageToMap(image: Image, dragVector: SimpleVector) {
    val imageKey = DefaultImageStore.storeImage(UUID.randomUUID().toString + ".png", image)

    val scenery: Scenery = if (dragVector.magnitude > DragThreshold) {
      val imageRef = new TiledImageRef(Math.abs(dragVector.x), Math.abs(dragVector.y), imageKey, DefaultImageStore)
      new Scenery(
        SimpleVector(Math.min(releasedAt.x, pressedAt.x), Math.max(releasedAt.y, pressedAt.y)),
        imageRef,
        UUID.randomUUID().toString
      )
    } else {
      val imageRef = new SimpleImageRef(imageKey, DefaultImageStore)
      new Scenery(pressedAt, imageRef, UUID.randomUUID().toString)
    }

    editOptionsPanel.selectedLayer match {
      case value if value == editOptionsPanel.Background => map.areaMap.background.put(UUID.randomUUID().toString, scenery)
      case value if value == editOptionsPanel.Foreground => map.areaMap.foreground.put(UUID.randomUUID().toString, scenery)
      case value if value == editOptionsPanel.Overlay => map.areaMap.overlay.put(UUID.randomUUID().toString, scenery)
    }
  }

  def removeWallsFromMap() {

    val dragBoundary = Rect(
      SimpleVector(Math.min(pressedAt.x, releasedAt.x), Math.max(pressedAt.y, releasedAt.y)),
      SimpleVector(Math.max(pressedAt.x, releasedAt.x), Math.min(pressedAt.y, releasedAt.y))
    )

    editOptionsPanel.selectedLayer match {
      case value if value == editOptionsPanel.Background =>
        val toRemove = map.areaMap.background.filter(
          (entry: (_, Entity)) => (
            (entry._2.isInstanceOf[Wall]) &&
              (entry._2.asInstanceOf[Wall].boundary intersectsWith dragBoundary)

            )
        )
        toRemove.foreach(_ match {
          case ((key: String, _)) => map.areaMap.background.remove(key)
        })
      case value if value == editOptionsPanel.Foreground =>
        val toRemove = map.areaMap.foreground.filter(
          (entry: (_, Entity)) => (
            (entry._2.isInstanceOf[Wall]) &&
              (entry._2.asInstanceOf[Wall].boundary intersectsWith dragBoundary)

            )
        )
        toRemove.foreach(_ match {
          case ((key: String, _)) => map.areaMap.foreground.remove(key)
        })

      case value if value == editOptionsPanel.Overlay =>
        val toRemove = map.areaMap.overlay.filter(
          (entry: (_, Entity)) => (
            (entry._2.isInstanceOf[Wall]) &&
              (entry._2.asInstanceOf[Wall].boundary intersectsWith dragBoundary)

            )
        )
        toRemove.foreach(_ match {
          case ((key: String, _)) => map.areaMap.overlay.remove(key)
        })

    }
  }

  def removeImagesFromMap() {
    val dragBoundary = Rect(
      SimpleVector(Math.min(pressedAt.x, releasedAt.x), Math.max(pressedAt.y, releasedAt.y)),
      SimpleVector(Math.max(pressedAt.x, releasedAt.x), Math.min(pressedAt.y, releasedAt.y))
    )

    editOptionsPanel.selectedLayer match {
      case value if value == editOptionsPanel.Background =>
        val toRemove = map.areaMap.background.filter(
          (entry: (_, Entity)) => (
            !(entry._2.isInstanceOf[Wall]) &&
              (Rect(entry._2.location, entry._2.location + SimpleVector(1,-1)) intersectsWith dragBoundary)
            )
        )
        toRemove.foreach(_ match {
          case ((key: String, _)) => map.areaMap.background.remove(key)
        })
      case value if value == editOptionsPanel.Foreground =>
        val toRemove = map.areaMap.foreground.filter(
          (entry: (_, Entity)) => (
            !(entry._2.isInstanceOf[Wall]) &&
              (Rect(entry._2.location, entry._2.location + SimpleVector(1,-1)) intersectsWith dragBoundary)
            )
        )
        toRemove.foreach(_ match {
          case ((key: String, _)) => map.areaMap.foreground.remove(key)
        })

      case value if value == editOptionsPanel.Overlay =>
        val toRemove = map.areaMap.overlay.filter(
          (entry: (_, Entity)) => (
            !(entry._2.isInstanceOf[Wall]) &&
              !(Rect(entry._2.location, entry._2.location + SimpleVector(1,-1)) intersectsWith dragBoundary)
            )
        )
        toRemove.foreach(_ match {
          case ((key: String, _)) => map.areaMap.overlay.remove(key)
        })

    }

  }

}
