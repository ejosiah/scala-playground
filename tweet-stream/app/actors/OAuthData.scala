package actors

import play.api.libs.oauth.{OAuthCalculator, RequestToken, ConsumerKey}
import play.api.libs.ws.WSSignatureCalculator

/**
  * Created by jay on 22/05/2016.
  */
case class OAuthData(apiKey: String, apiSecret: String, token: String, tokenSecret: String) {

	def apply(): WSSignatureCalculator = OAuthCalculator(ConsumerKey(apiKey, apiKey), RequestToken(token, tokenSecret))
}
