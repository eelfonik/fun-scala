## Decomposition

A pratical problem: find a general and convient way to access objects in a extensible class hierarchy.

Example [here](../src/main/scala/week4/).

### Hacky solution: type tests and type casts

- 我们可以使用 `isInstanceOf` 来确定一个东西的type. (**type tests**)

比如定义了一个method greet2:

```scala
def greet2(name: String) : String = “goodbye “ + name

greet2.isInstanceOf[AnyRef]
res10: Boolean = true

greet2.isInstanceOf[Object]
res11: Boolean = true

// 注意此处 scala.AnyRef 就是 java.lang.Object, 所以第二个判断也返回为true
```

- 然后也可以使用`asInstanceOf`来强制指定一个type (**type casts**)

```scala
greet2.asInstanceOf[Object]
// treat greets as an instance of "Object"
```

Those 2 are very low level and is discouraged in Scala, we have better alternatives.

### OO style of decomposition

```scala
trait Base {
  def eval: Int
}

class Number(n: Int) extends Base {
  def eval: Int = n
}

class Sum(e1: Base, e2: Base) extends Base {
  def eval: Int = e1.eval + e2.eval
}

class Prod(e1: Base, e2: Base) extends Base {
  def eval: Int = e1.eval * e2.eval
}
```

If in the program you need mostly creating new *subclasses*, it's a perfect fine solution. Unless you need to frequently add new `methods`: 

The disavantage is
- if we want to add another method to the classes, we need to touch the `trait` and all subclasses that extends it.
- if we want to have some simplification like `a * b + a * c = a * (b + c)`, it involves 2 classes `Prod` & `Sum`, so we cannot do it in a single object without reference to another object.

### Pattern matching

If we look at the decomposition, all we want to do ( the *test* (`is`) and *accesor* (`as`) method for example ) is to **reverse** the construction process:
- **Which** subclass was used ? -> *test*
- **What** were the arguments used in constructor ? -> *accessor*

Scala use `case class` to adress this problem, see detailed notes about [case class](class-and-object.md#case-class), and the usage as [ADT](types.md#ADT).

In general, **pattern matching** is like `switch` statement is other languages, but instead of only be able to case test *primary values* (like `string`, `number`), it's expanded to *class hierarchy level*.

#### Some syntactical decision for pattern matching
- to distinguish a **variable** `n` which can match *anything*, with a **constant** `N` can only match *one value*, syntactically the first letter of variables must be lower case, and upper case for constant
- cannot use the *same variable name* in matching

#### Evaluation of a pattern matching
Assume we have a pattern matching like following:

```scala
def eval(e: Expr): Int = e match {
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
}

eval(Sum(Number(1), Number(2)))
```

1. replace the param by the value

```scala
Sum(Number(1), Number(2)) match {
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
}
```

2. evaluation the match expression

```scala
// bound `Number(1)` to variable `e1`
// and `Number(2)` to varaible `e2`
eval(Number(1)) + eval(Number(2))
```

3. repeat for the `Number`

```scala
Number(1) match {
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
} + eval(Number(2))

// becomes
1 + eval(Number(2))

// Then
1 + 2

// finally
3
```

#### Match anything
Pattern matching can match values, types, regex, so it's very powerful.

```scala
someVal match {
  // if someVal is Nil
  case Nil => throw new Error("patternMatch: things went wrong")
  // if someVal is a List of Any type (this is type matching)
  case list: List[Any] => flatten(list)
  // if someVal equals to string "text" (this is a value mathing)
  case n == "text" => n
  // for all other cases, just return someVal
  case _ => someVal
}
```

One thing to keep in mind is pattern matching is **sequential** : once it had a fulfilled case, it will not keep looking for all the following cases, so be carefull with the **order** of possible *overlapping* case clauses.

### for expression

#### General form
`for (s <- Seq if p(s)) yield f(s)`

is the same as saying: 

`Seq filter p map f`

To generalize the for expression:

```scala
/*
We can use `{}` instead of `()` to be able to write multiple generators in multiple lines
*/
for {
  s1 <- Seq1 // generator 1
  s2 <- Seq2 // generator 2
  ...
  sN <- SeqN // generator n
  if p(s1, ...sN) // filter/predict function (optional)
} yield f(s1, ...sN) // map function

// you can see the things like this
for (s1 <- Seq1.withFilter(s1 => p)) yield s1
```

#### Combine with pattern matching
Actually the for expression can be combined with pattern matching to be more powerful, that is, **the left-hand of a generator can also be a pattern** :

```scala
// note case class is just another way of defining pattern match
abstract class Item
case class MapItem(bindings: Map[Int, String]) extends Item
case class StringItem(value: String) extends Item

val seq1: List[Item] = List(
  MapItem(Map(1 -> "a", 2 -> "b", 3 -> "c")),
  MapItem(Map(1 -> "a", 2 -> "b1", 3 -> "c1")),
  MapItem(Map(1 -> "a2", 2 -> "b2", 3 -> "c2")),
  StringItem("someString")
) // a list of Item type

for {
  MapItem(s1) <- seq1 // this line is pattern matching for all MapItem
  if s1(1) == "a"
} yield s1(2)

// and the map & filter version is not so concise as before
seq1.map(s => s match {
  case MapItem(s) => {
    if (s(1) == "a") s(2)
    else "" // and you need to take care yourself here to return the same type for empty values to be able to filter
  }
  case _ => ""
}).filter(s => !s.isEmpty)
```

## Partial function

The definition of `PartialFunction` trait is a subtype of `Function1`, who implements the `isDefinedAt` method

```scala
trait PartialFunction[-A, +R] extends Function1[-A, +R]{
  def apply(x: A): R
  def isDefinedAt(x: A): R
}

// then with a f like
val f: PartialFunction[List[Int], String] = {
  case Nil => "one"
  case x :: y :: rest => "two"
}

f.isDefinedAt(List(1,2,3))
// res1: Boolean = true
f(List(1,2,3))
// String = two

// N.B if we have a g function as following
val g: PartialFunction[List[Int], String] = {
  case Nil => "one"
  case x :: rest => rest match {
    case Nil => "two"
  }
}

// `isDefinedAt` still return true
g.isDefinedAt(List(1,2,3))
// res2: Boolean = true

// but we actually have a match error
// as the isDefinedAt only check the outer most level of pattern matching
g(List(1,2,3))
// scala.MatchError ....
```

## Higher order function
- partial application ??
- closure ??


## DI dependency injection


大致是: 不要在每个controller里都用new来重复initialize某个service class, 要不然整个application里就会有很多个该class的instance

Existential type:

```scala
// when you define a list and don’t care about the type of list elements
def printContents(list: List[_]): Unit = list.foreach(println(_))
```


