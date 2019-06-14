package example

object Greet {

  class Bye[T](val name: T) {
    override def toString = "Bye(" + name + ")"
  } 
  object Bye {
    implicit val bye: Bye[String] = new Bye("kino")
  }

  def sayBye(implicit bye: Bye[String]): String = "hello " + bye.name
  sayBye

  def isEven(num: Int) = {
    if (num % 2 == 0)
      true
    else
      false
  }

  def bye1(name: String = "kino") = "Bye, " + name
  bye1()
  bye1("hh")

  // so the implicit will look in scope for an implicit val with String type
  implicit val whatever: String = "k"
  def bye2(implicit name: String) : String = "bye, " + name
  bye2

  def greet2(name: String) : String = "goodbye " + name
  // greet2.isInstanceOf[AnyRef]
  // greet2.isInstanceOf[Object]

}


object TestListBuffer {
  val listBuffer = scala.collection.mutable.ListBuffer.empty[Int]
  listBuffer += 1
  listBuffer += 2
  listBuffer.toList
}

import scala.collection.mutable.ListBuffer

object NaiveList {
  val list: List[Int] = List(1,2,3)
  list :+ 4 
  list.:+(5)

  list(1)

  // class Nlist[T](val el: T) {
  //   override def toString = "Nlist(" + el + ")"
  // }
  // object Nlist{
  //     def apply(el: Int): Nlist[Int] = new Nlist(el)
  // } 

  // or we can replace the above 2 by
  case class Nlist(el: Int)

  val n = Nlist(1)
  n match {
    case Nlist(el) => s"hello ${el}"
    case _ => println("nothing")
  }
}

object PersonGreet {
  class Person (val name: String) {
    override def toString = "person(" + name + ")"
  }
  object Person {
      def apply(name: String): Person = new Person(name)
  }
  val kino = Person("kino")

  val greetVal: String => String = (name) => "hello " + name
  greetVal("lee")

  val gr = greet _
  val num = 42
  // val num: Short = 42

  def greet(name: String) : String = "hello " + name

  def greet2(): Unit = println("Hello World")
  def id = math.random
}

object Music1 {
  sealed trait Symbol
  case class Note(
    name: String,
    duration: String,
    octave: Int
  ) extends Symbol
  case class Rest(
    duration: String
  ) extends Symbol

  val symbol1: Symbol = Note("C", "whole", 3)
  val symbol2: Symbol = Rest("half")

  def SymbolDuration(symbol: Symbol): String =
    symbol match {
      case Note(name, duration, octave) => duration
      case Rest(duration) => duration
    }

  SymbolDuration(symbol1)
}

object Music2 {
  sealed trait NoteName
  case object A extends NoteName
  case object B extends NoteName
  case object C extends NoteName
  case object D extends NoteName
  case object E extends NoteName
  case object F extends NoteName
  case object G extends NoteName

  sealed trait Duration
  case object Whole extends Duration
  case object Half extends Duration
  case object Quarter extends Duration

  sealed trait Symbol
  case class Note(
    name: NoteName,
    duration: Duration,
    octave: Int
  ) extends Symbol
  case class Rest(
    duration: Duration
  ) extends Symbol

  def SymbolDuration(symbol: Symbol): Duration =
    symbol match {
      case Note(name, duration, octave) => duration
      case Rest(duration) => duration
    }
}