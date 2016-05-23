package actors

import akka.actor.{Props, ActorRef, Actor}
import akka.routing.{ActorRefRoutee, AddRoutee}

/**
  * Created by jay on 22/05/2016.
  */
class Client(out: ActorRef, twitterStreamer: ActorRef) extends Actor{

	override def preStart = {
		twitterStreamer ! TwitterStreamer.Register(self)
	}

	override def receive = {
		case msg =>
			println(s"recieved msg: $msg")
			out ! msg
	}
}

object Client{

	def props(out: ActorRef, twitterStreamer: ActorRef) = Props(classOf[Client], out, twitterStreamer)
}