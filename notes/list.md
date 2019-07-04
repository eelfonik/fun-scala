## List is an important building block

### Basic

At basic, `List` **is** a sequence, and it's a *recursive tree structure*, while `Array` is flat.

### create a list

  ```scala
  List(x1, ..., xn)
  ```

- `List` is *homogeneous*: it can contain only the **same type** of elements (but that *type* can be `Any`, the ultimate top class, so you can still put anything if you want... 😹)

- `List` is constructed by
  - the empty list `Nil`
  - and a constructor `cons`, or `::`, so that `x :: xs` means getting a new List with first element (`head`) as `x`, combined with the elements in another List `xs` (`tail`). 

    **N.B.**
    - think a List with only one element `el` is actually `el :: Nil`
    - the operators end with `:` are **right associative**, while all other operators are left associative. And those right associative operators are treat as the *method call* of its **right operand** : So `1 :: 2 :: Nil` is the same as `Nil.::2.::1` -> see the `prepend` operation we defined [here](../src/main/scala/week4/NonPrimitive.scala).

### pattern match 

for `List` decomposition

  ```scala
  List(x)
  x :: Nil // a list with the first element as x

  List(x :: xs) // a list A of a list B, where the list B consistents of a first element `x`, and a tail
  ```

### Methods for `List`

  A complete doc is [here](https://www.scala-lang.org/api/2.13.0/scala/collection/immutable/List.html)

- Basic methods:

  - `xs.length`
  - `xs.last`, `xs.init`: reversed version of `xs.head`, `xs.tail`
  - `xs take n`, `xs drop n` : get the first `n` elements of a list, or take out the first `n` elements from a list
  - `xs ++ ys` or `xs concate ys` or `xs ::: ys`
  - `xs.reverse`
  - `xs updated (n,x)`: update a list at index `n`

- HOF methods:
  - filter & partition
    - `xs filter (x => x > 0)`
    - `xs filterNot (x => x > 0)`
    - `xs partition (x => x > 0)`

  - until we meet some condition
    - `xs takewhile (x => x > 0)`
    - `xs dropwhile (x => x > 0)`
    - `xs span (x => x > 0)`

  - fold & reduce
    
    in scala `reduce` is a special case of `fold`, as reduce requires reduce a list to the **same type**, because it will automatically take the **first element** in list as the initial value of "accumulator".
    
    Whereas `fold` can take another param as the initial accumulator with **any types** ( thus you can do some tricky transformations on list item which cannot use `map`, that is, the result of the transformation don't have a one-to-one relationship with original list, Ex: the `flatten` method implemented here [at the end](../src/main/scala/week5/List.scala) ).
    
    Roughly the signature of reduce `reduce(T, T): T` is a special case of `fold(A)(A, B): A`, where `A` is exactly the same type as `B`, that is `fold(T)(T, T):T`
    - `reduceLeft`
    - `reduceRight`
    - `foldLeft`
    - `foldRight`

    > Q: How about `reduce` & `fold`? The signature seems different than their left or right version ?

### Prove of list methods

- Natural induction（自然归纳法🙄)

  for a `p(n)` for **all intergers** `>= b`

  - show the `p(n)` hold for a base case `p(b)`
  - then try to prove if one has `p(b)`, it also has `p(b+1)` (this is called *induction step*)

We cas use *induction steps* to prove functional methods, because they don't have side effect, and can be replaced by the term it reduce to ( **referential transparency**, the simplest form to understand that: if you define `val x = 3 + 4`, then x can be reduced to `7`, so anywhere you use the variable `x`, you can safely replace it with `7` -> this value `7` is *transparent* to its *reference* `x` )

- Structual induction

  for a `P(n)` for **all lists** `xs`
  - show the `P(n)` hold for a base case `p(Nil)`
  - then try to prove with `xs` and some element `x`,  if one has `P(xs)`, it also has `P(x :: xs)`

#### Example to prove `concat`(`++`) of list is associative:

the `p`s are:
- `(xs ++ ys) ++ zs = xs ++ (ys ++ zs)` (`p1`)
- `Nil ++ xs = xs` (`P2`)
- `xs ++ Nil = xs` (`P3`)

take the definition of `concat`

```scala
def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
  case Nil => ys
  case head :: tail => head :: concat(tail, ys)
}
```
For `P1`
- base case `xs == Nil`
  - left hand : `(Nil ++ ys) ++ zs` -> `(ys) ++ zs`
  - right hand : `Nil ++ (ys ++ zs)` -> `ys ++ zs` ✅
- induction step `x :: xs`
  - left hand : `((x :: xs) ++ ys) ++ zs` -> `(x :: (xs ++ ys)) ++ zs` -> `x :: ((xs ++ ys) ++ zs)` -> `x :: (xs ++ (ys ++ zs))`
  - right hand : `(x :: xs) ++ (ys ++ zs)` -> `x :: (xs ++ (ys ++ zs))` ✅

For `P3`
- base case
  - left hand : `Nil ++ Nil` -> `Nil`
  - right hand : `Nil` ✅
- induction steps
  - left hand : `(x :: xs) ++ Nil` -> `x :: (xs ++ Nil)` -> `x :: xs`
  - right hand : `x :: xs` ✅

#### Example to prove `reverse`:

The `p` is `xs.reverse.revers = xs`

take the definition of `reverse`

```scala
// inefficient reverse but easier to proof
def reverse[T](xs: List[T]): List[T] = xs match {
  case Nil => Nil
  case head :: tail => reverse(tail) ++ List(head)
}
```

- base case `xs == Nil`
  - left hand : `Nil.reverse.reverse`
    1. `Nil.reverse` (1st clause of reverse def)
    2. `Nil` (1st clause of reverse def)
  - right hand : `Nil` ✅
- induction step `x :: xs`
  - left hand `(x :: xs).reverse.reverse`
    1. `(xs.reverse ++ List(x)).reverse` (2nd clause)
  - right hand `x :: xs`
    1. `x :: xs.reverse.reverse` (induction hypothesis) ❌

we can not reach any further conclusion without another params

let `ys = xs.reverse`, apply that to the 1st step of induction above, then left hand becomes `(ys ++ List(x)).reverse`, and the right hand `x :: ys.reverse`

So the `p` become `(ys ++ List(x)).reverse = x :: ys.reverse` (this is called **auxiliary equition** 辅助方程) for any list `ys`:

- base case `Nil`
  - left hand : 

    1. `List(x).reverse` (1st clause of `++`)
    2. `(x :: Nil).reverse` (definition of `List`)
    3. `Nil.reverse ++ List(x)` (2nd clause of `reverse`)
    4. `Nil ++ List(x)` (1st clause of `reverse`)
    5. `List(x)` (1st clause of `++`)

  - right hand : 

    1. `x :: Nil` (1st clause of `reverse`)
    2. `List(x)` (definition of `List`) ✅

- induction step `y :: ys`
  - left hand : `((y :: ys) ++ List(x)).reverse`

    1. `(y :: (ys ++ List(x))).reverse` (2nd clause of `++`) *N.B: *: this step is called **unfold** 
    2. `(ys ++ List(x)).reverse ++ List(y)` (2nd clause of `reverse`)
    3. `x :: ys.reverse ++ List(y)` (hypothesis)

  - right hand: `x :: (y :: ys).reverse`

    1. `x :: ys.reverse ++ List(y)` (2nd clause of `reverse`)*N.B: * this step is called **fold**  ✅

  - so the prove of induction step is sometimes called **fold/unfold method**

#### Prove of distribution law of `map`

`p` is `(xs ++ ys) map f = (xs map f) ++ (ys map f)`

take the definition of `map`:

```scala
def map[T, U](f: T => U)(xs: List[T]): List[U] = xs match {
  case Nil => List()
  case head :: tl => f(head) :: map(f)(tl)
}
```

- base case `xs = Nil`

  - left hand : `(Nil ++ ys) map f`

    1. `ys map f` (1st clause of `++`)

  - right hand: `(Nil map f) ++ (ys map f)`
    1. `Nil ++ (ys map f)` (1st clause of `map`)
    2. `ys map f` (1st clause of `++`) ✅

- induction step `x :: xs`

  - left hand: `((x :: xs) ++ ys) map f`

    1. `(x :: (xs ++ ys)) map f` (2nd clause of `++`)
    2. `f(x) :: ((xs ++ ys) map f)` (2nd clause of `map`)
    3. `f(x) :: (xs map f ++ ys map f)` (hypothesis)

  - right hand: `((x :: xs) map f) ++ (ys map f)`

    1. `(f(x) :: xs map f) ++ (ys map f)` (2nd clause of `map`)
    2. `f(x) :: (xs map f ++ ys map f)` (2nd clause of `++`) ✅






