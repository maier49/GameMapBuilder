package main.scala.mapbuilder

import scala.swing._
import main.scala.geometry.SimpleVector
import java.awt.Dimension
import main.scala.geometry.SimpleVector
import scala.swing.event.MouseClicked
import main.scala.gameControl.AreaMap

class MapEditPanel extends Panel {
  var areaMap : AreaMap = new AreaMap(SimpleVector(500,500))()
  peer.setSize(new Dimension(500,500))
  override def paintComponent(graphics: Graphics2D) {
    super.paintComponent(graphics)
    graphics.drawImage(areaMap.draw, 0, 0, null)
  }

  override def paint(graphics: Graphics2D) {
    super.paint(graphics)
    graphics.drawImage(areaMap.draw, 0, 0, null)
  }

}
