package fix

import scalafix._
import scala.meta._
import scala.meta.transversers.Traverser

case object DottyMigrationRule extends Rule("DottyMigration") {

  override def fix(ctx: RuleCtx): Patch =
    fixEnumIdent(ctx) + fixMacro(ctx) + fixExistentialTypes(ctx) //+ fixEtaExpansion(ctx) + fixXml(ctx)

  /** Replace xml literals by ??? */
  private def fixXml(ctx: RuleCtx): Patch = {
    var patch = Patch.empty
    object traverser extends Traverser {
      override def apply(tree: Tree): Unit = tree match {
        case xml: Term.Xml =>
          patch += ctx.replaceTree(xml, "???")
        case _ =>
          super.apply(tree)
      }
    }
    traverser(ctx.tree)
    patch
  }

  /** Replace macro definition by an empty function.
    *
    * {{{
    *   // before
    *   def foo: T = macro MacroImpl
    *
    *   // after
    *   def foo: T = ???
    * }}}
    */
  private def fixMacro(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case m: Defn.Macro =>
        ctx.replaceTree(m.body, "???") + {
          // Remove macro Kw and its following space
          val macroKw = m.tokens.find(_.is[Token.KwMacro]).get
          ctx.removeToken(macroKw) + ctx.removeToken(ctx.tokenList.next(macroKw))
        }
    }.asPatch
  }

  /** For now, only warn on existential types */
  private def fixExistentialTypes(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case e: Type.Existential =>
        val message = LintCategory.warning("Rewrite me!")
        ctx.lint(message.at(e.pos))
    }.asPatch
  }

  /** Replace enum by `enum` */
  private def fixEnumIdent(ctx: RuleCtx): Patch = {
    ctx.tokens.collect {
      case e @ Token.Ident("enum") =>
        ctx.replaceToken(e, "`enum`")
    }.asPatch
  }

  private def fixEtaExpansion(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case e @ Term.Eta(expr) =>
        ctx.replaceTree(e, s"() => ${expr.tokens}")
    }.asPatch
  }
}
