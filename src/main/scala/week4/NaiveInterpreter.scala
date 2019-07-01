package idealized.scala

/*
Naive way of doing decomposition: write them all
*/

// trait Expr {
//   def isNumber: Boolean
//   def isSum: Boolean
//   def numValue: Int
//   def leftOp: Expr
//   def rightOp: Expr
// }

// class Number(n: Int) extends Expr {
//   def isNumber: Boolean = true
//   def isSum: Boolean = false
//   def numValue: Int = n
//   def leftOp: Expr = throw new Error("Number.leftOp")
//   def rightOp: Expr = throw new Error("Number.rightOp")
// }

// // Sum(e1, e2) corresponds to e1 + e2
// class Sum(e1: Expr, e2: Expr) extends Expr {
//   def isNumber: Boolean = false
//   def isSum: Boolean = true
//   def numValue: Nothing = throw new Error("Sum.value")
//   def leftOp: Expr = e1
//   def rightOp: Expr = e2
// }

// object interpreter {
//   def eval(e: Expr): Int = 
//     if (e.isNumber) e.numValue
//     else if (e.isSum) eval(e.leftOp) + eval(e.rightOp)
//     else throw new Error("unknown expression" + e)

//   // imagine we want to add things like `Product`, `Var`
//   // it will force us to add more methods inside the trait to handle each newly added class
//   // one hacky way to avoid doing this, is using type test & type cast as following: 

//   // def eval(e:Expr): Int =
//   //   if (e.isInstanceOf[Number])
//   //     e.asInstanceOf[Number].numValue
//   //   else if (e.isInstanceOf[Sum])
//   //     eval(e.asInstanceOf[Sum].leftOp) + eval(e.asInstanceOf[Sum].rightOp)
//   //   else throw new Error("unknown expression" + e)
// }


/*
OO way of doing decomposition
- define an abstract method in base trait
- implement this method in subclasses
*/

// trait Expr {
//   def eval: Int
// }

// class Number(n: Int) extends Expr {
//   def eval: Int = n
// }

// class Sum(e1: Expr, e2: Expr) extends Expr {
//   def eval: Int = e1.eval + e2.eval
// }

// class Prod(e1: Expr, e2: Expr) extends Expr {
//   def eval: Int = e1.eval * e2.eval
// }

/* 
pattern matching for decompose
*/

trait Expr

case class Number(n:Int) extends Expr
case class Var(x: String) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr
case class Prod(e1: Expr, e2: Expr) extends Expr

object interpreter {
  // some implicite val for Var(x) ?
  // how to pass the variable dict to eval ?
  def eval(e: Expr): Int = e match {
    case Number(n) => n
    case Var(x) => ???
    case Sum(e1, e2) => eval(e1) + eval(e2)
    case Prod(e1, e2) => eval(e1) * eval(e2)
  }

  def show(e: Expr): String = {
    def paren(e:Expr) = {
      e match {
        case Sum(_, _) => s"(${show(e)})"
        case _ => show(e)
      }
    }
    e match {
      case Number(n) => s"${n}"
      case Var(x) => x
      case Sum(e1, e2) => s"${show(e1)} + ${show(e2)}"
      case Prod(e1, e2) => s"${paren(e1)} * ${paren(e2)}"
    }
  }

  Sum(Prod(Number(2), Var("x")), Var("y"))
  // 2 * x + y
  Prod(Sum(Number(2), Var("x")), Var("y"))
  // (2 + x) * y
  Prod(Sum(Number(2), Var("x")), Sum(Var("y"), Number(3)))
  // (2 + x) * (y + 3)
}

// we can also define the `eval` method directly in base trait

// trait Expr {
//   def eval: Int = this match {
//     case Number(n) => n
//     case Sum(e1, e2) => e1.eval + e2.eval
//   }
// }


