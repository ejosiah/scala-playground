package com.josiahebhomenye

import java.awt.{BorderLayout, FlowLayout}
import java.awt.event.{ActionListener, ActionEvent}
import java.io.FileOutputStream
import javax.swing.{JPanel, JTextField, JButton, JFrame}
import akka.stream
import akka.stream.scaladsl.StreamConverters._
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl._
import akka.stream.{ClosedShape, IOResult, OverflowStrategy, ActorMaterializer}
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.{Failure, Try, Success}

/**
  * Created by jay on 23/05/2016.
  */
object StreamTest extends App{
	implicit val system = ActorSystem("test-system")
	implicit val materializer = ActorMaterializer()

	val source = Source.actorRef(10, OverflowStrategy.dropHead)
	val graph = source.toMat(Sink.foreach(println))(Keep.left)
	val sink = fromOutputStream(() => new FileOutputStream("actor.text"), autoFlush = true)
	val stringToBytes = Flow[String].map(ByteString(_))

	val graph2 = source.via(stringToBytes).toMat(sink)(Keep.left)



	val actor = graph.run()
	val actor2 = graph2.run()



	import language.implicitConversions

	implicit def fnToActionListener(fn: ActionEvent => Unit) : ActionListener = new ActionListener {
		override def actionPerformed(e: ActionEvent): Unit = fn(e)
	}

	new JFrame("Actor stream"){
		val button = new JButton("send")
		val textField = new JTextField
		val action : ActionListener = {e: ActionEvent =>
			val msg = textField.getText()
			textField.setText(null)
			actor ! msg
			actor2 ! msg
		}
		button addActionListener action
		add(textField, BorderLayout.CENTER)
		add(new JPanel().add(button), BorderLayout.SOUTH)
		setSize(200, 100)
		setVisible(true)
	}


}
