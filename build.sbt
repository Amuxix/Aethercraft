import java.text.SimpleDateFormat
import java.util.{Calendar, Properties}

name := "Aethercraft"
version := "1.0"

javacOptions ++= Seq("-Xlint", "-encoding", "UTF-8")
scalaVersion := "2.13.0"
scalacOptions ++= Seq(
  "-encoding", "utf-8",                // Specify character encoding used by source files.
  "-explaintypes",                     // Explain type errors in more detail.
  "-language:higherKinds",             // Allow higher-kinded types
  "-language:postfixOps",              // Explicitly enables the postfix ops feature
  "-language:implicitConversions",     // Explicitly enables the implicit conversions feature
  "-Ybackend-parallelism", "4",        // Maximum worker threads for backend.
  "-Ybackend-worker-queue", "10",      // Backend threads worker queue size.
  "-Ymacro-annotations",               // Enable support for macro annotations, formerly in macro paradise.
  "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
  "-Xmigration:2.14.0",                // Warn about constructs whose behavior may have changed since version.
  //"-Xfatal-warnings", "-Werror",       // Fail the compilation if there are any warnings.
  "-Xlint:_",                          // Enables every warning. scalac -Xlint:help for a list and explanation
  "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
  "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
  "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  "-Wdead-code",                       // Warn when dead code is identified.
  "-Wextra-implicit",                  // Warn when more than one implicit parameter section is defined.
  //"-Wnumeric-widen",                   // Warn when numerics are widened.
  "-Woctal-literal",                   // Warn on obsolete octal syntax.
  //"-Wself-implicit",                   // Warn when an implicit resolves to an enclosing self-definition.
  "-Wunused:_",                        // Enables every warning of unused members/definitions/etc
  "-Wunused:patvars",                  // Warn if a variable bound in a pattern is unused.
  "-Wunused:params",                   // Enable -Wunused:explicits,implicits. Warn if an explicit/implicit parameter is unused.
  "-Wunused:linted",                   // -Xlint:unused <=> Enable -Wunused:imports,privates,locals,implicits.
  //"-Wvalue-discard",                   // Warn when non-Unit expression results are unused.
)
// These lines ensure that in sbt console or sbt test:console the -Ywarn* and the -Xfatal-warning are not bothersome.
// https://stackoverflow.com/questions/26940253/in-sbt-how-do-you-override-scalacoptions-for-console-in-all-configurations
/*scalacOptions in (Compile, console) ~= (_ filterNot { option =>
  option.startsWith("-Ywarn") || option == "-Xfatal-warnings" || option.startsWith("-Xlint")
})*/
//scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value

resolvers ++= Seq(
  Opts.resolver.sonatypeSnapshots,
  Opts.resolver.sonatypeReleases,
  "Spigot Snapshots Repo" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots/",
  "Spigot Repo" at "https://https://hub.spigotmc.org/nexus/content/repositories/releases/",
)
val circeVersion = "0.12.0-RC1"
libraryDependencies ++= Seq(
  //"org.bukkit" % "bukkit" % "1.14.4-R0.1-SNAPSHOT" % Provided,
  "net.md-5" % "bungeecord-chat" % "1.13-SNAPSHOT",
  "org.spigotmc" % "spigot-api" % "1.14.2-R0.1-SNAPSHOT" % Provided exclude("net.md-5", "bungeecord-chat"),
  "com.beachape" %% "enumeratum" % "1.5.13",
  "com.beachape" %% "enumeratum-circe" % "1.5.21",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  //"org.typelevel" %% "cats-core" % "1.6.0",
  "org.typelevel" %% "cats-effect" % "2.0.0-RC1",
  "com.github.pathikrit" %% "better-files"   % "3.8.0",
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
        |main: me.amuxix.bukkit.Bukkit
        |api-version: 1.14
        |""".stripMargin
  IO.write(file, pluginYml)
  Seq(file)
}.taskValue

mappings in(Compile, packageBin) += {
  (resourceManaged.value / "plugin.yml") -> "plugin.yml"
}

val serverProperties = settingKey[Properties]("The test server properties")
serverProperties := {
  val prop = new Properties()
  IO.load(prop, new File("project/server.properties"))
  prop
}

assemblyOutputPath in assembly := new File(serverProperties.value.getProperty("testServerPluginsLocation", assemblyOutputPath.toString)) / (assemblyJarName in assembly).value
