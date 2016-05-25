package controllers

import javax.inject.{Inject, Singleton}

import actors.{Registrar, OAuthData, TwitterStreamer, Client}
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.Configuration
import play.api.i18n.MessagesApi
import play.api.libs.streams.ActorFlow
import play.api.libs.ws.WSClient
import play.api.mvc.{WebSocket, Action, Controller}

/**
  * Created by jay on 22/05/2016.
  */
@Singleton
class Application @Inject() (implicit config: Configuration, system: ActorSystem
							 , mat: Materializer, messages: MessagesApi, wSClient: WSClient) extends Controller{
	val oAuthData = OAuthData(config)
	val url = config.getString("twitter.url").get
	val twitterStreamer = system.actorOf(TwitterStreamer.props(wSClient, url, oAuthData, mat), "twitter-streamer")
	val registrar = system.actorOf(Registrar.props(twitterStreamer), "registrar")

	def index = Action{ implicit req =>
		Ok(views.html.index("tweeter stream is up and running"))
	}

	def tweets = WebSocket.accept[String, String]{ request =>
		ActorFlow.actorRef{ out => Client.props(out, registrar) }
	}

}
