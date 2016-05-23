package actors

import akka.actor._
import akka.routing.{RemoveRoutee, ActorRefRoutee, AddRoutee, FromConfig}
import akka.stream.javadsl.Sink
import play.api.libs.ws.WSClient
import akka.stream.Materializer

/**
  * Created by jay on 22/05/2016.
  */
class TwitterStreamer(ws: WSClient, url: String, oAuthData: OAuthData, implicit val mat: Materializer) extends Actor with ActorLogging{

	import streams._
	import context._
	import TwitterStreamer._

	lazy val router = actorOf(FromConfig.props(), "router")
	lazy val endPoint = Sink.actorRef[String](router, None)

	override def preStart(): Unit ={
		router
	}

	def receive = {
		case Register(client) => register(client)
		case Terminated(client) => unRegister(client)
		case StartStreaming => startStreaming()
	}

	def register(client: ActorRef): Unit ={
		log.info(s"client connection received from $client")
		router ! new AddRoutee(new ActorRefRoutee(client))
		watch(client)
	}

	def unRegister(client: ActorRef): Unit = {
		log info s"client $client connection terminated"
		router ! new RemoveRoutee(new ActorRefRoutee(client))
	}

	def startStreaming(): Unit = {
		import language.postfixOps
		ws
			.url(url)
			.sign(oAuthData())
			.withQueryString("track" -> "cat")
			.stream()
			.map{ stream =>
				(stream body) via BytesToString runWith endPoint
			}
	}

}

object TwitterStreamer{
	case class Register(me: ActorRef)
	case object StartStreaming

	def props(ws: WSClient,url: String, oAuthData: OAuthData, mat: Materializer): Props = Props(classOf[TwitterStreamer], ws, url, oAuthData, mat)
}
