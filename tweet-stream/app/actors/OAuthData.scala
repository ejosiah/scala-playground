package actors

import play.api.Configuration
import play.api.libs.oauth.{OAuthCalculator, RequestToken, ConsumerKey}
import play.api.libs.ws.WSSignatureCalculator

/**
  * Created by jay on 22/05/2016.
  */
object OAuthData{

	def apply(config: Configuration): OAuthData = (for{
			apiKey <- config.getString("twitter.apiKey")
			apiSecret <- config.getString("twitter.apiSecret")
			token <- config.getString("twitter.token")
			tokenSecret <- config.getString("twitter.tokenSecret")
		} yield OAuthData(apiKey, apiSecret, token, tokenSecret)).head
}


case class OAuthData(apiKey: String, apiSecret: String, token: String, tokenSecret: String) {

	def apply(): WSSignatureCalculator = OAuthCalculator(ConsumerKey(apiKey, apiSecret), RequestToken(token, tokenSecret))
}

