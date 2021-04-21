package com.twitch.http

import cats.effect.Sync
import cats.effect.concurrent.Ref
import com.twitch.livestreams.{LivestreamInfo, LivestreamRoutes}
import io.chrisdavenport.log4cats.Logger
import org.http4s.implicits._
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.CORS
import cats.implicits._

trait RoutesApp[F[_]] {
  val app: HttpApp[F]
}

object RoutesApp {
  def build[F[_]](lsInfo: LivestreamInfo[F])(implicit F: Sync[F], L: Logger[F]): RoutesApp[F] = {
    new RoutesApp[F] {
      private val ping = Ping.route
      private val livestreams = LivestreamRoutes.routes(lsInfo)
      private val routes: HttpRoutes[F] = ping <+> livestreams

      override val app: HttpApp[F] = CORS(routes).orNotFound
    }
  }
}
