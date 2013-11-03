package main.scala.graphics.images

import scala.collection.mutable.HashMap
import java.awt.{Toolkit, Image}
import javax.swing.ImageIcon
import javax.imageio.ImageIO
import java.io.File
import java.net.URL

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 3:04 AM
 * To change this template use File | Settings | File Templates.
 */
object DefaultImageStore extends ImageStore {
  val images : HashMap[String, Image] = new HashMap[String, Image]()
  override def getImage(id : String) : Option[Image] = {
    for (image <- images.get(id)) yield image
  }

  override def storeImage(key : String, imagePath: String) : String = {

    images.put(key, new ImageIcon(imagePath).getImage)
    key
  }

  override def storeImage(key : String, image: Image) : String = {
    images.put(key, image)
    key
  }

  override def getAllImages(): List[(String, Image)] = {
    images.toList
  }
}
