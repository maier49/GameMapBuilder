package main.scala

import javax.swing._
import java.awt._
import main.scala.gameControl.{GameScreen, AreaMap}
import main.scala.gameControl.events.{MyKeyEvent, EventQueue}
import scala.swing.event.KeyEvent
import scala.swing.{MainFrame, Panel}
import main.scala.gameControl.events.MyKeyEvent
import main.scala.geometry.SimpleVector


/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 10/20/13
 * Time: 3:41 AM
 * To change this template use File | Settings | File Templates.
 */
class ScalaPane(var map : GameScreen) extends Panel {
  val WIDE: Int = 1280
  val HIGH: Int = 960


  focusable = true
  var index : Int = 0
  var totalTime : Long = 0
  var averageTime : Long = 0
  var frameCount : Int = 0
  this.opaque = false
  this.preferredSize =  new Dimension(WIDE, HIGH)
  val frame: MainFrame = new MainFrame()

  frame.contents = this
  frame.pack()
  frame.visible = true
  frame.resizable = false
  frame.preferredSize =  new Dimension(WIDE, HIGH)

  listenTo(keys)
  reactions += {
    case e: KeyEvent => EventQueue.enqueue(MyKeyEvent(e))
  }

  override def paintComponent(graphics : Graphics2D) {
    super.paintComponent(graphics)
    val image = map.draw(graphics, SimpleVector(WIDE, HIGH))
  }

  override def paint(graphics : Graphics2D) {
    super.paint(graphics)
    val image = map.draw(graphics, SimpleVector(WIDE, HIGH))
  }

}

