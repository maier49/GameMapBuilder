package main.scala.gameControl.events

import scala.swing.event.{KeyEvent, KeyPressed}


/**
 * Created with IntelliJ IDEA.
 * User: Bradley
 * Date: 11/2/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
case class MyKeyEvent(val swingKeyPressEvent: KeyEvent ) extends MyEvent
