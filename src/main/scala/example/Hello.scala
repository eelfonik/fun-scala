package example

object Hello extends Greeting with App {
  println(greeting)
}

// or
// object Hello {
//   def main(args: Array[String]) = println("hello")
// }

trait Greeting {
  lazy val greeting: String = "hello"
}
