package com.twitch.integration

import com.twitch.token.AccessToken
import io.circe.Decoder
import io.circe.generic.semiauto._

final case class AuthResponse(access_token: AccessToken)
object AuthResponse {
  implicit val decoder: Decoder[AuthResponse] = deriveDecoder
}
