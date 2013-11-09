package main.scala.mapbuilder

import scala.swing._
import main.scala.geometry.SimpleVector
import java.awt.Dimension
import main.scala.geometry.SimpleVector
import scala.swing.event.MouseClicked
import main.scala.gameControl.AreaMap

class MapEditPanel extends Panel {
  private var _resolution: SimpleVector = SimpleVector(500,500)

  def resolution = _resolution

  def resolution_=(resolution: SimpleVector) {
    _resolution = resolution
    preferredSize = new Dimension(_resolution.x, _resolution.y)
    areaMap.center = _resolution/2
    revalidate()
  }

  var areaMap : AreaMap = new AreaMap(_resolution/2)
  peer.setSize(new Dimension(_resolution.x,_resolution.y))
  override def paintComponent(graphics: Graphics2D) {
    super.paintComponent(graphics)
    areaMap.draw(graphics, _resolution)
  }

  override def paint(graphics: Graphics2D) {
    super.paint(graphics)
    areaMap.draw(graphics, _resolution)
  }

}
