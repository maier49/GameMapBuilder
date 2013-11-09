package main.scala.graphics.images.imageUtil

import java.awt._
import main.scala.geometry.{Rect, SimpleVector}
import main.scala.graphics.images.{SectionImageRef, MyImageRef}
import java.awt.image.ImageObserver
import javax.swing.{ImageIcon, JLabel, JPanel}
import sun.awt.image.ToolkitImage
import scala.List
import main.scala.geometry.Rect
import scala.Some
import main.scala.geometry.SimpleVector

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/21/13
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
object ImageUtil {
  def drawImage(image: Image, location: SimpleVector, graphics: Graphics, targetHeight: Int): Boolean = {
    val success = graphics.drawImage(image, location.x, targetHeight - location.y, null)
    success
  }


  def drawImages(images: List[Image], locations: List[SimpleVector], graphics: Graphics, targetHeight: Int): Boolean = {
    if (images.isEmpty)
      false
    else {
      val success = for ((image, location) <- images zip locations) yield {
        graphics.drawImage(image, location.x, targetHeight - location.y, null) || image.isInstanceOf[ToolkitImage]
      }
      success.reduceLeft(_ && _)
    }
  }

  def drawRectangles(rectangles: List[Rect], graphics: Graphics, targetHeight: Int, color: Color = Color.BLACK): Boolean = {
    if (rectangles.isEmpty)
      false
    else {
      val oldGraphics = if (graphics.isInstanceOf[Graphics2D])
        Some(
          graphics.asInstanceOf[Graphics2D].getColor,
          graphics.asInstanceOf[Graphics2D].getStroke
        )
      else
        None


      for (exists <- oldGraphics) {
        graphics.asInstanceOf[Graphics2D].setStroke(new BasicStroke(5f))
        graphics.setColor(color)
      }
      for (rectangle <- rectangles)
        graphics.drawRect(
          rectangle.topLeft.x,
          targetHeight - rectangle.topLeft.y,
          rectangle.bottomRight.x - rectangle.topLeft.x,
          rectangle.topLeft.y - rectangle.bottomRight.y
        )


      for (original <- oldGraphics) {
        graphics.asInstanceOf[Graphics2D].setStroke(original._2)
        graphics.asInstanceOf[Graphics2D].setColor(original._1)
      }

      true
    }
  }


}
