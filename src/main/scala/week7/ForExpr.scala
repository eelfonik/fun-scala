package week7

// for expression as queries
object ForExpr {
  case class Book(title: String, authors: List[String])
  val books: List[Book] = List(
    Book(title="sicp", authors = List("sicp, author")),
    Book(title="functional programming", authors = List("fp1, author1", "fp2, author2")),
    Book(title="clean code", authors = List("martin, uncle")),
    Book(title="data intensive application", authors = List("morgan, JP", "just, Kidding")),
    Book(title="algos", authors = List("fp1, author1"))
  )

  // find the book whose author name is martin
  for {
    book <- ForExpr.books
    name <- book.authors
    if (name.contains("martin"))
  } yield book.title

  // find the names of all authors who have writtern at least 2 books
  { for {
    book1 <- ForExpr.books
    book2 <- ForExpr.books
    if (book1 != book2)
    author1 <- book1.authors
    author2 <- book2.authors
    if (author1 == author2)
    } yield author1
  }.distinct

  // another way to remove duplication is use `set` instead of `List` on the original data structure
}