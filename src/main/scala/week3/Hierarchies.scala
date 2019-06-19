package week3

object intsets {
  // create first a small tree with 2 empty nodes
  val tree1 = new NonEmpty(4, Empty, Empty)
  // then use it
  val tree2 = tree1 include 5
}

// implement sets as binary tree
abstract class IntSet {
  def include(x: Int): IntSet
  def contains(x: Int): Boolean
}

// class Empty extends IntSet {
//   // an empty set contains nothing
//   def contains(x:Int): Boolean = false
//   // and the include method of an empty set will add the x & 2 empty node
//   def include(x:Int): IntSet = new NonEmpty(x, new Empty, new Empty)
  
//   override def toString = "."
// }

// The class definition of Empty is kind of waste, as we need only one instance of Empty
// so we can use an object definition instead of class definition, aka a singleton object
// where we can only has one instance
object Empty extends IntSet {
  // an empty set contains nothing
  def contains(x:Int): Boolean = false
  // and the include method of an empty set will add the x & 2 empty node
  def include(x:Int): IntSet = new NonEmpty(x, Empty, Empty)
  
  override def toString = "."
}

class NonEmpty(value: Int, left: IntSet, right: IntSet) extends IntSet {
  // we defined a rule that the left node's value is always smaller than the root value
  // and the right node's value is greater than the root value
  // so the tree is sorted

  // for contains, we traverse the tree by comparing x with the value, 
  // until we found it, or reach the empty node with `contains` always be false

  // note the infix operator usage here
  // we could also write `if (x < value) left.contains(x)`
  def contains(x: Int): Boolean =
    if (x < value) left contains x
    else if (x > value) right contains x
    else true

  // as we always return a new tree when including, it will return a modified version of tree
  // with either newly created left side tree, or newly created right side tree
  // this is called persistent data structure, as the old tree is untouched
  def include(x:Int): IntSet =
    if (x < value) new NonEmpty(value, left include x, right)
    else if (x > value) new NonEmpty(value, left, right include x)
    else this

  override def toString = "<" + left + value + right + ">"
}