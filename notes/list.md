## List is an important building block

### Basic

At basic, `List` **is** a sequence, and it's a recursive tree structure, while `Array` is flat.

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
    - `foldLeft`
