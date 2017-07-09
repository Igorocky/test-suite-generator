package app

trait Param extends Enum[String] {
  val name: String
  lazy val possibleValues = allElems
  protected def value(str: String): (Param, String) = {
    addElem(str)
    this -> str
  }
}