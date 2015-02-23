//import AssemblyKeys._

organization  := "com.ipsumllc"

version       := "0.1"

scalaVersion  := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "krasserm at bintray" at "http://dl.bintray.com/krasserm/maven"

libraryDependencies ++= {
  val akkaV = "2.1.4"
  val sprayV = "1.1.1"
  Seq(
    "io.spray"            %   "spray-can"     % sprayV,
    "io.spray"            %   "spray-routing" % sprayV,
    "io.spray"            %   "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2"        % "2.2.3" % "test",
    "org.scalaj"          %% "scalaj-http"    % "0.3.16"
    //,"com.typesafe.akka"   %% "akka-persistence-experimental" % "2.4-SNAPSHOT"
    //"org.fusesource.leveldbjni" % "leveldbjni-all" % "1.7",
    //,"com.github.krasserm" %% "akka-persistence-cassandra" % "0.3.4"
  )
}

//test in assembly := {}
//jarName in assembly := "blah.jar"

Revolver.settings
