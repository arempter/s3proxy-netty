import scalariform.formatter.preferences._

name := "s3proxy-netty"

version := "0.1"

scalaVersion := "2.12.6"

scalacOptions += "-unchecked"
scalacOptions += "-deprecation"
scalacOptions ++= Seq("-encoding", "utf-8")
scalacOptions += "-target:jvm-1.8"
scalacOptions += "-feature"
scalacOptions += "-Xlint"
scalacOptions += "-Xfatal-warnings"

libraryDependencies += "io.netty" % "netty-all" % "4.1.28.Final"
libraryDependencies += "com.typesafe" % "config" % "1.2.1"

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DanglingCloseParenthesis, Preserve)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DoubleIndentMethodDeclaration, true)
  .setPreference(NewlineAtEndOfFile, true)
  .setPreference(SingleCasePatternOnNewline, false)
