package main.scala.mapbuilder

import scala.swing._
import java.awt.Dimension
import javax.imageio.ImageIO
import scala.swing.event.{WindowDeactivated, WindowClosing, WindowClosed}
import main.scala.gameControl.AreaMap

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
        map.preferredSize = new Dimension(map.areaMap.resolution.x, map.areaMap.resolution.y)
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

}
