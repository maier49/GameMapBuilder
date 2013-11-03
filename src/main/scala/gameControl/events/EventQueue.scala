package main.scala.gameControl.events

import scala.collection.mutable

/**
 * The singleton global event queue
 */
object EventQueue extends mutable.Queue[MyEvent]
