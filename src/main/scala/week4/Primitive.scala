package idealized.scala

abstract class TBoolean {
  def ifThenElse[T](thenExpr: => T, elseExpr: => T): T

  def && (x: => TBoolean): TBoolean = ifThenElse(x, False)
  def || (x: => TBoolean): TBoolean = ifThenElse(True, x)
  def unary_! : TBoolean = ifThenElse(False, True)

  def == (x: => TBoolean): TBoolean = ifThenElse(x, !x)
  def != (x: => TBoolean): TBoolean = ifThenElse(!x, x)

  // assume false < true
  def < (x: => TBoolean): TBoolean = ifThenElse(False, x)
}

object True extends TBoolean {
  def ifThenElse[T](thenExpr: => T, elseExpr: => T) = thenExpr
}

object False extends TBoolean {
  def ifThenElse[T](thenExpr: => T, elseExpr: => T) = elseExpr
}

// non-negative natural number
abstract class Nat {
  def isZero: Boolean
  def predecessor: Nat
  def succesor: Nat = new Succ(this)
  def +(that: Nat): Nat
  def -(that: Nat): Nat
}

object Zero extends Nat {
  def isZero = true
  def predecessor: Nat = throw new Error("predecessor of zero")
  def +(that: Nat): Nat = that
  def -(that: Nat): Nat = if (that.isZero) this else throw new Error("zero cannot minus another natural number")
}

class Succ(n: Nat) extends Nat {
  def isZero = false
  def predecessor: Nat = n

  def +(that: Nat): Nat = new Succ(n + that)
  def -(that: Nat): Nat = if (that.isZero) this else n - that.predecessor

  // we can also implement with accumulator

  // private def accumulator(fAcc: Nat => Nat)(indicator: Nat, acc: Nat): Nat = {
  //   if (indicator.isZero) acc
  //   else accumulator(fAcc)(indicator.predecessor, fAcc(acc))
  // }

  // def +(that: Nat): Nat = {
  //   def addAccumulator = accumulator(y => y.succesor)_
  //   addAccumulator(that, this)
  // }

  // note the sub here does't need to take care about the case of "that" being zero
  // as we use "that" as the indicator of accumulation, it will terminate once reached zero
  // and if "that" is greater than "this", then the y.predecessor will throw an error
  // which is exactly the behaviour we want
   
  // def -(that: Nat): Nat = {
  //   def subAccumulator = accumulator(y => y.predecessor)_
  //   subAccumulator(that, this)
  // }
}