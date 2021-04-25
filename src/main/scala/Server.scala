package com.twitch

import cats.effect.concurrent.Ref
import cats.implicits._
import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, Resource, Timer}
import com.twitch.config.ConfigService
import org.http4s.HttpApp
import org.http4s.server.blaze.BlazeServerBuilder
import com.twitch.http.RoutesApp
import com.twitch.integration.TwitchClient
import com.twitch.livestreams.LivestreamInfo
import com.twitch.token.Token
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.client.blaze.BlazeClientBuilder
import fs2.Stream

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

object Server {

  def serve[F[_]](implicit F: ConcurrentEffect[F], CS: ContextShift[F], T: Timer[F]): Resource[F, ExitCode] = {
    implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    for {
      implicit0(l: SelfAwareStructuredLogger[F]) <- Resource.liftF(Slf4jLogger.create[F])
      c <- BlazeClientBuilder[F](ec).resource
      cfg <- Resource.liftF(ConfigService.getConfig)
      tc = TwitchClient.build(c, cfg.twitch)
      tk <- Resource.liftF(Token.build(tc))
      ls = LivestreamInfo.build(tc, tk)
      svc = RoutesApp.build[F](ls).app
      server <- Resource.liftF(buildServer(ec, tk)(svc))
    } yield server
  }

  private def buildServer[F[_] : ConcurrentEffect : Timer](ec: ExecutionContext, token: Token[F])(services: HttpApp[F]): F[ExitCode] =
    BlazeServerBuilder[F](ec)
      .bindHttp(8080, "localhost")
      .withHttpApp(services)
      .serve
      .concurrently {
        Stream.awakeEvery[F](4.hours).map(_ => token.refreshAccessToken)
      }
      .compile
      .drain
      .as(ExitCode.Success)

}
