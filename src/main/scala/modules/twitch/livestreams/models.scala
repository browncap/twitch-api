package com.twitch.livestreams

import io.circe.config.syntax._
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import org.http4s.{QueryParamDecoder, QueryParamEncoder}
import org.http4s.dsl.impl.OptionalMultiQueryParamDecoderMatcher

final case class Streamer(value: String)
object Streamer {
  implicit val queryParamEncoder: QueryParamEncoder[Streamer] =
    QueryParamEncoder[String].contramap[Streamer](a => a.value)
  implicit val queryParamDecoder: QueryParamDecoder[Streamer] =
    QueryParamDecoder[String].map(Streamer(_))
}
object StreamerVar {
  def unapply(str: String): Option[Streamer] =
    if (!str.isEmpty)
      Some(Streamer(str))
    else
      None
}
object StreamerParam extends OptionalMultiQueryParamDecoderMatcher[Streamer]("streamer")

final case class Livestream(
  user_name: String,
  game_name: String,
  title: String,
  viewer_count: Int,
  thumbnail_url: String
)
object Livestream {
  implicit val decoder: Decoder[Livestream] = deriveDecoder
  implicit val encoder: Encoder[Livestream] = deriveEncoder
}

final case class Livestreams(data: List[Livestream])
object Livestreams {
  implicit val decoder: Decoder[Livestreams] = deriveDecoder
  implicit val encoder: Encoder[Livestreams] = deriveEncoder
}

case object BadQueryParameters extends Throwable