package main.scala.mapbuilder

import scala.swing._
import java.awt.{GridLayout, Dimension}
import javax.imageio.ImageIO
import scala.swing.event.{WindowDeactivated, WindowClosing, WindowClosed}
import main.scala.gameControl.AreaMap
import javax.swing.{JOptionPane, JLabel, JPanel}
import java.util.UUID
import main.scala.graphics.images.{DefaultImageStore, TiledImageRef}
import main.scala.gameObjects.entity.classes.Scenery
import main.scala.geometry.SimpleVector

class OptionsToolBar(val map: MapEditPanel, val mapLoadCallBacks: List[() => Unit]) extends MenuBar {
  val toolBarChooser = new FileChooser()
  toolBarChooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
  val imageChooser = new FileChooser()
  var imageSelection: Option[ImageSelectPanel] = None
  preferredSize = new Dimension(800, 100)

  contents += new Menu("File"){

    contents += new MenuItem(
      Action("Save") {
        toolBarChooser.showSaveDialog(this)
        AreaMap.exportToJson(toolBarChooser.selectedFile.getAbsolutePath, map.areaMap)
      })
    contents += new MenuItem(
      Action("Open") {
        toolBarChooser.showOpenDialog(this)
        map.areaMap = AreaMap.loadFromFile(toolBarChooser.selectedFile.getAbsolutePath)
        for (callBack <- mapLoadCallBacks) callBack()
        map.preferredSize = new Dimension(map.resolution.x, map.resolution.y)
      }
    )
  }


  contents += new Button(Action("Load Image") {
      imageChooser.showOpenDialog(this)

      imageSelection match {
        case None => {
          val panel = new ImageSelectPanel(this, ImageIO.read(imageChooser.selectedFile))
          imageSelection = Some(panel)
          listenTo(panel)
          reactions += {
            case WindowClosing(`panel`) =>
            imageSelection = None
          }
        }
        case Some(imageSelectionPanel) => imageSelectionPanel.image = ImageIO.read(imageChooser.selectedFile)
      }

    imageSelection.get.repaint()

  })


  contents += new Button(Action("Tile with loaded image") {
    if (imageSelection.getOrElse(false) != false) {
      val panel = new JPanel(new GridLayout(0, 2))
      panel.add(new JLabel("This will tile the selected image across the entire background layer. Are you sure?"))


      val result = JOptionPane.showConfirmDialog(null, panel, "Tile Image", JOptionPane.OK_CANCEL_OPTION)
      if (result == JOptionPane.OK_OPTION) {
        val imageKey = DefaultImageStore.storeImage(UUID.randomUUID().toString + ".png", imageSelection.get.image)
        map.areaMap.background.put(UUID.randomUUID().toString,
        new Scenery(
          SimpleVector(0, map.resolution.y),
          TiledImageRef(map.resolution.x, map.resolution.y, imageKey, DefaultImageStore)
        ))
        map.repaint()
      }
    }

  })

}
