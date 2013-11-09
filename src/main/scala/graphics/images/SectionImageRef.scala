package main.scala.graphics.images

import main.scala.geometry.SimpleVector
import java.awt.{Graphics, Image}
import java.awt.image.BufferedImage
import sun.awt.image.ToolkitImage

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
case class SectionImageRef(imageId: String, upperLeft: SimpleVector, bottomRight: SimpleVector, imageStore: ImageStore) extends MyImageRef {

  override def image: Image = {

    val imageOption = for {storedImage <- imageStore.getImage(imageId)} yield {
      val newImage = new BufferedImage(storedImage.getWidth(null), storedImage.getHeight(null), BufferedImage.TYPE_INT_ARGB)
      newImage.getGraphics.drawImage(storedImage, 0, 0, null)
      newImage.getSubimage(upperLeft.x, newImage.getHeight - upperLeft.y, bottomRight.x - upperLeft.x, upperLeft.y - bottomRight.y)
    }

    imageOption.getOrElse(new BufferedImage(0,0,BufferedImage.TYPE_INT_RGB))


  }

  override def draw(graphics: Graphics, location: SimpleVector) {
    val width = bottomRight.x - upperLeft.x
    val height = upperLeft.y - bottomRight.y
    for (storedImage <- imageStore.getImage(imageId)) yield {

      graphics.drawImage(
        storedImage,
        location.x,
        location.y,
        location.x + width,
        location.y + height,
        upperLeft.x,
        storedImage.getHeight(null) - upperLeft.y,
        bottomRight.x,
        storedImage.getHeight(null) - bottomRight.y,
        null
      )
    }
  }
}
