package com.stripe.rainier.core

import com.stripe.rainier.compute._

/**
  * A trait for objects representing the support of a continuous distribution.
  * Specifies a function to transform a real-valued variable to this range,
  * and its log-jacobian.
  */
private[rainier] trait Support {
  import Support._

  def transform(v: Variable): Real

  def logJacobian(v: Variable): Real

  def union(that: Support): Support = (this, that) match {
    case (UnboundedSupport, _)                            => UnboundedSupport
    case (_, UnboundedSupport)                            => UnboundedSupport
    case (BoundedBelowSupport(_), BoundedAboveSupport(_)) => UnboundedSupport
    case (BoundedAboveSupport(_), BoundedBelowSupport(_)) => UnboundedSupport
    case (BoundedBelowSupport(thisMin), BoundedSupport(thatMin, thatMax)) =>
      BoundedBelowSupport(realMin(thisMin, thatMin))
    case (BoundedSupport(thisMin, thisMax), BoundedBelowSupport(thatMin)) =>
      BoundedBelowSupport(realMin(thisMin, thatMin))
    case (BoundedAboveSupport(thisMax), BoundedSupport(thatMin, thatMax)) =>
      BoundedAboveSupport(realMax(thisMax, thatMax))
    case (BoundedSupport(thisMin, thisMax), BoundedAboveSupport(thatMax)) =>
      BoundedAboveSupport(realMax(thisMax, thatMax))
    case (BoundedSupport(thisMin, thisMax), BoundedSupport(thatMin, thatMax)) =>
      BoundedSupport(realMin(thisMin, thatMin), realMax(thisMax, thatMax))
  }

  def isDefinedAt(real: Real): Real
}

object Support {
  private def realMin(a: Real, b: Real): Real = ((a - b).abs - (b + a)) / 2.0

  private def realMax(a: Real, b: Real): Real = ((a - b).abs + (b + a)) / 2.0

  def union(supports: Iterable[Support]): Support = supports.reduce {
    (a: Support, b: Support) =>
      a.union(b)
  }
}

/**
  * A support representing the whole real line.
  */
object UnboundedSupport extends Support {
  def transform(v: Variable): Real = v

  def logJacobian(v: Variable): Real = Real.zero

  def isDefinedAt(real: Real): Real = Real.one
}

/**
  * A support representing a bounded (min, max) interval.
  */
case class BoundedSupport(min: Real, max: Real) extends Support {
  def transform(v: Variable): Real =
    (Real.one / (Real.one + (v * -1).exp)) * (max - min) + min

  def logJacobian(v: Variable): Real =
    transform(v).log + (1 - transform(v)).log + (max - min).log

  def isDefinedAt(real: Real): Real = (real > min) * (real < max)
}

/**
  * A support representing an open-above {r > k} interval.
  * @param min The lower bound of the distribution
  */
case class BoundedBelowSupport(min: Real = Real.zero) extends Support {
  def transform(v: Variable): Real =
    v.exp + min

  def logJacobian(v: Variable): Real = v

  def isDefinedAt(real: Real): Real = (real > min)
}

/**
  * A support representing an open-below {r < k} interval.
  * @param max The upper bound of the distribution
  */
case class BoundedAboveSupport(max: Real = Real.zero) extends Support {
  def transform(v: Variable): Real =
    max - (-1 * v).exp

  def logJacobian(v: Variable): Real = v

  def isDefinedAt(real: Real): Real = (real < max)
}