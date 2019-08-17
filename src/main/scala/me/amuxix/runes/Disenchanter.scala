package me.amuxix.runes

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.{catsStdInstancesForOption, toTraverseOps}
import me.amuxix._
import me.amuxix.inventory.Item
import me.amuxix.material.Material.{Dirt, RedstoneBlock, RedstoneTorch, RedstoneWire}
import me.amuxix.pattern._
import me.amuxix.position.BlockPosition

object Disenchanter extends RunePattern[Disenchanter] {
  override val runeCreator: RuneCreator = Disenchanter.apply
  override val activatesWith: Option[Item] =|> Boolean = {
    case Some(item) if item.isEnchanted => true
  }
  // format: off
  override val layers: List[BaseLayer] = List(
    ActivationLayer(
      RedstoneWire, RedstoneWire,  RedstoneWire,
      RedstoneWire, RedstoneTorch, RedstoneWire,
      RedstoneWire, RedstoneWire,  RedstoneWire,
    ),
    Layer(
      NotInRune, NotInRune,     NotInRune,
      NotInRune, RedstoneBlock, NotInRune,
      NotInRune, NotInRune,     NotInRune,
    ),
  )
  // format: on
}

case class Disenchanter(
  center: BlockPosition,
  creator: Player,
  direction: Direction,
  rotation: Matrix4,
  pattern: Pattern
) extends Rune {

  override def validateActivationItem(
    activationItem: Option[Item]): Option[String] =
    activationItem match {
      case Some(item) if item.isEnchanted => None
      case _ => Some("This rune must be activated with an enchanted item")
    }

  /**
    * Internal activate method that should contain all code to activate a rune.
    */
  override protected def onActivate(activationItem: Option[Item]): EitherT[IO, String, Boolean] = {
    val setRedstoneBlockToDirt = (center + Down).block.setMaterial(Dirt).toLeft(())
    activationMessage = activationItem.fold(activationMessage)(_.name + " has been disenchanted")

    for {
      item <- EitherT.fromOption[IO](activationItem, "No activation tool.")
      _ <- EitherT.liftF(center.strikeLightningEffect)
      _ <- EitherT.liftF(activator.position.traverse(_.strikeLightningEffect))
      _ <- setRedstoneBlockToDirt
      _ <- item.disenchant
    } yield true
  }

  /**
    * Should this rune use a true name if the activator is wearing one?
    */
  override val shouldUseTrueName: Boolean = true
}

