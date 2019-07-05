package week6

import scala.io.Source

object phoneNumber {
  val mnem = Map(
    '2' -> "ABC",
    '3' -> "DEF",
    '4' -> "GHI",
    '5' -> "JKL",
    '6' -> "MNO",
    '7' -> "PQRS",
    '8' -> "TUV",
    '9' -> "WXYZ"
  )

  // given a string of numbers
  // find all the possible letter string values it could present
  // note the assumption that the string num represents a single word
  def numCode(num: String): List[String] = {
    val mnemWithDefault = mnem.withDefaultValue("")
    num.foldLeft(List(""))((acc, n) => {
      acc.flatMap(str => mnemWithDefault(n).map(ltr => str + ltr))
    })
  }

  // map every Char 'A'....'Z' to '2'...'9'
  val charCode: Map[Char, Char] = mnem.flatMap(m => {
    val (numChar, letterString) = m
    letterString.map(c => (c -> numChar))
  })

  // or
  // val charCode: Map[Char, Char] = 
  //   for ((digit, str) <- mnem; ltr <- str) yield ltr -> digit

  // represent a word with corresponding numeric string
  // EX: "SCALA" to "72252"
  // using the fact that map is also a function: 
  def wordCode(word: String): String = word.toUpperCase.map(charCode)

  // or
  // def wordCode(word: String): String = 
  //   for (ltr <- word) yield charCode(ltr.toUpper)

  // if we have a predefined list of words, like `List("SHK", "ewr", "HT", "qgj")`
  // we can construct a Map of wordsForNum
  // which for a given number string, we can have a list of possible words
  val words = List("SHK", "ewr", "HT", "qgj")
  val wordsForNum: Map[String, List[String]] = words groupBy wordCode withDefaultValue List()
  // res: Map[String,List[String]] = HashMap(48 -> List(HT), 745 -> List(SHK, qgj), 397 -> List(ewr))

  // here the num string represents a sentence instead of a single word
  def encode(num: String): Set[List[String]] = ???
}