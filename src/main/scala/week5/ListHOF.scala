package week5

//read it higher order function, or hall of fame 😉

object listHof {
  def map[T, U](f: T => U)(xs: List[T]): List[U] = xs match {
    case Nil => List()
    case head :: tl => f(head) :: map(f)(tl)
  }

  // def squareList(xs: List[Int]): List[Int] = xs match {
  //   case Nil => xs
  //   case y :: ys => y * y :: squareList(ys)
  // }

  def squareList(xs: List[Int]): List[Int] = map((x: Int) => x * x)(xs)

  def filter[T](f: T => Boolean)(xs: List[T]): List[T] = xs match {
    case Nil => xs
    case head :: tl => if (f(head)) head :: filter(f)(tl) else filter(f)(tl)
  }


  def pack[T](xs: List[T]): List[List[T]] = xs match {
    case Nil => Nil
    case head :: tail => {
      val (same, rest) = xs span (el => el == head)
      same :: pack(rest)
    }
  }

  pack(List("a", "a", "a", "b", "c", "c", "a"))
  // List(List("a", "a", "a"), List("b"), List("c", "c"), List("a"))

  // a common encode used for compression
  def encode[T](xs: List[T]): List[(T, Int)] = map((x: List[T]) => (x.head, x.length))(pack(xs))

  encode(List("a", "a", "a", "b", "c", "c", "a"))
  // List(("a", 3), ("b", 1), ("c", 2), ("a", 1))

  def reduceLeft[T](xs: List[T])(op: (T, T) => T): T = xs match {
    case Nil => throw new Error("reduceLeft: empty list")
    case head :: tl => foldLeft(tl)(head)(op)
  }

  def foldLeft[U, T](xs: List[T])(acc: U)(op: (U, T) => U): U = xs match {
    case Nil => acc
    case head :: tl => foldLeft(tl)(op(acc, head))(op)
  }

  foldLeft(List(1, 2, 3))(0)((x, y) => x + y)
  // res0: Int = 6

  foldLeft(List(1, 2, 3))("")((x, y) => x + y.toString)
  // res1: String = 123

  def mapFun[T, U](xs: List[T], f: T => U): List[U] = (xs foldRight List[U]())(f(_) :: _)

  // can we replace `foldRight` with `foldLeft` here ?
  def lengthFun[T](xs: List[T]): Int = (xs foldRight 0)((el, acc) => 1 + acc)
}