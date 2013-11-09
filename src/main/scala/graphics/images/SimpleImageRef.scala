package main.scala.graphics.images

import java.awt.{Graphics, Image}
import java.awt.image.BufferedImage
import main.scala.geometry.SimpleVector

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 12:48 AM
 * To change this template use File | Settings | File Templates.
 */
case class SimpleImageRef(jImage : String, store : ImageStore) extends MyImageRef{
  override def image : Image = store.getImage(jImage) match {
    case Some(image) => image
    case None => new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)
  }

  override def draw(graphics: Graphics, location: SimpleVector) {
      graphics.drawImage(image, location.x, location.y, null)
  }

}
