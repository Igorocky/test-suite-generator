package app

import scala.util.parsing.combinator.RegexParsers

case class TestSuite(title: String, comment: Option[String], testCases: List[TestCase])
case class TestCase(title: String, body: String)

class FreeSpecParser extends RegexParsers {

}
