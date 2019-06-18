package week2

object rationals {
  val x = new Rational(1, 3)
  val y = new Rational(5, 7)
  val z = new Rational(3, 2)

  // usage
  x.sub(y).sub(z)
  // note the operator "add" or "sub" is used by dot notation
  // and scala enables the infix version of any operator with argument:
  x sub y sub z
  // As the identifier in scala is very flexible, it can be Alphanumeric, symbolic 
  // we can define the "sub/add" method in Rational class by "-/+"
  // That is : def - (y: Rational) = this + y.neg
  // then we can use this def with infix syntax,
  // which results a nice and identical ways for rational number as for Int:
  x - y - z

  // if you use symbol as identifier, the general precedence rules will applied according to the first character:
  x * y - y * z 
  // the above is the same as (x * y) - (y * z)
}

class Rational(x: Int, y: Int) {
  // require is a predefined function to force requirements
  // raise an IllegalArgumentException if not satisfied
  require(y != 0, "denominator cannot be zero!")

  // private keyword means only visible inside class
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a
    else gcd(b, a % b)

  private val g = gcd(x, y)

  def numer = x / g
  def denom = y / g

  // a second constructor takes only 1 argument, so you can use val x = new Rational(2)
  // note the usage of "this" here, it refers to the class itself inside class
  def this(x: Int) = this(x, 1)

  // the override indicates overriding the default method
  // to print to stdout
  override def toString = numer + "/" + denom

  // N.B the add method takes only one argument
  // as the left operand of this method is the rational number itself as we defined by class
  // the following 2 are equivalent
  def add(another: Rational) =
    new Rational(numer * another.denom + another.numer * denom, denom * another.denom)
  def + (another: Rational) =
    new Rational(numer * another.denom + another.numer * denom, denom * another.denom)

  // as for operator don't have params, we need to use "unary"
  // and for an operator end with symbol (+-%*), it's required to have a space after the name
  // because ":" itself is a valid symbol value, so it'll be merged with the name if has no space
  def neg: Rational = new Rational(-numer, denom)
  def unary_- : Rational = new Rational(-numer, denom)

  // 2 equivalent sub function
  def sub(y: Rational) = add(y.neg)
  // N.B.
  // 1. usage of "this": as we use the infix operator, we need to pass the left hand operand explicitly
  // 2. the unary operator "-", why we cannot define "def unary_aaa: Rational = E" ?
  def - (y: Rational) = this + -y

  def mul(y: Rational) =
    new Rational(numer * y.numer, denom * y.denom)
  def * (y: Rational) =
    new Rational(numer * y.numer, denom * y.denom)
}