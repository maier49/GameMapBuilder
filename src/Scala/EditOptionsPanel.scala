package Scala

import scala.swing._
import scala.swing.{Label}
import main.scala.gameController.AreaMap
import scala.swing.event.ButtonClicked
import javax.swing.{JOptionPane, JDialog}
import main.scala.geometry.SimpleVector

/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/30/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
class EditOptionsPanel(val map: MapEditPanel, oriented: Orientation.Value) extends BoxPanel(oriented) {
  var Background = "Background"
  var Foreground = "Foreground"
  var Overlay = "Overlay"
  var selectedLayer = Background

  val backgroundCheckBox = new CheckBox("Background") {
    selected = true
  }
  val foregroundCheckBox = new CheckBox("Foreground") {
    selected = true
  }
  val overlayCheckBox = new CheckBox("Overlay") {
    selected = true
  }
  val backgroundRadioButton = new RadioButton("Background") {
    selected = true
  }

  val widthField = new TextField(map.areaMap.resolution.x.toString)
  val heightField = new TextField(map.areaMap.resolution.y.toString)
  val setResolutionButton = new Button("Set Resolution")
  val foregroundRadioButton = new RadioButton("Foreground")
  val overlayRadioButton = new RadioButton("Overlay")
  new ButtonGroup(backgroundRadioButton, foregroundRadioButton, overlayRadioButton)
  contents.append(new Label("Filter layer display:"), backgroundCheckBox, foregroundCheckBox, overlayCheckBox,
    new Label("Choose layer to edit:"), backgroundRadioButton, foregroundRadioButton, overlayRadioButton,
    new Label("Map Width:"), widthField, new Label("Map Height:"), heightField, setResolutionButton)

  listenTo(backgroundCheckBox, foregroundCheckBox, overlayCheckBox, backgroundRadioButton, foregroundRadioButton,
    overlayRadioButton, setResolutionButton)
  reactions += {
    case ButtonClicked(`backgroundCheckBox`) =>
      map.areaMap.drawBackGround = backgroundCheckBox.selected
      map.repaint()
    case ButtonClicked(`foregroundCheckBox`) =>
      map.areaMap.drawForeGround = foregroundCheckBox.selected
      map.repaint()
    case ButtonClicked(`overlayCheckBox`) =>
      map.areaMap.drawOverlay = overlayCheckBox.selected
      map.repaint()
    case ButtonClicked(`backgroundRadioButton`) =>
      selectedLayer = Background
    case ButtonClicked(`foregroundRadioButton`) =>
      selectedLayer = Foreground
    case ButtonClicked(`overlayRadioButton`) =>
      selectedLayer = Overlay
    case ButtonClicked(`setResolutionButton`) =>
      try {
        val x = widthField.text.toInt
        val y = heightField.text.toInt
        map.areaMap.resolution = SimpleVector(x,y)
      } catch {
        case exception: Exception => {
          val dialog = JOptionPane.showMessageDialog(this.peer, "Invalid X or Y. Make sure your inputs are positive integers")
        }
      }
  }


  val updateResolution: () => Unit =  () => {
    this.widthField.text = map.areaMap.resolution.x.toString
    this.heightField.text = map.areaMap.resolution.y.toString
  }
}
