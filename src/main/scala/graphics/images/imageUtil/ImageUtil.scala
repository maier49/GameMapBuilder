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
  def drawImage(image: Image, location: SimpleVector, targetImage: Image): Boolean = {
    val success = targetImage.getGraphics.drawImage(image, location.x, targetImage.getHeight(null) - location.y, null)
    targetImage.getGraphics.dispose()
    success
  }

  def drawImages(images: List[Image], locations: List[SimpleVector], targetImage: Image): Boolean = {
    if (images.isEmpty)
      false
    else {
      val graphics = targetImage.getGraphics
      val success = for ((image, location) <- images zip locations) yield {
        graphics.drawImage(image, location.x, targetImage.getHeight(null) - location.y, null) || image.isInstanceOf[ToolkitImage]
      }

      graphics.dispose()
      success.reduceLeft(_ && _)
    }
  }

  def drawRectangles(rectangles: List[Rect], targetImage: Image): Boolean = {
    if (rectangles.isEmpty)
      false
    else {
      val graphics = targetImage.getGraphics
      val oldStroke = if (graphics.isInstanceOf[Graphics2D])
        Some(graphics.asInstanceOf[Graphics2D].getStroke)
      else
        None

      for (stroke <- oldStroke) {
        graphics.asInstanceOf[Graphics2D].setStroke(new BasicStroke(5f))
        graphics.setColor(Color.BLACK)
      }
      for (rectangle <- rectangles)
        graphics.drawRect(
          rectangle.topLeft.x,
          targetImage.getHeight(null) - rectangle.topLeft.y,
          rectangle.bottomRight.x - rectangle.topLeft.x,
         rectangle.topLeft.y - rectangle.bottomRight.y
        )


      for (stroke <- oldStroke) {
        graphics.asInstanceOf[Graphics2D].setStroke(stroke)
      }

      graphics.dispose()
      true
    }
  }


}
