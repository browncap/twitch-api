package com.twitch.livestreams

import cats.syntax.all._
import cats.effect.Sync
import io.chrisdavenport.log4cats.Logger
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityEncoder._

object LivestreamRoutes {
  def routes[F[_]](livestreams: LivestreamInfo[F])(implicit F: Sync[F], L: Logger[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {

      case GET -> Root / "livestreams" :? StreamerParam(streamer) =>
        for {
          s    <- F.fromOption(streamer.toOption, BadQueryParameters)
          ls   <- livestreams.getStreams(s)
          resp <- Ok(ls)
        } yield resp

    }
  }
}
