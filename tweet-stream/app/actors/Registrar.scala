package actors

import akka.actor.Actor.Receive
import akka.actor._
import akka.routing._

/**
  * Created by jay on 25/05/2016.
  */

object Registrar{
	case class Register(me: ActorRef)

	def props(streamer: ActorRef): Props = Props(classOf[Registrar], streamer)
}

class Registrar(streamer: ActorRef) extends Actor with ActorLogging{
	import Registrar._

	import context._
	lazy val router = actorOf(FromConfig.props(), "router")
	var count = 0

	def receive = {
		case Register(client) => register(client)
		case Terminated(client) => unRegister(client)	// FIXME this may be redundant as router already does this
	}

	def register(client: ActorRef): Unit ={
		log.info(s"client connection received from $client")
		router ! new AddRoutee(new ActorRefRoutee(client))
		watch(client)
		if(count < 1){
			streamer ! TwitterStreamer.StartStreaming(router)
			count = 1
		}
	}

	def unRegister(client: ActorRef): Unit = {
		log info s"client $client connection terminated"
		router ! new RemoveRoutee(new ActorRefRoutee(client))
	}


}
