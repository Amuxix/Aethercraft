import java.text.SimpleDateFormat
import java.util.Properties
import java.util.Calendar

name := "RunecraftScala"
version := "1.0"

javacOptions ++= Seq("-Xlint", "-encoding", "UTF-8")
scalaVersion := "2.12.5"
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
//  "-Ywarn-numeric-widen",              // Warn when numerics are widened.
  "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
//  "-Ywarn-unused:privates",            // Warn if a private member is unused.
//  "-Ywarn-unused:locals",              // Warn if a local definition is unused.
//  "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
//  "-Ywarn-unused:params",              // Warn if a value parameter is unused.
//  "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
//  "-Ywarn-value-discard",              // Warn when non-Unit expression results are unused.
)

resolvers += "Spigot Repo" at "https://hub.spigotmc.org/nexus/content/groups/public/"
addCompilerPlugin("com.github.ghik" %% "silencer-plugin" % "0.5")
libraryDependencies ++= Seq(
  "org.bukkit" % "bukkit" % "1.12.2-R0.1-SNAPSHOT" % Provided,
  "com.beachape" %% "enumeratum" % "1.5.6",
  "com.beachape" %% "enumeratum-circe" % "1.5.6",
  "com.github.ghik" %% "silencer-lib" % "0.5",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

val circeVersion = "0.9.3"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  name.value + "-" + version.value + "." + artifact.extension
}

resourceGenerators in Compile += Def.task {
  val file = (resourceManaged in Compile).value / "plugin.yml"
  val date = new SimpleDateFormat("yyyyMMdd-HHmm").format(Calendar.getInstance().getTime)
  val pluginYml =
    s"""name: Runecraft
        |version: ${version.value}-$date
        |authors: [Amuxix]
        |main: me.amuxix.Runecraft
     """.stripMargin
  IO.write(file, pluginYml)
  Seq(file)
}.taskValue

mappings in(Compile, packageBin) += {
  (resourceManaged.value / "plugin.yml") -> "plugin.yml"
}

val serverProperties = settingKey[Properties]("The test server properties")
serverProperties := {
  val prop = new Properties()
  IO.load(prop, new File("server.properties"))
  prop
}

lazy val packageToTestServer = taskKey[Unit]("Moves the jar to the test server.")
packageToTestServer := {
  (assembly in Compile).value
  val artifactLocation: File = (assemblyPackageScala / assemblyOutputPath).value

  println(serverProperties)
  val testServerPluginsLocation = Option(serverProperties.value.getProperty("testServerPluginsLocation"))
    .map(new File(_))
    .getOrElse {
      throw new IllegalStateException("TestServerPluginsLocation missing, please add it to perform this operation.")
    }
  require(testServerPluginsLocation.exists(), s"The file ${testServerPluginsLocation.getAbsolutePath} does not exist!")
  IO.move(artifactLocation, testServerPluginsLocation)
  println("Moved Jar to test server.")
}

test in assembly := {}