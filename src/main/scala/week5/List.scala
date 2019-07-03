package week5

object listMethods {
  // insertion ascending sort for List
  // at worst case will be O(n*n)
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

  // merge sort is O(n * log(n))
  def mergeSort1(xs: List[Int]): List[Int] = {
    val midpoint = xs.length / 2
    if (midpoint == 0) xs
    else {
      def merge1(a: List[Int], b: List[Int]): List[Int] = {
        (a, b) match {
          case (Nil, _) => b
          case (_, Nil) => a
          case (headA :: tailA, headB :: tailB) => 
            if (headA < headB) headA :: merge1(tailA, b)
            else headB :: merge1(a, tailB)
        }
      }
      val (left, right) = xs splitAt midpoint
      merge1(mergeSort1(left), mergeSort1(right))
    }
  }

  // We need to find a way to make the sort method more generalized
  // as the above only deal with List[Int]

  // The problem is the compare( the `headA < headB` inside merge) methods
  // it can only deal with Int type
  // how can we generalize this depending on type params ?
  // the thing is, don't write a `headA < headB` expr directly inside merge
  // instead, pass a comparison function as param 

  def mergeSort[T](xs: List[T])(lessThan: (T, T) => Boolean): List[T] = {
    val midpoint = xs.length / 2
    if (midpoint == 0) xs
    else {
      def merge(a: List[T], b: List[T]): List[T] = {
        (a, b) match {
          case (Nil, _) => b
          case (_, Nil) => a
          case (headA :: tailA, headB :: tailB) => 
            if (lessThan(headA, headB)) headA :: merge(tailA, b)
            else headB :: merge(a, tailB)
        }
      }
      val (left, right) = xs splitAt midpoint
      merge(mergeSort(left)(lessThan), mergeSort(right)(lessThan))
    }
  }

  // Then we can use the sort this way
  // note you can omit the type of comparison function
  // as the compiler can infer the type of x & y by looking at the passed in List
  val nums = List(3,4,-1,5)
  mergeSort(nums)((x, y) => x < y)

  val names = List("bob", "micheal", "jjj")
  mergeSort(names)((x, y) => x.compareTo(y) < 0)

  // or using buildin Ordering class, which contains methods like `lt`

  // import math.Ordering
  // def mergeSort[T](xs: List[T])(order: Ordering[T]): List[T] = {
  //   ...
  //   if (Ordering.lt(headA, headB)) xxx
  //   ...
  //   merge(mergeSort(left)(order), mergeSort(right)(order))
  // }

  // then use it
  // mergeSort(nums)(Ordering.Int)
  // mergeSort(names)(Ordering.String)


  // `head` is O(1), and `last` is O(n)
  def last[T](list: List[T]): T = list match {
    case List() => throw new Error("last of Empty list")
    case List(x) => x
    case y :: ys => last(ys)
  }

  // this will be O(n), whereas `tail` is O(1)
  def init[T](list: List[T]): List[T] = list match {
    case Nil => throw new Error("init of Empty list")
    case List(x) => List()
    case y :: ys => y :: init(ys)
  }

  // a O(n) (length of first list `xs` ) because of `init`
  // even if we use `head` & `tail`
  // we have to recursively call the `concat` inside another operator
  // which will cause a bigger usage of space
  def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case Nil => ys
    case head :: tail => head :: concat(tail, ys)
      // or concat(init(xs), last(xs) :: ys)
  }

  // a O(n * n) quadratic
  def reverse[T](xs: List[T]): List[T] = xs match {
    case Nil => xs
    case head :: tail => concat(reverse(tail), List(head)) 
  }

  def removeAt[T](xs: List[T], n: Int): List[T] = {
    def iter(acc: List[T], remaining: List[T], index: Int): List[T] = {
      if (index >= remaining.length) remaining
      else if (index == 0) acc ++ remaining.tail
      else iter(acc ++ List(remaining.head), remaining.tail, index - 1)
    }
    iter(List(), xs, n)
    
    // or we should use the existing methods
    // (xs take n) ::: (xs drop n + 1)
  }

  def flatten(xs: List[Any]): List[Any] = xs.foldLeft(List[Any]())((acc, it) => {
    it match {
      case list: List[Any] => acc ::: flatten(list)
      case n => acc :+ n
    }
  })

  flatten(List(List(1, 7), 2, List(3, List(5, 8))))
  // expect res0: List[Any] = List(1, 7, 2, 3, 5, 8)
}