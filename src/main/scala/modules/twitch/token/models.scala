package com.twitch.token

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe.generic.extras.semiauto.deriveUnwrappedDecoder
import io.circe.generic.extras.semiauto.deriveUnwrappedEncoder

final case class AccessToken(value: String)
object AccessToken {
  implicit val decoder: Decoder[AccessToken] = deriveUnwrappedDecoder
  implicit val encoder: Encoder[AccessToken] = deriveUnwrappedEncoder
}
