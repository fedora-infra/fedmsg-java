name := "fedmsg"

version := "1.0.0"

organization := "org.fedoraproject"

publishTo := Some(Resolver.file("file", new File( "releases" )) )

libraryDependencies ++= Seq(
  "org.zeromq" % "jeromq" % "0.3.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.0",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.3.0",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.50"
)
