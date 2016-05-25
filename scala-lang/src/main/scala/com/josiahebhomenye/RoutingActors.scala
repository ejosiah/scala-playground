package com.josiahebhomenye

import java.io.File

import akka.actor.Actor.Receive
import akka.actor._
import akka.routing._
import akka.stream.ActorMaterializer
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random

/**
  * Created by jay on 23/05/2016.
  */
object RoutingActors extends App {
	import language.postfixOps
	class EchoActor extends Actor{
		override def preStart(): Unit ={
			println(s"$self is online")
		}
		def receive = {
			case msg => println(msg)
		}
	}


	implicit val system = ActorSystem()
	implicit val mat = ActorMaterializer()
	implicit val ec  = system.dispatcher

	val router = system.actorOf(FromConfig().props(), "router0")
	val words = Source.fromFile(new File("/usr/share/dict/words")).getLines().toVector
	val size = words.size
	def randomWord = words(Random.nextInt(size))

	val scheduler = system.scheduler

	scheduler.scheduleOnce(1 seconds){
		router ! new AddRoutee(new ActorRefRoutee(system.actorOf(Props[EchoActor])))
		router ! new AddRoutee(new ActorRefRoutee(system.actorOf(Props[EchoActor])))
	}

	scheduler.schedule(3 second, 1 second) {  router ! randomWord }
	scheduler.schedule(5 minutes, 5 minutes){ router ! new AddRoutee(new ActorRefRoutee(system.actorOf(Props[EchoActor])))}


	def act = {

	}

}
