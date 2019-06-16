package week2

object Curry {
  def sum(f: Int => Int)(a: Int, b: Int): Int =
    if (a > b) 0 else f(a) + sum(f)(a + 1, b)
  
  //cube sum
  sum(x => x * x * x)(1, 10)

  //simple sum
  sum(x => x)(1, 10)

  // The real advantage of curried version is we can use partial application
  val sumCube = sum(x => x * x * x)_
  // note the `_` after, it's needed to express intention of partial application
  // or we can denote the type to say our intention
  // see https://stackoverflow.com/questions/15304523/why-does-scala-require-partial-application-of-curried-functions-when-assigning-t
  // val sumCube: (Int, Int) => Int = sum(x => x * x * x)

  // then later on we can use this `sumCube` in different context
  sumCube(2,5)
  sumCube(1,9)

  def product(f: Int => Int)(a: Int, b: Int): Int = 
    if (a > b) 1 else f(a) * product(f)(a + 1, b)

  // applying product to define factorial
  def factorial(n: Int) =
    product(x => x)(1, n)

  //general version of both sum & product
  // we just add a base value, and an accumulator function to say how to `reduce` them
  def accumulate(base: Int, accumulator: (Int, Int) => Int)(f:Int => Int)(a: Int, b: Int): Int =
    if (a > b) base else accumulator(f(a), accumulate(base, accumulator)(f)(a + 1, b))

  // the the sum will be
  val sumAccumulate = accumulate(0, (x, y) => x + y)_
  // the product will be
  val productAccumulate = accumulate(1, (x, y) => x * y)_


  // A complete example: use fixedPoint & averageDump to rewrite sqrt
  // fixedPoint means find an x for a function, where x = f(x)
  def fixedPoint(f: Double => Double)(initialGuess: Double) = {
    def fixedPointIter(guess: Double): Double = {
      val next = f(guess)
      if (isGoodEnough(guess, next)) next
      else fixedPointIter(next)
    }

    def isGoodEnough(guess: Double, next: Double): Boolean =
      math.abs((guess - next) / guess) / guess < 0.0001
      
    fixedPointIter(initialGuess)
  }

  // N.B the averageDump here must be a function that returns another function
  def averageDump(f: Double => Double)(x: Double) =
    (x + f(x)) / 2

  def sqrt(n: Double) = fixedPoint(averageDump(x => n / x))(1.0)
}