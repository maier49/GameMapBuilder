package main.scala.graphics.images

import main.scala.geometry.Rect
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.ImageIcon

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
case class TiledImageRef(width : Int, height: Int, tileImageFileKey : String, store : ImageStore) extends MyImageRef{


  override def image: Image = {
    store.getImage(tileImageFileKey) match {
      case Some(tile) => {


        val imageWidth = tile.getWidth(null)
        val imageHeight = tile.getHeight(null)
        assert(imageWidth != 0)
        assert(imageHeight != 0)

        val tilesX = width / imageWidth
        val tilesY = height / imageHeight

        val tiledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val tiledImageG = tiledImage.getGraphics

        val lastTileYOffset = if (tilesY * imageHeight < height) 1 else 0
        for (x <- 0 to tilesX)
          for (y <- 1 to (tilesY + lastTileYOffset))
            tiledImageG.drawImage(tile, x * imageWidth, height - (y * imageHeight), null)



        tiledImage
      }
      case None => new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    }

  }


}
