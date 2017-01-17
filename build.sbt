import java.text.SimpleDateFormat
import java.util.Calendar

name := "RunecraftScala"
version := "1.0"

scalaVersion := "2.12.0"
scalacOptions ++= Seq(
  "-deprecation", //Emit warning and location for usages of deprecated APIs.
  "-encoding", "UTF-8",
  "-feature", //Emit warning and location for usages of features that should be imported explicitly.
  "-language:implicitConversions", //Explicitly enables the implicit conversions feature
  "-unchecked", //Enable detailed unchecked (erasure) warnings
  //"-Xfatal-warnings", //Fail the compilation if there are any warnings.
  "-Xlint", //Enable recommended additional warnings.
  //"-Yinline-warnings", //Emit inlining warnings.
  //"-Yno-adapted-args", //Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ywarn-dead-code" //Warn when dead code is identified.
)

resolvers += "Spigot Repo" at "https://hub.spigotmc.org/nexus/content/groups/public/"
libraryDependencies ++= Seq(
  "org.bukkit" % "bukkit" % "1.11.2-R0.1-SNAPSHOT",
  "com.beachape" %% "enumeratum" % "1.5.6",
  "org.scalatest" % "scalatest_2.11" % "3.0.1" % Test
)

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  name.value + "-" + version.value + "." + artifact.extension
}

resourceGenerators in Compile += Def.task {
  val file = (resourceManaged in Compile).value / "plugin.yml"
  val date = new SimpleDateFormat("yyyyMMdd-HHmm").format(Calendar.getInstance().getTime)
  val pluginYml =
    s"""name: Runecraft
        |version: ${version.value}-$date
        |authors: [ Zeerix, SuperLlama, C'tri, RivkiinShadows, josiah42, Amuxix, lvletei ]
        |main: me.amuxix.Runecraft
        |website: http://forums.bukkit.org/threads/Runecraft.14897/
     """.stripMargin
  IO.write(file, pluginYml)
  Seq(file)
}.taskValue

mappings in(Compile, packageBin) += {
  (resourceManaged.value / "plugin.yml") -> "plugin.yml"
}

val libFolderName = "libs"
packageOptions in (Compile, packageBin) += Package.ManifestAttributes("Class-Path" -> (baseDirectory.value / libFolderName).list.map(libFolderName + "\\" + _).mkString(" "))

def firstLine(f: java.io.File): Option[String] = {
  val src = io.Source.fromFile(f)
  try {
    src.getLines.find(_ => true)
  } finally {
    src.close()
  }
}

lazy val packageToTestServer = taskKey[Unit]("Moves the jar to the test server.")
packageToTestServer := {
  (Keys.`package` in Compile).value
  val artifactLocation: File = (artifactPath in (Compile, packageBin)).value
  val line: Option[String] = firstLine(baseDirectory.value / "project" / "TestServerPluginsLocation")
  if (line.isDefined) {
    val runecraft = new File(line.get).listFiles().filter(_.name.toLowerCase.contains(name.value.toLowerCase)).head
    IO.move(artifactLocation, runecraft)
  } else {
    println("TestServerPluginsLocation missing, please add it to perform this operation.")
  }
  println("Moved Jar to test server.")
}
/*
lazy val createZip = taskKey[Unit]("Creates a zip to release a new version of Runecraft bundled with scala.")
moveJar := {
  (moveJar in ThisBuild).value
  //IO.zip()
}*/

//(Keys.`package` in Compile) <<= (Keys.`package` in Compile) dependsOn packageWithVersion