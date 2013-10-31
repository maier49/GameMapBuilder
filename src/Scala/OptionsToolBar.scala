package Scala

import scala.swing._
import java.awt.Dimension
import main.scala.gameController.AreaMap

class OptionsToolBar(val map: MapEditPanel, val mapLoadCallBacks: List[() => Unit]) extends MenuBar {
  val toolBarChooser = new FileChooser()
  toolBarChooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
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
        val widthDiff = map.areaMap.resolution.x - map.preferredSize.getWidth.toInt
        val heightDiff = map.areaMap.resolution.y - map.preferredSize.getHeight.toInt
        map.preferredSize = new Dimension(map.areaMap.resolution.x, map.areaMap.resolution.y)
      }
    )
  }
}
