import AssemblyKeys._

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.2"

unmanagedBase := baseDirectory.value / ".idea/libraries"

seq(assemblySettings: _*)