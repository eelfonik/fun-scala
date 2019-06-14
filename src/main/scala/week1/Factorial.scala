package week1

object Factorial {
  // tail recursion version
  def factorial(x: Int) = {
    def facIter(prev: Int, count: Int): Int = 
      if (count > x)
        prev
      else
        facIter(prev * count, count + 1)
    
    facIter(1, 1)
  }
}