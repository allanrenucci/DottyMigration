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
}
