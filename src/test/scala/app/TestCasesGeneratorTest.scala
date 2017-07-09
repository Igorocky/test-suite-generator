package app

import org.scalatest.{FreeSpec, Matchers}

class TestCasesGeneratorTest extends FreeSpec with Matchers {
  val param1 = new Param {
    override val name: String = "param1"
    val v1 = value("v1")
    val v2 = value("v2")
    val v3 = value("v3")
    end
  }

  val param2 = new Param {
    override val name: String = "param2"
    val v1 = value("v1")
    val v2 = value("v2")
    end
  }

  "TestCasesGenerator.generateTestCases should" - {
    "generate all possible combinations in correct order if 'impossible' is empty" in {
      //given
      val params = List(param1, param2)

      //when
      val res = TestCasesGenerator.generateTestCases(params, Nil)

      //then
      res(0) should be (List("param1"->"v1", "param2"->"v1"))
      res(1) should be (List("param1"->"v1", "param2"->"v2"))
      res(2) should be (List("param1"->"v2", "param2"->"v1"))
      res(3) should be (List("param1"->"v2", "param2"->"v2"))
      res(4) should be (List("param1"->"v3", "param2"->"v1"))
      res(5) should be (List("param1"->"v3", "param2"->"v2"))
      res.size should be(6)
    }

    "not generate impossible combinations" in {
      //given
      val params = List(param1, param2)
      val impossible: List[List[(Param, String)]] = List(
        List(param1.v1, param2.v1)
        ,List(param1.v3, param2.v2)
      )

      //when
      val res = TestCasesGenerator.generateTestCases(params, impossible)

      //then
      res(0) should be (List("param1"->"v1", "param2"->"v2"))
      res(1) should be (List("param1"->"v2", "param2"->"v1"))
      res(2) should be (List("param1"->"v2", "param2"->"v2"))
      res(3) should be (List("param1"->"v3", "param2"->"v1"))
      res.size should be(4)
    }

  }

  "TestCasesGenerator.toFreeSpec should produce correct output" in {
    //given
    val params = List(param1, param2)
    val impossible: List[List[(Param, String)]] = List(
      List(param1.v1, param2.v1)
      ,List(param1.v3, param2.v2)
    )

    //when
    val combs = TestCasesGenerator.generateTestCases(params, impossible)
    val res = TestCasesGenerator.toFreeSpec(combs)

    //then
    println(res)
    res.replaceAllLiterally(" ", ".") should be(
      """"param1:.v1".-.{
        |.."param2:.v2".in.{
        |....//param1:.v1,.param2:.v2
        |....pending
        |..}
        |}
        |"param1:.v2".-.{
        |.."param2:.v1".in.{
        |....//param1:.v2,.param2:.v1
        |....pending
        |..}
        |.."param2:.v2".in.{
        |....//param1:.v2,.param2:.v2
        |....pending
        |..}
        |}
        |"param1:.v3".-.{
        |.."param2:.v1".in.{
        |....//param1:.v3,.param2:.v1
        |....pending
        |..}
        |}""".stripMargin.replaceAllLiterally("\r\n", "\n"))
  }

  "TestCasesGenerator.groupByHead should do correct grouping" in {
    //given
    val combs = List(
      List("p1"->"v1","p2"->"v1","p3"->"v1")
      ,List("p1"->"v1","p2"->"v1","p3"->"v2")
      ,List("p1"->"v2","p2"->"v1","p3"->"v1")
      ,List("p1"->"v2","p2"->"v2","p3"->"v1")
      ,List("p1"->"v3","p2"->"v1","p3"->"v1")
    )

    //when
    val groups = TestCasesGenerator.groupByHead(combs)

    //then
    groups should be(
      List(
        ("p1"->"v1") -> List(
          List("p2"->"v1","p3"->"v1")
          ,List("p2"->"v1","p3"->"v2")
        )
        ,("p1"->"v2") -> List(
          List("p2"->"v1","p3"->"v1")
          ,List("p2"->"v2","p3"->"v1")
        )
        ,("p1"->"v3") -> List(
          List("p2"->"v1","p3"->"v1")
        )
      )
    )
  }
}
