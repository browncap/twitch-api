 package com.twitch.token

 import cats.implicits._
 import cats.effect.Sync
 import cats.effect.concurrent.Ref
 import com.twitch.integration.TwitchClient

 trait Token[F[_]] {
   def getAccessToken: F[AccessToken]
   def validateAccessToken: F[Unit]
   def refreshAccessToken: F[Unit]
 }

 object Token {
   def build[F[_]](twitchClient: TwitchClient[F])(implicit F: Sync[F]): F[Token[F]] = {
     twitchClient.retrieveAccessToken.flatMap { auth =>
       Ref.of[F, AccessToken](auth.access_token).map { ref =>
         new Token[F] {
           override def getAccessToken: F[AccessToken] =
             ref.get

           override def validateAccessToken: F[Unit] =
             ref.get.map(token => twitchClient.validateAccessToken(token))

           override def refreshAccessToken: F[Unit] =
             twitchClient.retrieveAccessToken.map(newToken => ref.update(_ => newToken.access_token))
         }
       }
     }
   }
 }
