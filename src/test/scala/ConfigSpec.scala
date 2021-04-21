package com.twitch

import org.specs2.mutable.Specification
import cats.effect.IO
import com.twitch.config.{ConfigService, ServiceConfig}

class ConfigSpec extends Specification {
  "Config must load" >> {
    ConfigService.getConfig[IO].attempt.unsafeRunSync() must beRight[ServiceConfig]
  }
}
