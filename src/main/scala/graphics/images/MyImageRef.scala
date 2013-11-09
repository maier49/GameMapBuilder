package main.scala.graphics.images

import java.awt.{Graphics, Image}
import java.awt
import main.scala.geometry.SimpleVector

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class MyImageRef {
  def image: awt.Image

  /**
   * An alternative to returning the image, this image simply renders itself onto the provided graphics object.
   * This can be much more memory efficient, particularly in the case of tiled images representing a large area
   * @param graphics
   */
  def draw(graphics: Graphics, location: SimpleVector)
}
