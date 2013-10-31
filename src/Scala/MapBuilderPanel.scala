package Scala

import scala.swing.{Orientation, Label, ScrollPane, BorderPanel}
import java.awt.Dimension

class MapBuilderPanel(val map: MapEditPanel) extends BorderPanel {
  val editOptionsPanel = new EditOptionsPanel(map, Orientation.Vertical)
  layout(new ScrollPane(map)) = BorderPanel.Position.Center
  layout(editOptionsPanel) = BorderPanel.Position.West
  val optionsToolbar = new OptionsToolBar(map, editOptionsPanel.updateResolution :: Nil)
  layout(optionsToolbar) = BorderPanel.Position.North
  optionsToolbar.visible = true
  preferredSize = new Dimension(800, 800)
}
