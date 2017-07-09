package app

object TestCasesGenerator {
  def generateTestCases(params: List[Param], impossible: List[List[(Param, String)]]): List[List[(String, String)]] = {
    val impossibleStr = impossible.map(_.map{case (param, value) => (param.name, value)})
    combinations(params).filterNot(line=>impossibleStr.exists(_.forall(line.contains)))
  }

  def toString(combs: List[List[(String, String)]]) =
    combs.map(_.map { case (name, variant) => s"$name: $variant" }.mkString(", ")).mkString("\n")

  def toFreeSpec(combs: List[List[(String, String)]], level: Int = 0): String = combs match {
    case Nil => ""
    case _ => {
      assert(combs.forall(_.size == combs.head.size))
      val prefix = " "*(level*2)
      (for {
        ((param, value), rest) <- groupByHead(combs.filter(_.nonEmpty))
      } yield
        s"""$prefix"$param: $value" ${if (rest.head.isEmpty) "in" else "-"} {
           |$prefix${toFreeSpec(rest, level+1)}
           |$prefix}""".stripMargin.replaceAllLiterally("\r\n", "\n")
      ).mkString("\n")
    }
  }

  protected[app] def groupByHead(combs: List[List[(String, String)]]): List[((String, String), List[List[(String, String)]])] = {
    combs.span(_.head == combs.head.head) match {
      case (Nil, _) => Nil
      case (prefix, rest) => (prefix.head.head, prefix.map(_.tail))::groupByHead(rest)
    }

  }


  private def combinations(cases: List[Param]): List[List[(String, String)]] = cases match {
    case Nil => List(Nil)
    case head::rest =>
      val prevs = combinations(rest)
      for {
        value <- head.possibleValues
        prev <- prevs
      } yield (head.name -> value) :: prev
  }
}