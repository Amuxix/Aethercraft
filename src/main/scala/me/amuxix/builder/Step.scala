package me.amuxix.builder

import cats.data.EitherT
import cats.effect.IO
import me.amuxix.Player
import me.amuxix.block.Block
import me.amuxix.inventory.Item
import me.amuxix.material.Material
import me.amuxix.material.Properties.BlockProperty
import me.amuxix.position.{BlockPosition, Vector3}
/*import io.circe.{ Decoder, Encoder }
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import cats.implicits.toFunctorOps

object Step {
  implicit val encodeStep: Encoder[Step] = Encoder.instance {
    case breakStep: BreakStep => breakStep.asJson
    case buildStep: BuildStep => buildStep.asJson
    case teleportStep: TeleportStep => teleportStep.asJson
    case displaceStep: DisplaceStep => displaceStep.asJson
    case notificationStep: NotificationStep => notificationStep.asJson
  }

  implicit val decodeStep: Decoder[Step] =
    List[Decoder[Step]](
      Decoder[BreakStep].widen,
      Decoder[BuildStep].widen,
      Decoder[TeleportStep].widen,
      Decoder[DisplaceStep].widen,
      Decoder[NotificationStep].widen,
    ).reduceLeft(_ or _)
}*/

sealed abstract class Step(val actions: Int) {
  val io: EitherT[IO, String, Unit]
}
/*sealed abstract class ReversibleStep(actions: Int) extends Step(actions, io) {
  val io: EitherT[IO, String, Unit]
  val reverseIO: EitherT[IO, String, Unit]
}*/

case class BreakStep(player: Player, block: Block, item: Item, drops: Boolean) extends Step(1){
  override val io: EitherT[IO, String, Unit] = block.breakUsing(player, item).toLeft(())
}
case class BuildStep(player: Player, material: Material with BlockProperty, where: BlockPosition) extends Step(1) {
  override val io: EitherT[IO, String, Unit] = where.block.setMaterial(material).toLeft(())
}
case class TeleportStep(player: Player, block: Block, target: BlockPosition) extends Step(2) {
  override val io: EitherT[IO, String, Unit] = block.moveTo(target, player).toLeft(())
}
case class DisplaceStep(player: Player, block: Block, displacementVector: Vector3[Int]) extends Step(2) {
  override val io: EitherT[IO, String, Unit] = block.move(displacementVector, player).toLeft(())
}
case class NotificationStep(player: Player, message: String) extends Step(0) {
  override val io: EitherT[IO, String, Unit] = EitherT.liftF(player.notify(message))
}