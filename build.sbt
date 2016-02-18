//import AssemblyKeys._


organization  := "com.ipsumllc"

version       := "0.8"

scalaVersion  := "2.11.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

javacOptions ++= Seq("-source", "1.8")

//resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

//resolvers += "krasserm at bintray" at "http://dl.bintray.com/krasserm/maven"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Spray Repository" at "http://repo.spray.io")

libraryDependencies ++= {
  val akkaVersion  = "2.3.9"
  val sprayVersion = "1.3.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.3.1",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "io.spray" %% "spray-testkit" % sprayVersion % "test",
    "org.specs2" %% "specs2" % "2.3.13" % "test",
    "com.typesafe.akka"   %% "akka-persistence" % "2.4.1",
    "org.iq80.leveldb"            % "leveldb"          % "0.7",
    "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8",
    "com.github.fernandospr" % "javapns-jdk16"  % "2.3.1",
    "com.notnoop.apns" % "apns" % "0.2.3"
  )
}

//test in assembly := {}
//jarName in assembly := "blah.jar"

Revolver.settings
