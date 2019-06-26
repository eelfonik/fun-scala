## keywords:
### `val`
define a constant (or value)

**Immutable**

EX:
```scala
scala> val num = 42
num: Int = 42
```

here scala guess the type of the constant by using *type inference*, but normally it’s better to specify the type explicitly like this:

```scala
scala> val num: Short = 42
num: Short = 42
```

### `var`
define a variable, very rare used

**mutable**

Just like `let` in js, `var` can be used as local variables that store temporary data or accumulate values in loops

### `def`
define a method

当定义method的时候，传入的arguments必须要指定type, 而return type是optional的（但强烈建议不要依赖type inference来猜测return type，要不然会容易造成误会，在def的后面加个冒号就行， 如下）

```scala
scala> def greet(name: String) : String = "hello " + name
greet: (name: String)String
```


Scala mostly uses *expressions* instead of statements to alter the control flow，因为scala的expression总是会默认return一个value (QUES: like treat procedure as data in Lisp?)

#### Method with return value (this is default) 
在一个def的method里，return value就是它的last expression. 

#### Method without return value
如果你希望一个def的method不要return value, 而是类似一个用来处理side effects的procedure, 可以指定return type为Unit（Unit其实就是表示absence of value）, 如下：

```scala
scala> def greet2(): Unit = println("Hello World")
greet2: ()Unit
```

#### Parameterless method 
没有arguments的method可以省略括号, 称为parameterless method:

```scala
scala> def id = Math.random
id: Double
scala> id
res10: Double = 0.15449866168176285
```

这个时候你就可以像是access一个variable/constant一样access这个method了，因为这个特性，在parameterless method里，绝对不要有side effects，要不然会非常让人迷惑


#### 把一个定义好的method赋值给一个`val`或者`var`,

- 如果method还不存在， 你可以如下

```scala
val greet: String => String = (name) => "hello " + name
greet("lee")
res2: String = hello lee
// 此处String => String是type of the function，可以读作 a function from String to String
```

- 如果method已经存在了，例如已经有个method叫做 greet,则需要用一个特殊的syntax告诉compiler这东西不是一个*function call*，而是一个*function reference*:

```scala
val gr = greet _
```


#### `def` vs `val` when defining values

def是*call by name*, 而val是*call by value*

Make sense,,,, as def is defining sth to a name, then you can refer the sth by that name after, but val is giving value to sth. Consider the following:

```scala
val x = 2

val y = square(2) 
// y refers to value 4

def y = square(2)
// y refers to expression square(2)
```

## Collections:

基本上就是一堆类似`list`,`array`之类的东西，分成两大类，**immutable & mutable**

完整的collection => https://www.scala-lang.org/api/current/scala/index.html

- **Immutable collection**:

例如`List`, `Array`

这一类的collection不能改变自身的值，但是有一些定义好的

  - Method用来return a new collection，比如`:+`

  ```scala
  val list: List[Int] = List(1,2,3)
  list :+ 4 
  res10: List[Int] = List(1,2,3,4)
  list.:+(5)
  res11: List[Int] = List(1,2,3,5)
  ```

注意这两种写法，有dot和没有dot,因为只有一个param,所以前者是后者的简略写法（省掉了dot以及parenthese）

  - access的方法无论对List还是Array都是一样,用括号：

```scala
list(1)
res: Int = 2
```

- **Mutable collection**:

例如`ListBuffer`
 
这类一般用来累加一些值，最后把它变成一个immutable collection来用

```scala
val listBuffer = scala.collection.mutable.ListBuffer.empty[Int]
listBuffer += 1
listBuffer += 2
listBuffer.toList
res: List[Int] = List(1,2)
```

## Functions:

Function定义的时候可以传入默认参数，例如

```scala
def sayBye(name: String = "kino") = "bye, " + name

sayBye
res: String = bye, kino
sayBye("hh")
res: String = bye, hh
```

或者更进一步，我们可以使用`implicit`这个词，然后在没有传入参数的时候，scala会自动在**current scope**里寻找定义为implicit，并且type为String的值，然后使用它 (如果current scope里有超过一个以上的值符合条件，你会收到一个error: `ambiguous implicit values`)

```scala
def bye2(implicit name: String) : String = "bye, " + name

bye2
error: could not find implicit value for parameter name: String

implicit val whatever: String = "k"
bye2
res: String = bye, k
```


implicit的值也可以import进来，不过如果是一个*class with companion object*, scala也会在这个companion object里寻找implicit的值，例如：

```scala
class Bye[T](val name: T) {
    override def toString = "Bye(" + name + ")"
}
object Bye {
    implicit val bye: Bye[String] = new Bye("kino")
}

def bye(implicit bye: Bye[String]): String = "hello " + bye.name

bye

res: String = hello kino
```

Multiple ways to define a function: 

- 可以用`def`, 或者`val` (基本上就跟js里的 function / const keyword差不多）
比如之前的这个例子：

```scala
val greet: String => String = (name) => "hello " + name

greet("lee")
```


## Loop & condition:

基本上不用*if-statements*, 而是*if-expressions*, 例如：

```scala
def isEven(num: Int) = {
    if (num % 2 == 0)
        true
    else
        false
}
```

注意这里我们没有指定return value的类型，因为在if expression里，两个分支(*if branch & else branch*)有可能会return不同类型的value,所以scala会自动使用*type inference*来决定return value的类型：
- 如果两个分支的return value的type相同，则return value就是这个type
- 如果两个分支不同，则寻找这两个value type**最近的共同**value type，例如假设上例中的else,如果return null，则这个method的return value会变成`AnyRef`

`while` loop很少用。。。

## String Interpolation:

- 跟js里的string literal差不多，就是可以在一个string的内部执行一些函数or pass some vals, 只要加一个s的前缀，再用`"`：

```scala
val name = "kino"
val hello = s"hello ${name}"
// OR 
val hello = s"hello $name"
val hello1 = s"hello ${name.toUpperCase}"

hello: String = hello kino
hello1: String = hello KINO
```

- 如果需要 spread string literal across multiple lines (多行) or include a lot of special characters without escaping(即把所有的特殊符号都当成raw text来对待)，可以使用`""""""`

```scala
val json="""
    {
        firstName: "Joe",
        lastName: "Black"
    }
"""
```


## Import Packages:

- 可以在文件开头import,也可以在定义的class或者function内部import

- 可以import特定的一个东西，
`import scala.collection.mutable.ListBuffer`

- 可以import特定的某几个个东西，
`import somepackage.{SomeThing, SomeOtherThing}`

- 也可以用wildcard来import整个package (注意：跟别的语言不一样，scala的wildcard用下划线`_`而不是星号`*`),
`import scala.collection.mutable._`