package app

import org.slf4j.LoggerFactory

object Main {
  val log = LoggerFactory.getLogger(this.getClass)

  val M = new Param {
    override val name: String = "M"

    val hasParent = value("HasParent")
    val hasOptionalParent = value("HasOptionalParent")
    val noParent = value("no-parent")
    end
  }

  val TABLE = new Param {
    override val name: String = "table"

    val empty = value("empty")
    val nonEmpty = value("non-empty")
    end
  }

  val PARENT_ID = new Param {
    override val name: String = "parentId"

    val none = value("None")
    val some = value("Some")
    end
  }

  val SIBLINGS = new Param {
    override val name: String = "siblings"

    val yes = value("yes")
    val no = value("no")
    end
  }

  def main(args: Array[String]): Unit = {
    generateTestCasesForInsertOrdered
  }

  def generateTestCasesForInsertOrdered: Unit = {
    val params: List[Param] = List(M, TABLE, PARENT_ID, SIBLINGS)

    val impossible: List[List[(Param, String)]] = List(
      List(M.noParent, PARENT_ID.some)
      ,List(M.noParent, TABLE.nonEmpty, SIBLINGS.no)
      ,List(M.hasParent, PARENT_ID.none)
      ,List(TABLE.empty, SIBLINGS.yes)
    )

    val combs: List[List[(String, String)]] = TestCasesGenerator.generateTestCases(params, impossible)
    println(TestCasesGenerator.toFreeSpec2(combs))
  }

}
