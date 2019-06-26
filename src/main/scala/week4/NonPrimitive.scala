package idealized.scala

// take from the polymorphism from week3
// note the covariant annotation of param T here
// which allows us to define Nil as an object that extends List[Nothing]
// instead of a class with type params T that extends List[T]
trait List[+T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
  def prepend[U >: T](elem: U): List[U] = new Cons(elem, this)
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty = false
}

// class Nil[T] extends List[T] {
//   def isEmpty = true
//   // because "Nothing" is a subtype of any type inherits from Object, so it belongs perfectly to the type T
//   def head: Nothing = throw new NoSuchElementException("Nil.head")
//   def tail: Nothing = throw new NoSuchElementException("Nil.tail")
// }

// rewrite the Nil to make it object intead of a class
// by making List a covariant
// object cannot have type parameters as it can only have one instance
// so we change the List type to "Nothing" as it's the bottom type
object Nil extends List[Nothing] {
  def isEmpty = true
  // reading the definition of head & tail in trait List, we can see that head indeed return a "Nothing"
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  // and the tail return instead of a List[Nothing], directly a "Nothing", which is even more the subtype of List[Nothing]
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

// List(1,2) expands to List.apply(1,2)
// so to be able to use this syntax, we need to define the apply method inside List object
object List {
  def apply[T](x: T, y: T) = new Cons(x, new Cons(y, Nil))
  def apply[T]() = Nil

  val x: List[String] = Nil 
}
