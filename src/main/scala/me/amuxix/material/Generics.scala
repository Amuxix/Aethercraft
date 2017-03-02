package me.amuxix.material


/**
  * Created by Amuxix on 14/02/2017.
  */
sealed trait Generic

trait Tool extends Generic
trait GenericAxe extends Tool
trait GenericHoe extends Tool
trait GenericPickaxe extends Tool
trait GenericShovel extends Tool

trait Armor extends Generic
trait GenericBoots extends Armor
trait GenericChestplate extends Armor
trait GenericHelmet extends Armor
trait GenericLeggings extends Armor

trait Slab extends Generic with Solid
trait GenericSingleSlab extends Slab
trait GenericDoubleSlab extends Slab with Solid

trait GenericPlant extends Generic with Transparent with Attaches with Crushable
trait GenericDoublePlant extends GenericPlant

trait GenericCarpet extends Generic with Transparent with Attaches
trait GenericClayBlock extends Generic with Solid
trait GenericFence extends Generic with Solid with Transparent
trait GenericFenceGate extends Generic with GenericFence with Rotates
trait GenericGlass extends Generic with Solid with Transparent
trait GenericGlassPane extends Generic with GenericGlass
trait GenericLeaves extends Generic with Solid with Transparent with Crushable
trait GenericLog extends Generic with Solid
trait GenericPlank extends Generic with Solid
trait GenericRails extends Generic with Transparent with Attaches
trait GenericSapling extends Generic with Attaches with Transparent with Crushable
trait GenericStairs extends Generic with Solid with Rotates
trait GenericTorch extends Generic with Transparent with Attaches with Rotates
trait GenericWool extends Generic with Solid
trait GenericBanner extends Generic
trait GenericDoor extends Generic
trait GenericSword extends Generic