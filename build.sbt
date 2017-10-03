val scalafixVersion = "0.5.2"
// Use a scala version supported by scalafix.
scalaVersion in ThisBuild := "2.12.3"

lazy val rules = project.settings(
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % scalafixVersion
)

lazy val input = project.settings(
  scalafixSourceroot := sourceDirectory.in(Compile).value,
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
)

lazy val output = project

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % scalafixVersion % Test cross CrossVersion.full,
    buildInfoPackage := "fix",
    buildInfoKeys := Seq[BuildInfoKey](
      "inputSourceroot" ->
        sourceDirectory.in(input, Compile).value,
      "outputSourceroot" ->
        sourceDirectory.in(output, Compile).value,
      "inputClassdirectory" ->
        classDirectory.in(input, Compile).value
    )
  )
  .dependsOn(input, rules)
  .enablePlugins(BuildInfoPlugin)
