package actors

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import akka.routing.{ActorRefRoutee, AddRoutee}

/**
  * Created by jay on 22/05/2016.
  */
class Client(out: ActorRef, registrar: ActorRef) extends Actor with ActorLogging{

	override def preStart = {
		val me = self
		log.info(s"$me online subscribing to twitter stream")
		registrar ! Registrar.Register(me)
	}

	override def receive = {
		case None => log.info("received no data, going to ignore it")
		case msg => out ! msg
	}
}

object Client{
	def props(out: ActorRef, twitterStreamer: ActorRef) = Props(classOf[Client], out, twitterStreamer)
}