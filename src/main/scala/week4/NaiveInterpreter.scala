package idealized.scala

/*
some options to do decomposition
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
a better way is using pattern matching for eval
*/

trait Expr

case class Number(n:Int) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr

object interpreter {
  def eval(e: Expr): Int = e match {
    case Number(n) => n
    case Sum(e1, e2) => eval(e1) + eval(e2)
  }
}


