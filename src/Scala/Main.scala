package Scala


import swing._
import Scala.Main.OptionsToolBar

/**
 * A Simple UI for building maps
 */

object Main extends SimpleSwingApplication {

  object Map extends Panel {
    preferredSize = new Dimension(500, 500)

    def exportContents: String = {
      "This is A MotherFucking String"
    }
  }

  object SaveDialog extends Dialog {
    preferredSize = new Dimension(200, 200)
  }

  object OptionsToolBar extends MenuBar {
    val toolBarChooser = new FileChooser()
    preferredSize = new Dimension(800, 100)

    contents += new Menu("File"){

      contents += new MenuItem(
        Action("Save") {
          toolBarChooser.showSaveDialog(this)
        })
      contents += new MenuItem(
        Action("Open") {
          toolBarChooser.showOpenDialog(this)
        }
      )
    }

  }


  object MapBuilderPanel extends BorderPanel {
    layout(new Label("fdfdsa")) = BorderPanel.Position.Center
    layout(new Label("Shit Yeah")) = BorderPanel.Position.West
    layout(OptionsToolBar) = BorderPanel.Position.North
    OptionsToolBar.visible = true
    preferredSize = new Dimension(800, 800)

  }

  def top = new MainFrame {
    title = "Hello World!"
    contents = MapBuilderPanel

    /*val frame1 = new Frame {
      title = "Scala"
      contents = MapPanel
    }

    frame1.visible = true*/
  }


}


