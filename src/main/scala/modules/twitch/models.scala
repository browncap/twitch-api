package com.twitch.integration

import io.circe.config.syntax._
import com.twitch.token.AccessToken
import io.circe.Decoder
import io.circe.generic.semiauto._
import io.circe.generic.extras.semiauto.deriveUnwrappedDecoder

import scala.concurrent.duration.FiniteDuration

final case class TokenExpiration(value: FiniteDuration)
object TokenExpiration {
  implicit val decoder: Decoder[TokenExpiration] = deriveUnwrappedDecoder
}

final case class AuthResponse(access_token: AccessToken)
object AuthResponse {
  implicit val decoder: Decoder[AuthResponse] = deriveDecoder
}
