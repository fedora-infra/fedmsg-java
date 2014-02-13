name := "fedmsg"

version := "1.0.0"

organization := "org.fedoraproject"

libraryDependencies ++= Seq(
  "org.functionaljava" % "functionaljava" % "3.1",
  "org.zeromq" % "jzmq" % "3.0.1",
  //"org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.0",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.3.0",
  //"org.bouncycastle" % "bcprov-jdk16" % "1.46",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.50"
)
