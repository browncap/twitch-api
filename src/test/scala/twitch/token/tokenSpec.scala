package com.twitch.token

import cats.effect.IO
import com.twitch.integration.{AuthResponse, TwitchClient}
import com.twitch.livestreams.{Livestreams, Streamer}
import io.chrisdavenport.log4cats.noop.NoOpLogger
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class TokenSpec extends WordSpec with Matchers with ScalaCheckPropertyChecks {

  class MockTwitchClient extends TwitchClient[IO] {
    def retrieveAccessToken: IO[AuthResponse] = ???
    def validateAccessToken(token: AccessToken): IO[Unit] = ???
    def getStreams(token: AccessToken, streamers: List[Streamer]): IO[Livestreams] = ???
  }

  def mockTwitchClient = new MockTwitchClient {
    override def retrieveAccessToken: IO[AuthResponse] = IO.pure(AuthResponse(AccessToken("Token")))
    override def validateAccessToken(token: AccessToken): IO[Unit] = IO.unit
  }

  "Token" should {
    "get a stored AccessToken" in {
      val tokenSvc = Token.build(mockTwitchClient).unsafeRunSync()
      tokenSvc.getAccessToken.unsafeRunSync() should be(AccessToken("Token"))
    }

    "validate a stored AccessToken" in {
      val tokenSvc = Token.build(mockTwitchClient).unsafeRunSync()
      tokenSvc.validateAccessToken.unsafeRunSync() should be(())
    }

    "refresh a stored AccessToken" in {
      val tokenSvc = Token.build(mockTwitchClient).unsafeRunSync()
      tokenSvc.refreshAccessToken.unsafeRunSync() should be(())
    }
  }
}