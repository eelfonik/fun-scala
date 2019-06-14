package week1

object Sqrt {
  def sqrt(x: Double) = {

    def sqrtIter(guess: Double): Double = 
      if (goodEnough(guess))
        guess
      else
        sqrtIter(improve(guess))
  
    def goodEnough(guess: Double): Boolean =
      math.abs(guess * guess - x) / x <= 0.001

    def improve(guess: Double): Double =
      (guess + x / guess) / 2

    sqrtIter(1.0)
  
  }
}