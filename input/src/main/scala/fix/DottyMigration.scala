/*
rule = "class:fix.DottyMigrationRule"
*/
package fix

object DottyMigration {
  val a = <a></a>
  val b = <b>{<c/>}</b>
  val c =
    <a>
      <b/>
    </a>

  def foo = 3
  val bar = foo _
  val enum = 1
  enum.toString
}
