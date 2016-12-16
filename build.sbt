name := "RunecraftScala"

version := "1.0"

scalaVersion := "2.12.0"

resolvers += "Spigot Repo" at "https://hub.spigotmc.org/nexus/content/groups/public/"

libraryDependencies ++= Seq(
  "org.bukkit" % "bukkit" % "1.11-R0.1-SNAPSHOT",
  "org.scalatest" % "scalatest_2.11" % "3.0.1" % Test
)

scalacOptions ++= Seq(
  "-deprecation", //Emit warning and location for usages of deprecated APIs.
  "-encoding", "UTF-8",
  "-feature", //Emit warning and location for usages of features that should be imported explicitly.
  "-language:implicitConversions", //Explicitly enables the implicit conversions feature
  "-unchecked", //Enable detailed unchecked (erasure) warnings
  "-Xfatal-warnings", //Fail the compilation if there are any warnings.
  "-Xlint", //Enable recommended additional warnings.
  //"-Yinline-warnings", //Emit inlining warnings.
  "-Yno-adapted-args", //Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ywarn-dead-code" //Warn when dead code is identified.
)