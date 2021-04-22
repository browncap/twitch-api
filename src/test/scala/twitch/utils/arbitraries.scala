package com.twitch.utils

import com.twitch.livestreams.Livestream
import org.scalacheck.{Arbitrary, Gen}

object arbitraries {

  implicit val livestreamArb: Arbitrary[Livestream] = Arbitrary {
    for {
      u  <- Gen.alphaStr
      g  <- Gen.alphaStr
      t  <- Gen.alphaStr
      v  <- Gen.posNum[Int]
      tu <- Gen.alphaStr
    } yield Livestream(u, g, t, v, tu)
  }

}