package week4

object listMethods {
  // insertion ascending sort for List
  def isort(list: List[Int]) : List[Int] = list match {
    case Nil => List()
    case x :: xs => insert(x, isort(xs))
  }

  def insert(x: Int, xs: List[Int]): List[Int] = xs match {
    case Nil => List(x)
    case y :: ys => {
      if (x < y) x :: xs
      else y :: insert(x, ys)
    }
  }
}