package com.twitch.livestreams

import cats.effect.IO
import io.chrisdavenport.log4cats.noop.NoOpLogger
import io.circe.Json
import org.http4s.{Header, Method, Request, Uri}
import org.scalatest.{FlatSpec, Matchers, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.http4s.implicits._
import io.circe.syntax._
import io.circe.Json
import org.http4s.circe.CirceEntityDecoder._
import com.twitch.utils.arbitraries._

class LivestreamsRoutesSpec extends WordSpec with Matchers with ScalaCheckPropertyChecks {

  implicit val logger = NoOpLogger.impl[IO]

  def responseJson(ls: Livestream*): Json =
    Json.obj("data" :=
      Json.fromValues(
        ls.map { ls =>
          Json.obj(
            "user_name" := ls.user_name,
            "game_name" := ls.game_name,
            "title" := ls.title,
            "viewer_count" := ls.viewer_count,
            "thumbnail_url" := ls.thumbnail_url
          )
        }
      )
    )

  def mockLivestreamInfoService(livestreams: Livestream*) = new LivestreamInfo[IO] {
    def getStream(streamer: Streamer): IO[Livestreams] = IO.pure(Livestreams(livestreams.toList))
    def getStreams(streamers: List[Streamer]): IO[Livestreams] = IO.pure(Livestreams(livestreams.toList))
  }

  "LivestreamRoutes" should {
    "get stream info for a single streamer" in {
      forAll { (livestream: Livestream) =>

        val result = responseJson(livestream)
        val mockSvc = mockLivestreamInfoService(livestream)

        val routes = LivestreamRoutes.routes(mockSvc).orNotFound
        val req = Request[IO](method = Method.GET, uri = Uri.unsafeFromString("/livestreams/Streamer"))
        val routeResult = routes.run(req).unsafeRunSync

        routeResult.status.isSuccess should be(true)
        routeResult.as[Json].unsafeRunSync() should be(result)
      }
    }

    "get stream info for multiple streamers" in {
      forAll { (ls1: Livestream, ls2: Livestream) =>

        val result = responseJson(ls1, ls2)
        val mockSvc = mockLivestreamInfoService(ls1, ls2)

        val routes = LivestreamRoutes.routes(mockSvc).orNotFound
        val req = Request[IO](method = Method.GET, uri = Uri.unsafeFromString("/livestreams?user_login=Streamer&user_login=Streamer2"))
        val routeResult = routes.run(req).unsafeRunSync

        routeResult.status.isSuccess should be(true)
        routeResult.as[Json].unsafeRunSync() should be(result)
      }
    }
  }
}