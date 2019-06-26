## some tips
 
- Scala的class不能有static members，可以用object来达到类似的效果 (why no static members ???)
- 可以使用`private`来声明只对class内部可见的methods & vals
- 一些确保data合法的**predefined functions**:
    - `require`,  EX: `require(y != 0, "y cannot be zero!")` -> 如果不满足就会有`IllegalArgumentException`, 这是*precondition*
    - `assert`,  EX: `assert(x > 0)` -> 如果不满足会有`AssertionError`, 这是用来验证或者确保function是正确的（跟test接近了）
- inside a class definition, `this` refers to the class constructor itself, and you can have multiple constructors for a class besides the **default implicit constructor**

## class & companion object

```scala
class Person (val name: String) {
    override def toString = "person(" + name + ")"
}
object Person {
    def apply(name: String): Person = new Person(name)
}

// call it

val kino = Person("kino")
```

**N.B.**

- If an object has the same name as a class, then it’s called a **companion object**. Companion objects are often used in Scala for defining additional methods and implicit values.
注意在这种情况下，the apply method inside object *must have the same constructor parameters* as the original class，比如上例中的apply, 就只能使用一个叫name的param,因为对应的class里只有这样一个param.

- 这样一来我们就不用使用`new`这个keyword了，因为如果直接使用Person()这种形式，apply这个method是默认被call的

> Q: 这里TM到底发生了什么？？
> 1. override是为啥？=> 为了redefine superclass里的默认method
> 2. 指定apply的return value是一个class instance ?
> 3. 然后为什么apply就被默认call了？？？

> A:
> 1. override就是为了让interpreter在默认输出里写出我们指定的句子，加了override之后，屏幕的stdout变成了：`kino: Person = Person(kino)`， 如果不加，就会直接输出 `kino: Person = Person@684ad81c` 
> 2. 就是定义一个？？？ 
> 3. ？？？

**Example**: 

在scala的标准库里，比如那个`List`（这玩意在运行时scala自动import了，所以不用手动import就可以使用），就是:
1. 先定义了一个*parameterized class List*, 在里面override了toString,使得屏幕输出一个类似`List[Int] = List(1,2,3,4)` 的东西，而不是`List[Int] = List@374234`这种，并且包含有一个apply method, 接受一个`index`作为参数，返回在那个index上的element
2. 然后同时还有一个*companion object*也叫List, 在里面定义apply method, 调用List这个class的constructor, 用来init这个object. 

我自己实现的naiveList(Nlist)如下（缺少一个class里的apply method, 用来get element)：

```scala
class Nlist[T](val el: T) {
    override def toString = "Nlist(" + el + ")"
}
object Nlist{
    def apply(el: Int): Nlist[Int] = new Nlist(el)
}
```

## case class

上例可以简化为

```scala
case class Nlist(el: Int)
```

`case class`免除了我们override `toString`这个default method的必要，也不用再定义一个companion object来定义一个默认的apply method，同时还有如下优点：(注意以下的`==` , `!=`, `equals`, `toString`, `hashCode` 都是定义在scala的`Any`这个最顶层的*superclass*里的)
1. override了某些标准库里的`hashCode`这个default method =>这个method能用来干嘛？？？
2. override `equals` method, so we can have two object with same set value equal, EX: `new Nlist(1) == new Nlist(1) returns true`
3. 自动定义了`unapply` method，因此我们可以对这个class使用 **pattern match**

EX:

```scala
val n = Nlist(1)
n match {
  case Nlist(el) => s"hello ${el}"
  case _ => println("nothing")
}

res0: Any = hello 1
```

## abstract class

> Q: 跟trait有啥差别？
> A: 不能一口气继承很多个啊哈哈哈

- Abstract class can contain members *without* implementation :

```scala
abstract class IntSet {
  def include(x: Int): IntSet
  def contains(x: Int): Boolean
}
```

- cannot be instantiate by keyword `new`, but you can `extends` it, and **MUST** implements all the methods:

```scala
class Empty extends IntSet {
  // an empty set contains nothing
  def contains(x:Int): Boolean = false
  // and the include method of an empty set will add the x & 2 empty node
  def include(x:Int): IntSet = new NonEmpty(x, new Empty, new Empty)
  
  override def toString = "."
}
```

- Once extended, we can say that the class `Empty` is a **subclass** of the **baseclass** (or **superclass**) `IntSet`, That means anywhere when you need an object of type `IntSet`, you can use its subclass (the class `Empty` in this case)
- any class that doesn't have baseClasses ( like the abstract `IntSet` class ), is implicitly extended from `Object`
- you can also redefine a implemented method in the superclass, using `override` in the subclass




## Traits
 
Add functionality to a class, it could contain *abstract* (not implemented) or *concrete* (implemented) method

> Q: 这东西跟abstract object有什么区别？？

> A:
> - 更多是interface的意思，但可以包含concrete methods/members, 如下面的trait `A`
> - 另外trait不能有parameters
> - A trait is a kind of class that enables multiple inheritance. 

```scala
trait A {
    def a(): Unit = println("a")
}

trait B {
    def b(): Unit
}
```

这里的trait `A`就包含了一个*concrete method*, 而trait `B`则包含了一个*abstract method*

如果此时你试图使用

```scala
class C extends A with B
```

会得到一个警告： 
```
error: class C needs to be abstract, since method b in trait B of type ()Unit is not defined
```

2 options:

- abstract class

```scala
abstract class D extends A with B
```

但一个不能被instantiated的class有什么用？？？？

- 在定义class的时候，implement trait `B`里的method, 变为*concrete method*

```scala
class C extends A with B {
  def b(): Unit = println("blabal")
}
```


## 总结：
这一部分非常的object oriented, 特别是从一个abstract class or trait extends出来的各种class, 被称之为**dynamic method dispatch**, 即一个method被call的时候，取决于包含了这个method的object的*runtime type*.

> Q: **Dynamic method dispatch** 跟functional programming里使用的**higher order function**非常相似，所以这两者能否互相代替？以及跟多态polymorphism有啥差别？
