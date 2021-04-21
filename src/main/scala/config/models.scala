package com.twitch.config

final case class ServiceConfig(
  http: HttpConfig,
  twitch: TwitchConfig
)

final case class HttpConfig(
  port: Int,
  host: String
)

final case class TwitchConfig(
  clientId: String,
  clientSecret: String
)
