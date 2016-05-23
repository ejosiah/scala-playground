package controllers

import javax.inject.{Inject, Singleton}

import actors.{OAuthData, TwitterStreamer, Client}
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.i18n.MessagesApi
import play.api.libs.streams.ActorFlow
import play.api.libs.ws.WSClient
import play.api.mvc.{WebSocket, Action, Controller}

/**
  * Created by jay on 22/05/2016.
  */
@Singleton
class Application @Inject() (implicit system: ActorSystem, mat: Materializer, messages: MessagesApi, wSClient: WSClient) extends Controller{
	val oAuthData = OAuthData("", "", "", "")
	val twitterStreamer = system.actorOf(TwitterStreamer.props(wSClient, "", oAuthData, mat), "twitter-streamer")

	def index = Action{
		Ok(views.html.index("tweeter stream is up and running"))
	}

	def twitter = WebSocket.accept[String, String]{ request =>
		ActorFlow.actorRef{ out => Client.props(out, twitterStreamer) }
	}

}
