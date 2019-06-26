package idealized.scala

abstract class Boolean {
  def ifThenElse[T](thenExpr: => T, elseExpr: => T): T

  def && (x: => Boolean): Boolean = ifThenElse(x, false)
  def || (x: => Boolean): Boolean = ifThenElse(true, x)
  def unary_! : Boolean = ifThenElse(false, true)

  def == (x: => Boolean): Boolean = ifThenElse(x, !x)
  def != (x: => Boolean): Boolean = ifThenElse(!x, x)

  // assume false < true
  def < (x: => Boolean): Boolean = ifThenElse(false, x)
}

// object true extends Boolean {
//   def ifThenElse[T](thenExpr: => T, elseExpr: => T) = thenExpr
// }

// object false extends Boolean {
//   def ifThenElse[T](thenExpr: => T, elseExpr: => T) = elseExpr
// }

// non-negative natural number
abstract class Nat {
  def isZero: Boolean
  def predecessor: Nat
  def succesor: Nat
  def +(that: Nat): Nat
  def -(that: Nat): Nat
}

object Zero extends Nat {
  def isZero: Boolean = true
  def predecessor: Nat = throw new java.lang.IndexOutOfBoundsException("predecessor of zero")
  def succesor: Nat = new Succ(Zero)
  def +(that: Nat): Nat = that
  def -(that: Nat): Nat = throw new java.lang.IndexOutOfBoundsException("zero cannot minus another number")
}

class Succ(n: Nat) extends Nat {
  def isZero: Boolean = false
  def predecessor: Nat = n
  def succesor: Nat = new Succ(this)
  def +(that: Nat): Nat = ???
}