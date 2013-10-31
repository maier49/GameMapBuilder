package Scala


import swing._
import java.awt.{Dimension, Color, Graphics}
import main.scala.gameController.AreaMap
import main.scala.geometry.SimpleVector
import scala.swing.Dimension

/**
 * A Simple UI for building maps
 */

object Main extends SimpleSwingApplication {

  val map = new MapEditPanel()


  object SaveDialog extends Dialog {
    preferredSize = new Dimension(200, 200)
  }

  def top = new MainFrame {
    title = "Hello World!"
    contents = new MapBuilderPanel(map)
  }


}