import java.text.SimpleDateFormat
import java.util.{Calendar, Properties}

name := "Aethercraft"
version := "1.0"

javacOptions ++= Seq("-Xlint", "-encoding", "UTF-8")
scalaVersion := "2.12.8"
scalacOptions ++= Seq(
  "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8",                // Specify character encoding used by source files.
  "-explaintypes",                     // Explain type errors in more detail.
  "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  "-language:implicitConversions",     // Explicitly enables the implicit conversions feature
  "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
  "-Xfuture",                          // Turn on future language features.
  "-Ypartial-unification",             // Enable partial unification in type constructor inference
  "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  //"-Xlint",                            // Enables every warning. scala -Xlint:help for a list and explanation
  "-Xlint:_,-unused",                  // Enables every warning except "unused". scala -Xlint:help for a list and explanation
  "-Ywarn-dead-code",                  // Warn when dead code is identified.
  "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
  "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
  "-Ywarn-unused:privates",            // Warn if a private member is unused.
  "-Ywarn-unused:locals",              // Warn if a local definition is unused.
  "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
//  "-Ywarn-unused:params",              // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
  "-Ybackend-parallelism", "12",        // Maximum worker threads for backend
)

resolvers += "Spigot Repo" at "https://hub.spigotmc.org/nexus/content/groups/public/"
val circeVersion = "0.11.1"
libraryDependencies ++= Seq(
  "org.bukkit" % "bukkit" % "1.13.2-R0.1-SNAPSHOT" % Provided,
  "com.beachape" %% "enumeratum" % "1.5.13",
  "com.beachape" %% "enumeratum-circe" % "1.5.13",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.typelevel" %% "cats-core" % "1.5.0",
  //"org.scalatest" %% "scalatest" % "3.0.1" % Test,
)

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  name.value + "-" + version.value + "." + artifact.extension
}

resourceGenerators in Compile += Def.task {
  val file = (resourceManaged in Compile).value / "plugin.yml"
  val date = new SimpleDateFormat("yyyyMMdd-HHmm").format(Calendar.getInstance().getTime)
  val pluginYml =
    s"""name: Aethercraft
        |version: ${version.value}-$date
        |author: Amuxix
        |main: me.amuxix.bukkit.Aethercraft
        |api-version: 1.13
     """.stripMargin
  IO.write(file, pluginYml)
  Seq(file)
}.taskValue

mappings in(Compile, packageBin) += {
  (resourceManaged.value / "plugin.yml") -> "plugin.yml"
}

//assemblyMergeStrategy in assembly := (_ => MergeStrategy.concat)
/*assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
  case "reference.conf"                                 => MergeStrategy.concat
  case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
  case _                                                => MergeStrategy.first
}*/

val serverProperties = settingKey[Properties]("The test server properties")
serverProperties := {
  val prop = new Properties()
  IO.load(prop, new File("project/server.properties"))
  prop
}

assemblyOutputPath in assembly := new File(serverProperties.value.getProperty("testServerPluginsLocation", assemblyOutputPath.toString)) / (assemblyJarName in assembly).value
