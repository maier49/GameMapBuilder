package main.scala.resourceFileUtils

/**
 * Constants used when creating a resource file representing an AreaMap
 */
object AreaMapFileKeys {




  val Resolution = "RESOLUTION"
  val Center = "CENTER"
  val Scenery = "SCENERY"
  val Wall = "WALL"
  val Character = "CHARACTER"
  val NoImage = "NOIMAGE"


  val Layer = "LAYER"

  object LayerVal {
    val Foreground = "FOREGROUND"
    val Background = "BACKGROUND"
    val Overlay = "OVERLAY"
  }


  object CoordinateKey {
    val X = "X"
    val Y = "Y"
  }

  val ImagesDirectoryPath = "/images/"
  val MapDataFile = "/mapData.txt"

  val ImageFileKey = "IMAGEFILEKEY"
  val ImageType = "IMAGETYPE"
  val EntityId = "ENTITYID"

  object VectorType {
    val Size = "SIZE"
    val Location = "LOCATION"
  }

  object EntityType {
    val Static = "STATIC"
  }

  object ImageTypeVal {
    val TiledImage = "TILEDIMAGE"
    val SimpleImage = "SIMPLEIMAGE"
  }


  object CharacterEntityKeys {
    val WalkSpeed = "WALKSPEED"
    val RunSpeed = "RUNSPEED"
  }



}
