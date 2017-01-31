import io.circe.parser._
import io.circe.syntax._
import me.amuxix.material.Material
import me.amuxix.material.Material.Air

val a: Material = Air
val json = a.asJson
decode[Material](a.asJson.spaces2) match {
  case Left(_) => None
  case Right(material)
}