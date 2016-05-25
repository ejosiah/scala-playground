package actors

import akka.actor._
import akka.stream.scaladsl.{Sink, Flow}
import akka.util.ByteString
import play.api.libs.ws.WSClient
import akka.stream.Materializer

/**
  * Created by jay on 22/05/2016.
  */
object TwitterStreamer{
	case class StartStreaming(router: ActorRef)
	case object StopStreaming	// TODO stop the stream when no one is listening
	case class Status(streaming: Boolean)
	case object GetStatus

	def props(ws: WSClient,url: String, oAuthData: OAuthData, mat: Materializer): Props
		= Props(classOf[TwitterStreamer], ws, url, oAuthData, mat)
}

class TwitterStreamer(ws: WSClient, url: String, oAuthData: OAuthData, implicit val mat: Materializer) extends Actor with ActorLogging{

	import TwitterStreamer._
	import context._
	var streaming = false

	override def preStart(): Unit ={
		log.info("twitter streamer online")
	}

	def receive = {
		case StartStreaming(router) => startStreaming(router)
		case GetStatus => Status(streaming)
	}

	def startStreaming(router: ActorRef): Unit = {
		import language.postfixOps
		if(!streaming) {
			val bytesToString = Flow[ByteString].map(bytes => bytes.decodeString("UTF-8"))
			val endPoint = Sink.actorRef[String](router, None)
			log.info("streaming in progress")
			ws
				.url(url)
				.sign(oAuthData())
				.withQueryString("track" -> "cat")
				.stream()
				.map { stream =>
					(stream body) via bytesToString runWith endPoint
				}
			streaming = true
		}
	}

}
