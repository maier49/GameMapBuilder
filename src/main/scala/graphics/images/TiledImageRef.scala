package main.scala.graphics.images

import main.scala.geometry.{SimpleVector, Rect}
import java.awt.{Graphics, Image}
import java.awt.image.BufferedImage
import javax.swing.ImageIcon

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
case class TiledImageRef(width: Int, height: Int, tileImageFileKey: String, store: ImageStore) extends MyImageRef {

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


  override def draw(graphics: Graphics, location: SimpleVector) {
    for (tile <- store.getImage(tileImageFileKey)) {
      val imageWidth = tile.getWidth(null)
      val imageHeight = tile.getHeight(null)

      assert(imageWidth != 0)
      assert(imageHeight != 0)

      val tilesX = width / imageWidth
      val tilesY = height / imageHeight

      for (x <- 0 to tilesX - 1)
        for (y <- 0 to tilesY - 1)
          graphics.drawImage(tile, location.x + x * imageWidth, location.y + y * imageHeight, null)

      val xEdgeDif = width - (tilesX * imageWidth)
      val yEdgeDif = height - (tilesY * imageHeight)

      if (xEdgeDif > 0) {
        for (i <- 0 to tilesY - 1)
          graphics.drawImage(
            tile,
            location.x + tilesX * imageWidth,
            location.y + i * imageHeight,
            location.x + tilesX * imageWidth + xEdgeDif,
            location.y + i * imageHeight + imageHeight,
            0,
            0,
            xEdgeDif,
            imageHeight,
            null
          )
      }

      if (yEdgeDif > 0) {
        for (i <- 0 to tilesX - 1)
          graphics.drawImage(
            tile,
            location.x + i * imageWidth,
            location.y + tilesY * imageHeight,
            location.x + i * imageWidth + imageWidth,
            location.y + tilesY * imageHeight + yEdgeDif,
            0,
            0,
            imageWidth,
            yEdgeDif,
            null
          )
      }

      if (yEdgeDif > 0 && xEdgeDif > 0) {
        graphics.drawImage(
          tile,
          location.x + tilesX * imageWidth,
          location.y + tilesY * imageHeight,
          location.x + width,
          location.y + height,
          0,
          0,
          xEdgeDif,
          yEdgeDif,
          null
        )
      }
    }
  }

}
