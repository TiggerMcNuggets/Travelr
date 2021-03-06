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
libraryDependencies += "io.ebean.test" % "ebean-test-config" % "11.36.1" % "test"
libraryDependencies += "commons-io" % "commons-io" % "2.5"


// Use Play Evolutions
libraryDependencies += evolutions
libraryDependencies ++= Seq(evolutions, jdbc)

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")


libraryDependencies += javaJdbc % Test

// https://mvnrepository.com/artifact/com.openhtmltopdf/openhtmltopdf-core
libraryDependencies += "com.openhtmltopdf" % "openhtmltopdf-core" % "1.0.0"
// https://mvnrepository.com/artifact/com.openhtmltopdf/openhtmltopdf-pdfbox
libraryDependencies += "com.openhtmltopdf" % "openhtmltopdf-pdfbox" % "1.0.0"
// https://mvnrepository.com/artifact/org.jsoup/jsoup
libraryDependencies += "org.jsoup" % "jsoup" % "1.12.1"




// Dependecy for iCal4j
// https://mvnrepository.com/artifact/org.mnode.ical4j/ical4j
libraryDependencies += "org.mnode.ical4j" % "ical4j" % "3.0.10"


// Dependencies for cucumber tests
libraryDependencies ++= Seq (
  "io.cucumber" % "cucumber-core" % "4.2.0" % " test " ,
  "io.cucumber" % "cucumber-jvm" % "4.2.0" % " test " ,
  "io.cucumber" % "cucumber-junit" % "4.2.0" % " test " ,
  "io.cucumber" % "cucumber-java" % "4.2.0",
  "org.xerial" % "sqlite-jdbc" % "3.25.2",
  javaWs
)

// FIXED BUG WHEN GENERATING ScalaDoc  https://github.com/scala/bug/issues/11365
scalacOptions in (Compile, doc) += "-no-java-comments"

// Set Java version to 11?
////scalacOptions += "-target:11"
//javacOptions ++= Seq("-source", "jdk-11", "-target", "jdk-11")

// Set config for testing
javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

// Maintainer
maintainer := "frd15@uclive.ac.nz"