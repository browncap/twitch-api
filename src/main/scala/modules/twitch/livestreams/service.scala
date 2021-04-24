 package com.twitch.livestreams

 import cats.implicits._
 import cats.effect.Sync
 import com.twitch.integration.TwitchClient
 import com.twitch.token.Token

 trait LivestreamInfo[F[_]] {
   def getStreams(streamers: List[Streamer]): F[Livestreams]
 }

 object LivestreamInfo {
   def build[F[_]](twitchClient: TwitchClient[F], token: Token[F])(implicit F: Sync[F]): LivestreamInfo[F] = {
     new LivestreamInfo[F] {

       override def getStreams(streamers: List[Streamer]): F[Livestreams] = (for {
         token   <- token.getAccessToken
         streams <- twitchClient.getStreams(token, streamers)
       } yield streams).onError {
         case e => F.pure(println(e))
       }
     }
   }
 }
