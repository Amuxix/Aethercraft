package me.amuxix.bukkit.inventory.meta

import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import me.amuxix.bukkit.BukkitForm
import me.amuxix.bukkit.Material._
import me.amuxix.bukkit.inventory.Item.{materialDecoder, materialEncoder}
import me.amuxix.bukkit.inventory.meta.VanillaEnchant._
import me.amuxix.bukkit.inventory.meta.metas.SkullMeta
import me.amuxix.inventory.meta
import me.amuxix.inventory.meta.VanillaEnchant.{BINDING_CURSE, VANISHING_CURSE, VanillaEnchant}
import me.amuxix.inventory.meta.{ItemMeta => AetherItemMeta}
import me.amuxix.material.Material
import me.amuxix.material.Material.{PlayerHead => PlayerHeadMaterial}
import me.amuxix.material.Properties.ItemProperty
import me.amuxix.runes.traits.enchants.Enchant
import me.amuxix.runes.traits.enchants.Enchant._
import org.bukkit.inventory.meta.{ItemMeta => BukkitItemMeta, SkullMeta => BukkitSkullMeta}
import org.bukkit.persistence.PersistentDataType
import org.bukkit.{Bukkit, NamespacedKey}

import scala.collection.immutable.HashMap
import scala.jdk.CollectionConverters._

object ItemMeta {
  implicit val encoder: Encoder[ItemMeta] =
    Encoder.forProduct8(
        "material",
      "enchants",
      "vanillaEnchants",
      "localizedName",
      "lore",
      "customModelData",
      "unbreakable",
      "displayName",
      //"itemFlags",
      //"attributeModifiers",
    )(meta =>
      (
        meta.metaOf,
        meta.enchants,
        Option.when(meta.hasVanillaEnchants)(meta.vanillaEnchants),
        Option.when(meta.hasLocalizedName)(meta.localizedName),
        Option.when(meta.hasLore)(meta.lore),
        Option.when(meta.hasCustomModelData)(meta.customModelData),
        meta.isUnbreakable,
        Option.when(meta.hasDisplayName)(meta.displayName),
        //meta.getItemFlags,
        //meta.getAttributeModifiers,
      )
    )

  implicit val decoder: Decoder[ItemMeta] =
    Decoder.forProduct8[ItemMeta, Material with ItemProperty, Map[Enchant, Int], Option[Map[VanillaEnchant, Int]], Option[String], Option[List[String]], Option[Int], Boolean,
      Option[String]](
      "material",
      "enchants",
      "vanillaEnchants",
      "localizedName",
      "lore",
      "customModelData",
      "unbreakable",
      "displayName",
      //"itemFlags",
      //"attributeModifiers",
    ){
      case (material, enchants, vanillaEnchants, localizedName, lore, customModelData, unbreakable, displayName) =>
      val meta = new ItemMeta(material)
      meta.enchants = enchants
      vanillaEnchants.foreach(_.map {
        case (enchant, level) => meta.addVanillaEnchant(enchant, level)
      })
      localizedName.foreach(meta.localizedName_=)
      lore.foreach(meta.lore_=)
      customModelData.foreach(meta.customModelData_=)
      meta.setUnbreakable(unbreakable)
      displayName.foreach(meta.displayName_=)

      meta
    }

  implicit class BukkitItemMetalOps(meta: BukkitItemMeta) {
    def aetherize(material: Material with ItemProperty): AetherItemMeta = new ItemMeta(meta, material)
  }

  implicit class ItemMetaOps(meta: AetherItemMeta) extends BukkitForm[BukkitItemMeta] {
    override def bukkitForm: BukkitItemMeta = meta.asInstanceOf[ItemMeta].itemMeta
  }

  private val metas: Map[Material with ItemProperty, BukkitItemMeta => ItemMeta] = HashMap(
    PlayerHeadMaterial -> (meta => new SkullMeta(meta.asInstanceOf[BukkitSkullMeta]))
  )

  def apply(itemMeta: BukkitItemMeta, metaOf: Material with ItemProperty): ItemMeta = {
    def defaultMeta(meta: BukkitItemMeta): ItemMeta = new ItemMeta(meta, metaOf)
    metas.getOrElse(metaOf, defaultMeta(_))(itemMeta)
  }

  private val enchantNamespacedKey = new NamespacedKey(me.amuxix.bukkit.Bukkit.self, "enchants")
}

protected[bukkit] class ItemMeta protected[bukkit](private val itemMeta: BukkitItemMeta, val metaOf: Material with ItemProperty) extends meta.ItemMeta {
  private def this(material: Material with ItemProperty) = this(Bukkit.getItemFactory.getItemMeta(material.bukkitForm), material)
  lazy val persistentDataContainer = itemMeta.getPersistentDataContainer


  override def enchants: Map[Enchant, Int] = {
    parse(persistentDataContainer.get(ItemMeta.enchantNamespacedKey, PersistentDataType.STRING))
      .flatMap(_.as[Map[Enchant, Int]])
      .toOption
      .fold(Map.empty[Enchant, Int])(identity)
  }

  override def enchants_=(enchants: Map[Enchant, Int]): Unit =
    persistentDataContainer.set(ItemMeta.enchantNamespacedKey, PersistentDataType.STRING, enchants.asJson.noSpaces)


  override def addVanillaCurses(): Unit = {
    addVanillaEnchant(VANISHING_CURSE)
    addVanillaEnchant(BINDING_CURSE)
  }

  override def hasVanillaEnchants: Boolean = itemMeta.hasEnchants

  override def vanillaEnchants: Map[VanillaEnchant, Int] = itemMeta.getEnchants.asScala.toMap.map {
    case (enchant, level) => enchant.aetherize -> level
  }

  override def addVanillaEnchant(enchant: VanillaEnchant, level: Int = 1): Unit = itemMeta.addEnchant(enchant.bukkitForm, level, false)


  override def hasLocalizedName: Boolean = itemMeta.hasLocalizedName

  override def localizedName: String = itemMeta.getLocalizedName

  override def localizedName_=(name: String): Unit = itemMeta.setLocalizedName(name)


  override def hasLore: Boolean = itemMeta.hasLore

  override def lore: List[String] = Option(itemMeta.getLore).toList.flatMap(_.asScala)

  override def lore_=(lore: List[String]): Unit = itemMeta.setLore(lore.asJava)


  override def hasCustomModelData: Boolean = itemMeta.hasCustomModelData

  override def customModelData: Int = itemMeta.getCustomModelData

  override def customModelData_=(customModel: Int): Unit = itemMeta.setCustomModelData(customModel)


  override def isUnbreakable: Boolean = itemMeta.isUnbreakable

  override def setUnbreakable(unbreakable: Boolean): Unit = itemMeta.setUnbreakable(unbreakable)


  override def hasDisplayName: Boolean = itemMeta.hasDisplayName

  override def displayName: String = itemMeta.getDisplayName

  override def displayName_=(name: String): Unit = itemMeta.setDisplayName(name)
}