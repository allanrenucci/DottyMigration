package fix

import scalafix.testkit.SyntacticRuleSuite

class SyntacticTest extends SyntacticRuleSuite(DottyMigrationRule) {
  implicit class StatOps(stat: String) {
    def wrapped: String =
      s"""class W {
         |  $stat
         |}
       """.stripMargin
  }

  check("macro 0",
    "def foo: T = macro MacroImpl".wrapped,
    "def foo: T = ???".wrapped
  )

  check("macro 1",
    "def foo: T = macro a.b".wrapped,
    "def foo: T = ???".wrapped
  )

  check("macro 2",
    "def foo = macro MacroImpl".wrapped,
    "def foo = ???".wrapped
  )

  check("macro 3",
    "def foo(x: T1): T2 = macro MacroImpl".wrapped,
    "def foo(x: T1): T2 = ???".wrapped
  )

  check("macro 4",
    "def foo:T=macro MacroImpl".wrapped,
    "def foo:T=???".wrapped
  )
}
