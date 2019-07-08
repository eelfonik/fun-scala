package week6

// Seq
object prime {

  def isPrime(n: Int): Boolean = (2 until n) forall (d => n % d != 0)

  val n = 7

  // combinatorial search

  // get a pair of Int within a range n, that combines to a prime
  def pairPrime(n :Int): Seq[(Int, Int)] = {
    // generate all possible pairs then filter out
    (1 until n) flatMap (i => (1 until i) map (j => (i, j)) filter (pair => (isPrime(pair._1 + pair._2))))

    // or use for expression
    for {
      i <- (1 until n)
      j <- (1 until i)
      if isPrime(i + j)
    } yield (i, j)
  }

  def scalarProd(xs: List[Double], ys: List[Double]): Double = {
    (xs zip ys).foldLeft(0.0)((acc, pair) => acc + pair._1 * pair._2)
    // or
    (for ((x, y) <- xs zip ys) yield x * y).sum
  }

  val a = List(1.0, 5.0, 2.9)
  val b = List(3.0, 2.0)
}

// Set
// N queens problem
object nqueens {
  // n is the size of board
  // in the result Set, each List represent a possible solution
  // where the `Int`s represent numbers of column of the queen, with reversed row number
  // that is, the col of (row, col) : (n, a0), (n-1, a1), ...(0, aN)
  def queens(n: Int): Set[List[Int]] = {
    def isSafe(col: Int, existingCols: List[Int]): Boolean = {
      val rows = existingCols.length
      val colPairs = (rows - 1 to 0 by -1) zip existingCols
      // check the col we want to add is not equal to any of the existing cols (same column check)
      // and the difference of col is not equal to the difference of row (diagonal check)
      // note `rows` here is exactly the row reprsents `col`
      colPairs.forall(pair => pair._2 != col && math.abs(col - pair._2) != rows - pair._1)
    }

    def place(k: Int): Set[List[Int]] = {
      if (k == 0) Set(List())
      else for {
        list <- place(k - 1) // the recursion actually happend here
        col <- (0 until n)
        if (isSafe(col, list))
      } yield col :: list
    }
    // start backwards from last row
    place(n)
  }
}

// Map
object polynomial {
  // represent polynomial as a Map from exponents to coeffients
  // Ex: x^3 - 2x + 5
  // Map(0 -> 5, 1 -> -2, 3 -> 1)

  class Polynom(val terms: Map[Int, Double]) {
    def this(bindings: (Int, Double)*) = this(bindings.toMap)

    // interesting part is not to map on the first term, but rather on `other.terms`
    // as the concat of Map will use override strategy instead of merge
    def + (other: Polynom) = new Polynom(terms ++ other.terms.map(term => {
      val (exp, coef) = term
      terms get exp match {
        case Some(thisCoef) => exp -> (coef + thisCoef)
        case None => exp -> coef
      }
    }))

    // or use default value to avoid the pattern match to `Option`
    val terms0 = terms withDefaultValue 0.0
    def add (other: Polynom) = new Polynom(terms0 ++ other.terms0.map(term => {
      val (exp, coef) = term
      exp -> (coef + terms0(exp))
    }))

    // or instead of `++`, use `foldLeft`
    def addWithFoldLeft(other: Polynom) = new Polynom(other.terms0.foldLeft(terms0)((acc, term) => {
      val (exp, coef) = term
      acc + (exp -> (coef + terms0(exp)))
    }))

    override def toString(): String =
      (for ((exp, coef) <- terms.toList.sorted.reverse) yield coef+"x^"+exp) mkString " + "
  }

  val p1 = new Polynom(0 -> 5.0, 1 -> -2.0, 3 -> 1.0)
  //or val p1 = new Polynom(Map(0 -> 5.0, 1 -> -2.0, 3 -> 1.0))

  val p2 = new Polynom(1 -> 2.0, 2 -> 2.4)
  //or val p2 = new Polynom(Map(1 -> 2.0, 2 -> 2.4))

  p1 + p2

  p1 add p2

  p1 addWithFoldLeft p2

  // all the above 3 returns the same result
}