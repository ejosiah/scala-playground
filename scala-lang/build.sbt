name := "scala-lang"

version := "1.0"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.3.11",
	"com.typesafe.akka" %% "akka-stream" % "2.4.4",
	"com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalaVersion := "2.11.8"
    