package com.twitch.integration

import cats.effect.Sync
import com.twitch.config.TwitchConfig
import com.twitch.livestreams.{Livestreams, Streamer}
import com.twitch.token.AccessToken
import io.chrisdavenport.log4cats.Logger
import org.http4s._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.implicits._

trait TwitchClient[F[_]] {
  def retrieveAccessToken: F[AuthResponse]
  def validateAccessToken(token: AccessToken): F[Unit]
  def getStreams(token: AccessToken, streamers: List[Streamer]): F[Livestreams]
}

object TwitchClient {

  def build[F[_]](client: Client[F], config: TwitchConfig)(implicit F: Sync[F], L: Logger[F]): TwitchClient[F] =
    new TwitchClient[F] {

      def retrieveAccessToken: F[AuthResponse] = {
        val req = Request[F](
          method = Method.POST,
          uri =
            uri"https://id.twitch.tv/oauth2/token?client_id"
              .withQueryParam("client_id", config.clientId)
              .withQueryParam("client_secret", config.clientSecret)
              .withQueryParam("grant_type", "client_credentials")
        )

        client.expect[AuthResponse](req)
      }

      def validateAccessToken(token: AccessToken): F[Unit] = { // {"status":401,"message":"invalid access token"}
        val req = Request[F](
          method = Method.POST,
          uri = uri"https://id.twitch.tv/oauth2/validate",
          headers = Headers.of(
            Authorization(Credentials.Token(AuthScheme.Bearer, token.value)),
            Header("client-id", config.clientId))
        )

        client.expect[Unit](req)
      }

      def getStreams(token: AccessToken, streamers: List[Streamer]): F[Livestreams] = {
        val req = Request[F](
          method = Method.GET,
          uri =
            uri"https://api.twitch.tv/helix/streams"
              .withQueryParam("user_login", streamers),
          headers = Headers.of(
            Authorization(Credentials.Token(AuthScheme.Bearer, token.value)),
            Header("client-id", config.clientId))
        )

        client.expect[Livestreams](req)
      }

    }

}
