package main.scala.mapbuilder

import scala.swing._
import java.awt.{BorderLayout, Image}
import sun.print.PrinterGraphicsConfig
import javax.swing.JFrame

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 11/1/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
class ImageSelectPanel(val parentRef: OptionsToolBar, private var _image: Image) extends Dialog(null) {
  title = "Selected Image"
  def image = _image
  val panel = new BorderPanel() {
    preferredSize = new Dimension(image.getWidth(null), image.getHeight(null))

    override def paintComponent(graphics: Graphics2D) {
      super.paintComponent(graphics)
      graphics.drawImage(image, 0, 0, null)
    }

    override def paint(graphics: Graphics2D) {
      super.paint(graphics)
      graphics.drawImage(image, 0, 0, null)
    }
  }

  contents = panel
  preferredSize = new Dimension(image.getWidth(null), image.getHeight(null))

  pack()
  visible = true

  def image_=(image: Image) {
    _image = image
    preferredSize = new Dimension(image.getWidth(null), image.getHeight(null))
    panel.preferredSize = new Dimension(image.getWidth(null), image.getHeight(null))
  }


}
