package main.scala.mapbuilder

import scala.swing._
import java.awt.{BasicStroke, Color, BorderLayout, Image}
import sun.print.PrinterGraphicsConfig
import javax.swing.{SwingUtilities, JFrame}
import main.scala.geometry.{Rect, SimpleVector}
import scala.swing.event.{MouseReleased, MousePressed}
import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 11/1/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
class ImageSelectPanel(val parentRef: OptionsToolBar, private var _image: Image) extends Dialog(null) {
  title = "Selected Image"

  def image = {
    if (!cropped)
      _image
    else {
      val croppedImage = new BufferedImage(cropArea.width, cropArea.height, BufferedImage.TYPE_INT_ARGB)
      croppedImage.getGraphics.drawImage(
        _image,
        0,
        0,
        croppedImage.getWidth,
        croppedImage.getHeight,
        cropArea.x,
        cropArea.y,
        cropArea.x + cropArea.width,
        cropArea.y + cropArea.height,
        null
      )
      croppedImage.getGraphics.dispose()
      croppedImage
    }

  }

  private var pressedAt: SimpleVector = SimpleVector(0, 0)
  private var releasedAt: SimpleVector = SimpleVector(0, 0)
  private var cropArea: Rectangle = null
  private var cropped = false

  val panel = new Panel() {
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

  listenTo(panel.mouse.clicks)

  reactions += {
    case e: MousePressed =>
      if (SwingUtilities.isRightMouseButton(e.peer)) {
        cropped = false
        repaint()
      } else {
        pressedAt = SimpleVector(e.peer.getX, e.peer.getY)
      }
    case e: MouseReleased =>
      if (SwingUtilities.isLeftMouseButton(e.peer)) {
        releasedAt = SimpleVector(e.peer.getX, e.peer.getY)
        if ((releasedAt - pressedAt).magnitude > 10) {
          cropArea = new Rectangle(
            Math.min(releasedAt.x, pressedAt.x),
            Math.min(releasedAt.y, pressedAt.y),
            Math.abs(releasedAt.x - pressedAt.x),
            Math.abs(releasedAt.y - pressedAt.y)
          )

          cropped = true
          repaint()
        }

      }

    case _ =>
  }

  def image_=(image: Image) {
    _image = image
    preferredSize = new Dimension(image.getWidth(null), image.getHeight(null))
    panel.preferredSize = new Dimension(image.getWidth(null), image.getHeight(null))
  }

  def drawCropRectangle(graphics: Graphics2D) {
    val oldGraphics = (
      graphics.getColor,
      graphics.getStroke
      )

    graphics.setColor(Color.RED)


    graphics.setStroke(
      new BasicStroke(5f)
    )


    graphics.drawRect(cropArea.x, cropArea.y, cropArea.x + cropArea.width, cropArea.y + cropArea.height)

    graphics.setColor(oldGraphics._1)
    graphics.setStroke(oldGraphics._2)

  }

}
