package com.stripe.rainier.example

import com.stripe.rainier.compute.Real
import com.stripe.rainier.core._
import com.stripe.rainier.repl._
import com.stripe.rainier.sampler._

object BatchNormal {
  def model(k: Int): RandomVariable[(Real, Real)] = {
    val r = new scala.util.Random
    val trueMean = 3.0
    val trueStddev = 2.0
    val data = 1.to(k).toArray.map { i =>
      (r.nextGaussian * trueStddev) + trueMean
    }

    for {
      mean <- Uniform(0, 10).param
      stddev <- Uniform(0, 10).param
      _ <- Normal(mean, stddev).fit(data)
    } yield (mean, stddev)
  }

  def main(args: Array[String]): Unit = {
    plot2D(model(100000).sample(Walkers(100), 10000, 10000))
  }
}
