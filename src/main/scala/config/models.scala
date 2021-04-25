package com.twitch.config

final case class ServiceConfig(
  twitch: TwitchConfig
)

final case class TwitchConfig(
  clientId: String,
  clientSecret: String
)
