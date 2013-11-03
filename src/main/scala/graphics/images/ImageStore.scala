package main.scala.graphics.images

import java.awt.Image
import javax.swing.ImageIcon

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 3:03 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class ImageStore {
  def getImage(id: String): Option[Image]

  def storeImage(key: String, imagePath: String): String

  def storeImage(key: String, image: Image): String
  def getAllImages(): List[(String, Image)]

}
