package week3

// type parameters T
trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}

// what's the difference in class param with/without `val`?
// It's the same as
// class Cons[T](_head: [T], _tail: List[T]) extends List[T] {
//   this.head = _head
// or def head = _head
//   this.tail = _tail

//   //other code...
// }

// the `val` in class params are called field params, which has the same effect as we define (def)
// this field inside class

// But without even `val`, I can still use the head param directlly in class body
// so what is `this.head = _head` for ?
class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty = false
}

class Nil[T] extends List[T] {
  def isEmpty = true
  // because "Nothing" is a subtype of any type inherits from Object, so it belongs perfectly to the type T
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

// so we can use the definition above on functions
object Poly {
  def singleton[T](ele: T) = new Cons[T](ele: T, new Nil[T])

  // singleton[Int](1)
  // or simply singleton(1)  scala can do the type inference
  // or singleton[Boolean](true)

  val listTest = new Cons(1, new Cons(2, new Cons(3, new Nil)))

  def nth[T](n: Int, list: List[T]):T = 
    if (list.isEmpty) throw new IndexOutOfBoundsException("nth")
    else if (n == 0) list.head
    else nth(n-1, list.tail)
}
