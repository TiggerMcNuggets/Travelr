name := "team300-travelea"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.8"



lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += filters
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "org.glassfish.jaxb" % "jaxb-core" % "2.3.0.1"
libraryDependencies += "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.5"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.40"

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")



// FIXED BUG WHEN GENERATING ScalaDoc  https://github.com/scala/bug/issues/11365

scalacOptions in (Compile, doc) += "-no-java-comments"



