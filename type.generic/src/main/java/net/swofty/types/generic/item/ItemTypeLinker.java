package net.swofty.types.generic.item;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.Rarity;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.items.SandboxItem;
import net.swofty.types.generic.item.items.accessories.*;
import net.swofty.types.generic.item.items.accessories.abicases.*;
import net.swofty.types.generic.item.items.accessories.dungeon.AutoRecombobulator;
import net.swofty.types.generic.item.items.accessories.dungeon.CatacombsExpertRing;
import net.swofty.types.generic.item.items.accessories.dungeon.WitherRelic;
import net.swofty.types.generic.item.items.accessories.dungeon.scarf.ScarfsGrimoire;
import net.swofty.types.generic.item.items.accessories.dungeon.scarf.ScarfsStudies;
import net.swofty.types.generic.item.items.accessories.dungeon.scarf.ScarfsThesis;
import net.swofty.types.generic.item.items.accessories.dungeon.treasure.TreasureArtifact;
import net.swofty.types.generic.item.items.accessories.dungeon.treasure.TreasureRing;
import net.swofty.types.generic.item.items.accessories.dungeon.treasure.TreasureTalisman;
import net.swofty.types.generic.item.items.accessories.speed.SpeedArtifact;
import net.swofty.types.generic.item.items.accessories.speed.SpeedRing;
import net.swofty.types.generic.item.items.accessories.speed.SpeedTalisman;
import net.swofty.types.generic.item.items.accessories.spider.SpiderArtifact;
import net.swofty.types.generic.item.items.accessories.spider.SpiderRing;
import net.swofty.types.generic.item.items.accessories.spider.SpiderTalisman;
import net.swofty.types.generic.item.items.accessories.zombie.ZombieArtifact;
import net.swofty.types.generic.item.items.accessories.zombie.ZombieRing;
import net.swofty.types.generic.item.items.accessories.zombie.ZombieTalisman;
import net.swofty.types.generic.item.items.armor.celeste.CelesteBoots;
import net.swofty.types.generic.item.items.armor.celeste.CelesteChestplate;
import net.swofty.types.generic.item.items.armor.celeste.CelesteHelmet;
import net.swofty.types.generic.item.items.armor.celeste.CelesteLeggings;
import net.swofty.types.generic.item.items.armor.cheaptuxedo.CheapTuxedoBoots;
import net.swofty.types.generic.item.items.armor.cheaptuxedo.CheapTuxedoChestplate;
import net.swofty.types.generic.item.items.armor.cheaptuxedo.CheapTuxedoLeggings;
import net.swofty.types.generic.item.items.armor.eleganttuxedo.ElegantTuxedoBoots;
import net.swofty.types.generic.item.items.armor.eleganttuxedo.ElegantTuxedoChestplate;
import net.swofty.types.generic.item.items.armor.eleganttuxedo.ElegantTuxedoLeggings;
import net.swofty.types.generic.item.items.armor.fancytuxedo.FancyTuxedoBoots;
import net.swofty.types.generic.item.items.armor.fancytuxedo.FancyTuxedoChestplate;
import net.swofty.types.generic.item.items.armor.fancytuxedo.FancyTuxedoLeggings;
import net.swofty.types.generic.item.items.armor.farmsuit.FarmSuitBoots;
import net.swofty.types.generic.item.items.armor.farmsuit.FarmSuitChestplate;
import net.swofty.types.generic.item.items.armor.farmsuit.FarmSuitHelmet;
import net.swofty.types.generic.item.items.armor.farmsuit.FarmSuitLeggings;
import net.swofty.types.generic.item.items.armor.leaflet.LeafletBoots;
import net.swofty.types.generic.item.items.armor.leaflet.LeafletChestplate;
import net.swofty.types.generic.item.items.armor.leaflet.LeafletHelmet;
import net.swofty.types.generic.item.items.armor.leaflet.LeafletLeggings;
import net.swofty.types.generic.item.items.armor.mercenary.MercenaryBoots;
import net.swofty.types.generic.item.items.armor.mercenary.MercenaryChestplate;
import net.swofty.types.generic.item.items.armor.mercenary.MercenaryHelmet;
import net.swofty.types.generic.item.items.armor.mercenary.MercenaryLeggings;
import net.swofty.types.generic.item.items.armor.mineroutfit.MinerOutfitBoots;
import net.swofty.types.generic.item.items.armor.mineroutfit.MinerOutfitChestplate;
import net.swofty.types.generic.item.items.armor.mineroutfit.MinerOutfitHelmet;
import net.swofty.types.generic.item.items.armor.mineroutfit.MinerOutfitLeggings;
import net.swofty.types.generic.item.items.armor.mushroom.MushroomBoots;
import net.swofty.types.generic.item.items.armor.mushroom.MushroomChestplate;
import net.swofty.types.generic.item.items.armor.mushroom.MushroomHelmet;
import net.swofty.types.generic.item.items.armor.mushroom.MushroomLeggings;
import net.swofty.types.generic.item.items.armor.pumpkin.PumpkinBoots;
import net.swofty.types.generic.item.items.armor.pumpkin.PumpkinChestplate;
import net.swofty.types.generic.item.items.armor.pumpkin.PumpkinHelmet;
import net.swofty.types.generic.item.items.armor.pumpkin.PumpkinLeggings;
import net.swofty.types.generic.item.items.armor.rosetta.RosettaBoots;
import net.swofty.types.generic.item.items.armor.rosetta.RosettaChestplate;
import net.swofty.types.generic.item.items.armor.rosetta.RosettaHelmet;
import net.swofty.types.generic.item.items.armor.rosetta.RosettaLeggings;
import net.swofty.types.generic.item.items.armor.squire.SquireBoots;
import net.swofty.types.generic.item.items.armor.squire.SquireChestplate;
import net.swofty.types.generic.item.items.armor.squire.SquireHelmet;
import net.swofty.types.generic.item.items.armor.squire.SquireLeggings;
import net.swofty.types.generic.item.items.armor.starlight.StarlightBoots;
import net.swofty.types.generic.item.items.armor.starlight.StarlightChestplate;
import net.swofty.types.generic.item.items.armor.starlight.StarlightHelmet;
import net.swofty.types.generic.item.items.armor.starlight.StarlightLeggings;
import net.swofty.types.generic.item.items.backpacks.*;
import net.swofty.types.generic.item.items.brewing.*;
import net.swofty.types.generic.item.items.combat.*;
import net.swofty.types.generic.item.items.combat.mythological.craftable.DaedalusAxe;
import net.swofty.types.generic.item.items.combat.mythological.craftable.SwordOfRevelations;
import net.swofty.types.generic.item.items.combat.mythological.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.blaze.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.enderman.craftable.Terminator;
import net.swofty.types.generic.item.items.combat.slayer.enderman.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.enderman.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.spider.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.spider.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.wolf.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.wolf.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.zombie.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.zombie.drops.*;
import net.swofty.types.generic.item.items.combat.vanilla.*;
import net.swofty.types.generic.item.items.communitycenter.*;
import net.swofty.types.generic.item.items.communitycenter.enrichments.*;
import net.swofty.types.generic.item.items.communitycenter.katitems.KatBouquet;
import net.swofty.types.generic.item.items.communitycenter.katitems.KatFlower;
import net.swofty.types.generic.item.items.communitycenter.sacks.*;
import net.swofty.types.generic.item.items.communitycenter.stackingenchants.*;
import net.swofty.types.generic.item.items.communitycenter.upgradecomponents.*;
import net.swofty.types.generic.item.items.crimson.*;
import net.swofty.types.generic.item.items.dungeon.misc.AncientRose;
import net.swofty.types.generic.item.items.dungeon.misc.ArchitectsFirstDraft;
import net.swofty.types.generic.item.items.dungeon.misc.KismetFeather;
import net.swofty.types.generic.item.items.dungeon.misc.NecronHandle;
import net.swofty.types.generic.item.items.enchanted.*;
import net.swofty.types.generic.item.items.enchantment.EnchantedBook;
import net.swofty.types.generic.item.items.enchantment.HotPotatoBook;
import net.swofty.types.generic.item.items.farming.*;
import net.swofty.types.generic.item.items.farming.vanilla.*;
import net.swofty.types.generic.item.items.fishing.festival.*;
import net.swofty.types.generic.item.items.fishing.vanilla.*;
import net.swofty.types.generic.item.items.foraging.EfficientAxe;
import net.swofty.types.generic.item.items.foraging.PromisingAxe;
import net.swofty.types.generic.item.items.foraging.RookieAxe;
import net.swofty.types.generic.item.items.foraging.SweetAxe;
import net.swofty.types.generic.item.items.foraging.vanilla.*;
import net.swofty.types.generic.item.items.jerrysworkshop.GlacialFragment;
import net.swofty.types.generic.item.items.jerrysworkshop.GreenGift;
import net.swofty.types.generic.item.items.jerrysworkshop.RedGift;
import net.swofty.types.generic.item.items.jerrysworkshop.WhiteGift;
import net.swofty.types.generic.item.items.mining.PioneersPickaxe;
import net.swofty.types.generic.item.items.mining.RookiePickaxe;
import net.swofty.types.generic.item.items.mining.crystal.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.fine.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.rough.*;
import net.swofty.types.generic.item.items.mining.dwarven.*;
import net.swofty.types.generic.item.items.mining.vanilla.*;
import net.swofty.types.generic.item.items.minion.combat.*;
import net.swofty.types.generic.item.items.minion.farming.*;
import net.swofty.types.generic.item.items.minion.fishing.FishingMinion;
import net.swofty.types.generic.item.items.minion.foraging.*;
import net.swofty.types.generic.item.items.minion.mining.*;
import net.swofty.types.generic.item.items.minion.upgrade.MithrilInfusion;
import net.swofty.types.generic.item.items.minion.upgrade.fuel.EnchantedLavaBucket;
import net.swofty.types.generic.item.items.minion.upgrade.fuel.EverburningFlame;
import net.swofty.types.generic.item.items.minion.upgrade.fuel.MagmaBucket;
import net.swofty.types.generic.item.items.minion.upgrade.fuel.PlasmaBucket;
import net.swofty.types.generic.item.items.minion.upgrade.shipping.BudgetHopper;
import net.swofty.types.generic.item.items.minion.upgrade.shipping.EnchantedHopper;
import net.swofty.types.generic.item.items.minion.upgrade.skin.BeeMinionSkin;
import net.swofty.types.generic.item.items.minion.upgrade.upgrade.*;
import net.swofty.types.generic.item.items.miscellaneous.MoveJerry;
import net.swofty.types.generic.item.items.miscellaneous.SkyBlockMenu;
import net.swofty.types.generic.item.items.miscellaneous.decorations.Cactus;
import net.swofty.types.generic.item.items.miscellaneous.decorations.Melon;
import net.swofty.types.generic.item.items.miscellaneous.decorations.*;
import net.swofty.types.generic.item.items.miscellaneous.gifts.DeadBushofLove;
import net.swofty.types.generic.item.items.miscellaneous.gifts.GameAnnihilator;
import net.swofty.types.generic.item.items.miscellaneous.gifts.GameBreaker;
import net.swofty.types.generic.item.items.miscellaneous.gifts.QualityMap;
import net.swofty.types.generic.item.items.pet.petitems.*;
import net.swofty.types.generic.item.items.pet.pets.BeePet;
import net.swofty.types.generic.item.items.powerstones.LuxuriousSpool;
import net.swofty.types.generic.item.items.runes.BloodRune;
import net.swofty.types.generic.item.items.spooky.*;
import net.swofty.types.generic.item.items.travelscroll.HubCastleTravelScroll;
import net.swofty.types.generic.item.items.travelscroll.HubCryptsTravelScroll;
import net.swofty.types.generic.item.items.travelscroll.HubMuseumTravelScroll;
import net.swofty.types.generic.item.items.vanilla.blocks.Anvil;
import net.swofty.types.generic.item.items.vanilla.blocks.colored.carpet.*;
import net.swofty.types.generic.item.items.vanilla.blocks.colored.glass.*;
import net.swofty.types.generic.item.items.vanilla.blocks.colored.glasspane.*;
import net.swofty.types.generic.item.items.vanilla.blocks.colored.wool.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.andesite.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.cobblestone.Cobblestone;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.cobblestone.CobblestoneSlab;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.cobblestone.CobblestoneStairs;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.cobblestone.CobblestoneWall;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.diorite.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.granite.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.netherbricks.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.quartz.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.sandstone.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.stone.*;
import net.swofty.types.generic.item.items.vanilla.blocks.stone.stonebricks.*;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.Chest;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.CraftingTable;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.Ladder;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.Stick;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.acacia.*;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.birch.*;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.darkoak.*;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.jungle.*;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.oak.*;
import net.swofty.types.generic.item.items.vanilla.blocks.wood.spruce.*;
import net.swofty.types.generic.item.items.vanilla.items.FishingRod;
import net.swofty.types.generic.item.items.vanilla.items.armor.*;
import net.swofty.types.generic.item.items.vanilla.items.bow.Bow;
import net.swofty.types.generic.item.items.vanilla.items.dyes.*;
import net.swofty.types.generic.item.items.vanilla.items.weapon.*;
import net.swofty.types.generic.item.items.weapon.*;
import org.jetbrains.annotations.Nullable;

@Getter
public enum ItemTypeLinker {
    SANDBOX_ITEM(ItemType.SANDBOX_ITEM, SandboxItem.class),

    /**
     * Miscellaneous
     */
    ENCHANTED_BOOK(ItemType.ENCHANTED_BOOK, EnchantedBook.class),
    SKYBLOCK_MENU(ItemType.SKYBLOCK_MENU, SkyBlockMenu.class),
    MOVE_JERRY(ItemType.MOVE_JERRY, MoveJerry.class),
    HOT_POTATO_BOOK(ItemType.HOT_POTATO_BOOK, HotPotatoBook.class),
    GAME_BREAKER(ItemType.GAME_BREAKER, GameBreaker.class),
    GAME_ANNIHILATOR(ItemType.GAME_ANNIHILATOR, GameAnnihilator.class),
    QUALITY_MAP(ItemType.QUALITY_MAP, QualityMap.class),
    DEAD_BUSH_OF_LOVE(ItemType.DEAD_BUSH_OF_LOVE, DeadBushofLove.class),

    /**
     * Accessories
     */
    ZOMBIE_TALISMAN(ItemType.ZOMBIE_TALISMAN, ZombieTalisman.class),
    ZOMBIE_RING(ItemType.ZOMBIE_RING, ZombieRing.class),
    ZOMBIE_ARTIFACT(ItemType.ZOMBIE_ARTIFACT, ZombieArtifact.class),
    SPEED_TALISMAN(ItemType.SPEED_TALISMAN, SpeedTalisman.class),
    SPEED_RING(ItemType.SPEED_RING, SpeedRing.class),
    SPEED_ARTIFACT(ItemType.SPEED_ARTIFACT, SpeedArtifact.class),
    SKELETON_TALISMAN(ItemType.SKELETON_TALISMAN, SkeletonTalisman.class),
    HASTE_RING(ItemType.HASTE_RING, HasteRing.class),
    FARMING_TALISMAN(ItemType.FARMING_TALISMAN, FarmingTalisman.class),
    LAVA_TALISMAN(ItemType.LAVA_TALISMAN, LavaTalisman.class),
    POTATO_TALISMAN(ItemType.POTATO_TALISMAN, PotatoTalisman.class),
    TALISMAN_OF_POWER(ItemType.TALISMAN_OF_POWER, TalismanOfPower.class),
    BAT_TALISMAN(ItemType.BAT_TALISMAN, BatTalisman.class),
    FIRE_TALISMAN(ItemType.FIRE_TALISMAN, FireTalisman.class),
    AUTO_RECOMBOBULATOR(ItemType.AUTO_RECOMBOBULATOR, AutoRecombobulator.class),
    CATACOMBS_EXPERT_RING(ItemType.CATACOMBS_EXPERT_RING, CatacombsExpertRing.class),
    WITHER_RELIC(ItemType.WITHER_RELIC, WitherRelic.class),
    SCARFS_GRIMOIRE(ItemType.SCARFS_GRIMOIRE, ScarfsGrimoire.class),
    SCARFS_THESIS(ItemType.SCARFS_THESIS, ScarfsThesis.class),
    SCARFS_STUDIES(ItemType.SCARFS_STUDIES, ScarfsStudies.class),
    TREASURE_ARTIFACT(ItemType.TREASURE_ARTIFACT, TreasureArtifact.class),
    TREASURE_RING(ItemType.TREASURE_RING, TreasureRing.class),
    TREASURE_TALISMAN(ItemType.TREASURE_TALISMAN, TreasureTalisman.class),
    SPIDER_TALISMAN(ItemType.SPIDER_TALISMAN, SpiderTalisman.class),
    SPIDER_RING(ItemType.SPIDER_RING, SpiderRing.class),
    SPIDER_ARTIFACT(ItemType.SPIDER_ARTIFACT, SpiderArtifact.class),
    MINE_AFFINITY_TALISMAN(ItemType.MINE_AFFINITY_TALISMAN, MineAffinityTalisman.class),
    VILLAGE_AFFINITY_TALISMAN(ItemType.VILLAGE_AFFINITY_TALISMAN, VillageAffinityTalisman.class),
    BITS_TALISMAN(ItemType.BITS_TALISMAN, BitsTalisman.class),
    ACTUALLY_BLUE_ABICASE(ItemType.ACTUALLY_BLUE_ABICASE, ActuallyBlueAbicase.class),
    BLUE_BUT_GREEN_ABICASE(ItemType.BLUE_BUT_GREEN_ABICASE, BlueButGreenAbicase.class),
    BLUE_BUT_RED_ABICASE(ItemType.BLUE_BUT_RED_ABICASE, BlueButRedAbicase.class),
    BLUE_BUT_YELLOW_ABICASE(ItemType.BLUE_BUT_YELLOW_ABICASE, BlueButYellowAbicase.class),
    LIGHTER_BLUE_ABICASE(ItemType.LIGHTER_BLUE_ABICASE, LighterBlueAbicase.class),
    REZAR_ABICASE(ItemType.REZAR_ABICASE, RezarAbicase.class),
    SUMSUNG_G3_ABICASE(ItemType.SUMSUNG_G3_ABICASE, SumsungG3Abicase.class),
    SUMSUNG_GG_ABICASE(ItemType.SUMSUNG_GG_ABICASE, SumsungGGAbicase.class),

    /**
     * Community Shop Items
     */
    BOOSTER_COOKIE(ItemType.BOOSTER_COOKIE, BoosterCookie.class),
    GOD_POTION(ItemType.GOD_POTION, GodPotion.class),
    KAT_FLOWER(ItemType.KAT_FLOWER, KatFlower.class),
    KAT_BOUQUET(ItemType.KAT_BOUQUET, KatBouquet.class),
    HYPER_CATALYST_UPGRADER(ItemType.HYPER_CATALYST_UPGRADER, HyperCatalystUpgrader.class),
    ULTIMATE_CARROT_CANDY_UPGRADE(ItemType.ULTIMATE_CARROT_CANDY_UPGRADE, UltimateCarrotCandyUpgrade.class),
    COLOSSAL_EXPERIENCE_BOTTLE_UPGRADE(ItemType.COLOSSAL_EXPERIENCE_BOTTLE_UPGRADE, ColossalExperienceBottleUpgrade.class),
    MINION_STORAGE_EXPANDER(ItemType.MINION_STORAGE_EXPANDER, MinionStorageExpander.class),
    MATRIARCHS_PERFUME(ItemType.MATRIARCHS_PERFUME, MatriarchsPerfume.class),
    HOLOGRAM(ItemType.HOLOGRAM, Hologram.class),
    DITTO_BLOB(ItemType.DITTO_BLOB, DittoBlob.class),
    BUILDERS_WAND(ItemType.BUILDERS_WAND, BuildersWand.class),
    BLOCK_ZAPPER(ItemType.BLOCK_ZAPPER, BlockZapper.class),
    PORTALIZER(ItemType.PORTALIZER, Portalizer.class),
    AUTOPET_RULES_2_PACK(ItemType.AUTOPET_RULES_2_PACK, AutopetRules2Pack.class),
    POCKET_SACK_IN_A_SACK(ItemType.POCKET_SACK_IN_A_SACK, PocketSackInASack.class),
    DUNGEON_SACK(ItemType.DUNGEON_SACK, DungeonSack.class),
    RUNE_SACK(ItemType.RUNE_SACK, RuneSack.class),
    FLOWER_SACK(ItemType.FLOWER_SACK, FlowerSack.class),
    DWARVEN_SACK(ItemType.DWARVEN_SACK, DwarvenSack.class),
    CRYSTAL_HOLLOWS_SACK(ItemType.CRYSTAL_HOLLOWS_SACK, CrystalHollowsSack.class),
    ABIPHONE_CONTACTS_TRIO(ItemType.ABIPHONE_CONTACTS_TRIO, AbiphoneContactsTrio.class),
    PURE_WHITE_DYE(ItemType.PURE_WHITE_DYE, PureWhiteDye.class),
    PURE_BLACK_DYE(ItemType.PURE_BLACK_DYE, PureBlackDye.class),
    ACCESSORY_ENRICHMENT_SWAPPER(ItemType.ACCESSORY_ENRICHMENT_SWAPPER, AccessoryEnrichmentSwapper.class),
    ATTACK_SPEED_ENRICHMENT(ItemType.ATTACK_SPEED_ENRICHMENT, AttackSpeedEnrichment.class),
    CRITICAL_CHANCE_ENRICHMENT(ItemType.CRITICAL_CHANCE_ENRICHMENT, CriticalChanceEnrichment.class),
    CRITICAL_DAMAGE_ENRICHMENT(ItemType.CRITICAL_DAMAGE_ENRICHMENT, CriticalDamageEnrichment.class),
    DEFENSE_ENRICHMENT(ItemType.DEFENSE_ENRICHMENT, DefenseEnrichment.class),
    FEROCITY_ENRICHMENT(ItemType.FEROCITY_ENRICHMENT, FerocityEnrichment.class),
    HEALTH_ENRICHMENT(ItemType.HEALTH_ENRICHMENT, HealthEnrichment.class),
    INTELLIGENCE_ENRICHMENT(ItemType.INTELLIGENCE_ENRICHMENT, IntelligenceEnrichment.class),
    MAGIC_FIND_ENRICHMENT(ItemType.MAGIC_FIND_ENRICHMENT, MagicFindEnrichment.class),
    SEA_CREATURE_CHANCE_ENRICHMENT(ItemType.SEA_CREATURE_CHANCE_ENRICHMENT, SeaCreatureChanceEnrichment.class),
    SPEED_ENRICHMENT(ItemType.SPEED_ENRICHMENT, SpeedEnrichment.class),
    STRENGTH_ENRICHMENT(ItemType.STRENGTH_ENRICHMENT, StrengthEnrichment.class),
    CHAMPION(ItemType.CHAMPION, Champion.class),
    COMPACT(ItemType.COMPACT, Compact.class),
    CULTIVATING(ItemType.CULTIVATING, Cultivating.class),
    EXPERTISE(ItemType.EXPERTISE, Expertise.class),
    HECATOMB(ItemType.HECATOMB, Hecatomb.class),
    INFERNO_FUE_BLOCK(ItemType.INFERNO_FUE_BLOCK, InfernoFuelBlock.class),

    /**
     * Runes
     */
    BLOOD_RUNE(ItemType.BLOOD_RUNE, BloodRune.class),

    /**
     * Minions
     */
    COBBLESTONE_MINION(ItemType.COBBLESTONE_MINION, CobblestoneMinion.class),
    COAL_MINION(ItemType.COAL_MINION, CoalMinion.class),
    SNOW_MINION(ItemType.SNOW_MINION, SnowMinion.class),
    DIAMOND_MINION(ItemType.DIAMOND_MINION, DiamondMinion.class),
    EMERALD_MINION(ItemType.EMERALD_MINION, EmeraldMinion.class),
    IRON_MINION(ItemType.IRON_MINION, IronMinion.class),
    GOLD_MINION(ItemType.GOLD_MINION, GoldMinion.class),
    LAPIS_MINION(ItemType.LAPIS_MINION, LapisMinion.class),
    REDSTONE_MINION(ItemType.REDSTONE_MINION, RedstoneMinion.class),
    ENDSTONE_MINION(ItemType.ENDSTONE_MINION, EndstoneMinion.class),
    ICE_MINION(ItemType.ICE_MINION, IceMinion.class),
    QUARTZ_MINION(ItemType.QUARTZ_MINION, QuartzMinion.class),
    OBSIDIAN_MINION(ItemType.OBSIDIAN_MINION, ObsidianMinion.class),
    SAND_MINION(ItemType.SAND_MINION, SandMinion.class),
    GRAVEL_MINION(ItemType.GRAVEL_MINION, GravelMinion.class),
    GLOWSTONE_MINION(ItemType.GLOWSTONE_MINION, GlowstoneMinion.class),
    HARD_STONE_MINION(ItemType.HARD_STONE_MINION, HardStoneMinion.class),
    MITHRIL_MINION(ItemType.MITHRIL_MINION, MithrilMinion.class),

    ACACIA_MINION(ItemType.ACACIA_MINION, AcaciaMinion.class),
    BIRCH_MINION(ItemType.BIRCH_MINION, BirchMinion.class),
    DARK_OAK_MINION(ItemType.DARK_OAK_MINION, DarkOakMinion.class),
    JUNGLE_MINION(ItemType.JUNGLE_MINION, JungleMinion.class),
    OAK_MINION(ItemType.OAK_MINION, OakMinion.class),
    SPRUCE_MINION(ItemType.SPRUCE_MINION, SpruceMinion.class),

    FISHING_MINION(ItemType.FISHING_MINION, FishingMinion.class),

    WHEAT_MINION(ItemType.WHEAT_MINION, WheatMinion.class),
    POTATO_MINION(ItemType.POTATO_MINION, PotatoMinion.class),
    CACTUS_MINION(ItemType.CACTUS_MINION, CactusMinion.class),
    CARROT_MINION(ItemType.CARROT_MINION, CarrotMinion.class),
    COCOA_BEANS_MINION(ItemType.COCOA_BEANS_MINION, CocoaBeansMinion.class),
    MELON_MINION(ItemType.MELON_MINION, MelonMinion.class),
    MUSHROOM_MINION(ItemType.MUSHROOM_MINION, MushroomMinion.class),
    NETHER_WART_MINION(ItemType.NETHER_WART_MINION, NetherWartMinion.class),
    PUMPKIN_MINION(ItemType.PUMPKIN_MINION, PumpkinMinion.class),
    SUGAR_CANE_MINION(ItemType.SUGAR_CANE_MINION, SugarCaneMinion.class),
    CHICKEN_MINION(ItemType.CHICKEN_MINION, ChickenMinion.class),
    COW_MINION(ItemType.COW_MINION, CowMinion.class),
    PIG_MINION(ItemType.PIG_MINION, PigMinion.class),
    RABBIT_MINION(ItemType.RABBIT_MINION, RabbitMinion.class),
    SHEEP_MINION(ItemType.SHEEP_MINION, SheepMinion.class),

    BLAZE_MINION(ItemType.BLAZE_MINION, BlazeMinion.class),
    CAVE_SPIDER_MINION(ItemType.CAVE_SPIDER_MINION, CaveSpiderMinion.class),
    CREEPER_MINION(ItemType.CREEPER_MINION, CreeperMinion.class),
    ENDERMAN_MINION(ItemType.ENDERMAN_MINION, EndermanMinion.class),
    GHAST_MINION(ItemType.GHAST_MINION, GhastMinion.class),
    MAGMA_CUBE_MINION(ItemType.MAGMA_CUBE_MINION, MagmaCubeMinion.class),
    SKELETON_MINION(ItemType.SKELETON_MINION, SkeletonMinion.class),
    SLIME_MINION(ItemType.SLIME_MINION, SlimeMinion.class),
    SPIDER_MINION(ItemType.SPIDER_MINION, SpiderMinion.class),
    ZOMBIE_MINION(ItemType.ZOMBIE_MINION, ZombieMinion.class),

    /**
     * Minion Upgrades
     */
    AUTO_SMELTER(ItemType.AUTO_SMELTER, AutoSmelter.class),
    COMPACTOR(ItemType.COMPACTOR, Compactor.class),
    SUPER_COMPACTOR_3000(ItemType.SUPER_COMPACTOR_3000, SuperCompactor3000.class),
    BUDGET_HOPPER(ItemType.BUDGET_HOPPER, BudgetHopper.class),
    ENCHANTED_HOPPER(ItemType.ENCHANTED_HOPPER, EnchantedHopper.class),
    FLY_CATCHER(ItemType.FLY_CATCHER, FlyCatcher.class),
    DIAMOND_SPREADING(ItemType.DIAMOND_SPREADING, DiamondSpreading.class),
    MINION_EXPANDER(ItemType.MINION_EXPANDER, MinionExpander.class),
    MITHRIL_INFUSION(ItemType.MITHRIL_INFUSION, MithrilInfusion.class),

    /**
     * Minion Fuels
     */
    ENCHANTED_LAVA_BUCKET(ItemType.ENCHANTED_LAVA_BUCKET, EnchantedLavaBucket.class),
    MAGMA_BUCKET(ItemType.MAGMA_BUCKET, MagmaBucket.class),
    PLASMA_BUCKET(ItemType.PLASMA_BUCKET, PlasmaBucket.class),
    EVERBURNING_FLAME(ItemType.EVERBURNING_FLAME, EverburningFlame.class),
    HEAT_CORE(ItemType.HEAT_CORE, HeatCore.class),

    /**
     * Minion Skins
     */
    BEE_MINION_SKIN(ItemType.BEE_MINION_SKIN, BeeMinionSkin.class),

    /**
     * Pets
     */
    BEE_PET(ItemType.BEE_PET, BeePet.class),

    /**
     * Pet Items
     */
    ALL_SKILLS_EXP_BOOST(ItemType.ALL_SKILLS_EXP_BOOST, AllSkillsExpBoost.class),
    MINING_EXP_BOOST_COMMON(ItemType.MINING_EXP_BOOST_COMMON, MiningExpBoostCommon.class),
    MINING_EXP_BOOST_RARE(ItemType.MINING_EXP_BOOST_RARE, MiningExpBoostRare.class),
    FARMING_EXP_BOOST_COMMON(ItemType.FARMING_EXP_BOOST_COMMON, FarmingExpBoostCommon.class),
    FARMING_EXP_BOOST_RARE(ItemType.FARMING_EXP_BOOST_RARE, FarmingExpBoostRare.class),
    FISHING_EXP_BOOST(ItemType.FISHING_EXP_BOOST, FishingExpBoost.class),
    FORAGING_EXP_BOOST(ItemType.FORAGING_EXP_BOOST, ForagingExpBoost.class),
    COMBAT_EXP_BOOST(ItemType.COMBAT_EXP_BOOST, CombatExpBoost.class),
    BIG_TEETH(ItemType.BIG_TEETH, BigTeeth.class),
    IRON_CLAWS(ItemType.IRON_CLAWS, IronClaws.class),
    HARDENED_SCALES(ItemType.HARDENED_SCALES, HardenedScales.class),
    SHARPENED_CLAWS(ItemType.SHARPENED_CLAWS, SharpenedClaws.class),
    BUBBLEGUM(ItemType.BUBBLEGUM, Bubblegum.class),

    /**
     * Backpacks
     */
    SMALL_BACKPACK(ItemType.SMALL_BACKPACK, SmallBackpack.class),
    MEDIUM_BACKPACK(ItemType.MEDIUM_BACKPACK, MediumBackpack.class),
    LARGE_BACKPACK(ItemType.LARGE_BACKPACK, LargeBackpack.class),
    GREATER_BACKPACK(ItemType.GREATER_BACKPACK, GreaterBackpack.class),
    JUMBO_BACKPACK(ItemType.JUMBO_BACKPACK, JumboBackpack.class),
    JUMBO_BACKPACK_UPGRADE(ItemType.JUMBO_BACKPACK_UPGRADE, JumboBackpackUpgrade.class),

    /**
     * Decoration items
     */
    DECORATION_ANCIENT_FRUIT(ItemType.DECORATION_ANCIENT_FRUIT, AncientFruit.class),
    DECORATION_APPALLED_PUMPKIN(ItemType.DECORATION_APPALLED_PUMPKIN, AppalledPumpkin.class),
    DECORATION_APPLE(ItemType.DECORATION_APPLE, Apple.class),
    DECORATION_BANANA_BUNCH(ItemType.DECORATION_BANANA_BUNCH, BananaBunch.class),
    DECORATION_BEETROOT(ItemType.DECORATION_BEETROOT, Beetroot.class),
    DECORATION_BERRY(ItemType.DECORATION_BERRY, Berry.class),
    DECORATION_BERRY_BUSH(ItemType.DECORATION_BERRY_BUSH, BerryBush.class),
    DECORATION_BLUE_CORN(ItemType.DECORATION_BLUE_CORN, BlueCorn.class),
    DECORATION_BROWN_MUSHROOM(ItemType.DECORATION_BROWN_MUSHROOM, BrownMushroom.class),
    DECORATION_BUSH(ItemType.DECORATION_BUSH, Bush.class),
    DECORATION_CACTUS(ItemType.DECORATION_CACTUS, Cactus.class),
    DECORATION_CHESTO_BERRY(ItemType.DECORATION_CHESTO_BERRY, ChestoBerry.class),
    DECORATION_CORN(ItemType.DECORATION_CORN, Corn.class),
    DECORATION_KIWI(ItemType.DECORATION_KIWI, Kiwi.class),
    DECORATION_LEMON(ItemType.DECORATION_LEMON, Lemon.class),
    DECORATION_LETTUCE(ItemType.DECORATION_LETTUCE, Lettuce.class),
    DECORATION_LILAC_FRUIT(ItemType.DECORATION_LILAC_FRUIT, LilacFruit.class),
    DECORATION_MELON(ItemType.DECORATION_MELON, Melon.class),
    DECORATION_ONION(ItemType.DECORATION_ONION, Onion.class),
    DECORATION_ORANGE(ItemType.DECORATION_ORANGE, Orange.class),
    DECORATION_PINK_BERRY(ItemType.DECORATION_PINK_BERRY, PinkBerry.class),

    /**
     * Power Stones
     */
    LUXURIOUS_SPOOL(ItemType.LUXURIOUS_SPOOL, LuxuriousSpool.class),

    /**
     * Farming Props
     */
    ROOKIE_HOE(ItemType.ROOKIE_HOE, RookieHoe.class),
    WOODEN_HOE(ItemType.WOODEN_HOE, WoodenHoe.class),
    STONE_HOE(ItemType.STONE_HOE, StoneHoe.class),
    GOLDEN_HOE(ItemType.GOLDEN_HOE, GoldenHoe.class),
    IRON_HOE(ItemType.IRON_HOE, IronHoe.class),
    DIAMOND_HOE(ItemType.DIAMOND_HOE, DiamondHoe.class),
    WHEAT(ItemType.WHEAT, Wheat.class),
    WHEAT_CRYSTAL(ItemType.WHEAT_CRYSTAL, WheatCrystal.class),
    FLOWER_CRYSTAL(ItemType.FLOWER_CRYSTAL, FlowerCrystal.class),
    CARROT_CRYSTAL(ItemType.CARROT_CRYSTAL, CarrotCrystal.class),
    POTATO_CRYSTAL(ItemType.POTATO_CRYSTAL, PotatoCrystal.class),
    PUMPKIN_AND_MELON_CRYSTAL(ItemType.PUMPKIN_AND_MELON_CRYSTAL, PumpkinAndMelonCrystal.class),
    MUTANT_NETHER_WART(ItemType.MUTANT_NETHER_WART, MutantNetherWart.class),
    POLISHED_PUMPKIN(ItemType.POLISHED_PUMPKIN, PolishedPumpkin.class),
    BOX_OF_SEEDS(ItemType.BOX_OF_SEEDS, BoxOfSeeds.class),
    ENCHANTED_BOOKSHELF(ItemType.ENCHANTED_BOOKSHELF, EnchantedBookshelf.class),
    TIGHTLY_TIED_HAY_BALE(ItemType.TIGHTLY_TIED_HAY_BALE, TightlyTiedHayBale.class),
    COMPOST(ItemType.COMPOST, Compost.class),
    COMPOST_BUNDLE(ItemType.COMPOST_BUNDLE, CompostBundle.class),
    DUNG(ItemType.DUNG, Dung.class),
    HONEY_JAR(ItemType.HONEY_JAR, HoneyJar.class),
    PLANT_MATTER(ItemType.PLANT_MATTER, PlantMatter.class),

    /**
     * Foraging Props
     */
    WOODEN_AXE(ItemType.WOODEN_AXE, WoodenAxe.class),
    STONE_AXE(ItemType.STONE_AXE, StoneAxe.class),
    GOLDEN_AXE(ItemType.GOLDEN_AXE, GoldenAxe.class),
    IRON_AXE(ItemType.IRON_AXE, IronAxe.class),
    DIAMOND_AXE(ItemType.DIAMOND_AXE, DiamondAxe.class),
    ROOKIE_AXE(ItemType.ROOKIE_AXE, RookieAxe.class),
    PROMISING_AXE(ItemType.PROMISING_AXE, PromisingAxe.class),
    SWEET_AXE(ItemType.SWEET_AXE, SweetAxe.class),
    EFFICIENT_AXE(ItemType.EFFICIENT_AXE, EfficientAxe.class),

    /**
     * Fishing Props
     */
    SHARK_FIN(ItemType.SHARK_FIN, SharkFin.class),
    NURSE_SHARK_TOOTH(ItemType.NURSE_SHARK_TOOTH, NurseSharkTooth.class),
    BLUE_SHARK_TOOTH(ItemType.BLUE_SHARK_TOOTH, BlueSharkTooth.class),
    TIGER_SHARK_TOOTH(ItemType.TIGER_SHARK_TOOTH, TigerSharkTooth.class),
    GREAT_WHITE_SHARK_TOOTH(ItemType.GREAT_WHITE_SHARK_TOOTH, GreatWhiteSharkTooth.class),
    MAGMAFISH(ItemType.MAGMAFISH, Magmafish.class),
    SILVER_MAGMAFISH(ItemType.SILVER_MAGMAFISH, SilverMagmafish.class),
    GOLD_MAGMAFISH(ItemType.GOLD_MAGMAFISH, GoldMagmafish.class),
    DIAMOND_MAGMAFISH(ItemType.DIAMOND_MAGMAFISH, DiamondMagmafish.class),
    FISHING_ROD(ItemType.FISHING_ROD, FishingRod.class),

    /**
     * Combat Props
     */
    CHILI_PEPPER(ItemType.CHILI_PEPPER, ChiliPepper.class),
    STUFFED_CHILI_PEPPER(ItemType.STUFFED_CHILI_PEPPER, StuffedChiliPepper.class),
    ABSOLUTE_ENDER_PEARL(ItemType.ABSOLUTE_ENDER_PEARL, AbsoluteEnderPearl.class),
    WHIPPED_MAGMA_CREAM(ItemType.WHIPPED_MAGMA_CREAM, WhippedMagmaCream.class),
    ZOMBIE_HEART(ItemType.ZOMBIE_HEART, ZombieHeart.class),
    SOUL_STRING(ItemType.SOUL_STRING, SoulString.class),

    /**
     * Zombie Slayer
     */
    FOUL_FLESH(ItemType.FOUL_FLESH, FoulFlesh.class),
    REVENANT_FLESH(ItemType.REVENANT_FLESH, RevenantFlesh.class),
    UNDEAD_CATALYST(ItemType.UNDEAD_CATALYST, UndeadCatalyst.class),
    BEHEADED_HORROR(ItemType.BEHEADED_HORROR, BeheadedHorror.class),
    REVENANT_CATALYST(ItemType.REVENANT_CATALYST, RevenantCatalyst.class),
    SCYTHE_BLADE(ItemType.SCYTHE_BLADE, ScytheBlade.class),
    SHARD_OF_THE_SHREDDED(ItemType.SHARD_OF_THE_SHREDDED, ShardOfTheShredded.class),
    WARDEN_HEART(ItemType.WARDEN_HEART, WardenHeart.class),
    REVENANT_VISCERA(ItemType.REVENANT_VISCERA, RevenantViscera.class),
    CRYSTALLIZED_HEART(ItemType.CRYSTALLIZED_HEART, CrystallizedHeart.class),
    REVIVED_HEART(ItemType.REVIVED_HEART, RevivedHeart.class),
    REAPER_MASK(ItemType.REAPER_MASK, ReaperMask.class),
    WARDEN_HELMET(ItemType.WARDEN_HELMET, WardenHelmet.class),
    REVENANT_FALCHION(ItemType.REVENANT_FALCHION, RevenantFalchion.class),
    REAPER_FALCHION(ItemType.REAPER_FALCHION, ReaperFalchion.class),
    AXE_OF_THE_SHREDDED(ItemType.AXE_OF_THE_SHREDDED, AxeOfTheShreeded.class),
    REAPER_SCYTHE(ItemType.REAPER_SCYTHE, ReaperScythe.class),

    /**
     * Spider Slayer
     */
    TARANTULA_WEB(ItemType.TARANTULA_WEB, TarantulaWeb.class),
    TOXIC_ARROW_POISON(ItemType.TOXIC_ARROW_POISON, ToxicArrowPoison.class),
    SPIDER_CATALYST(ItemType.SPIDER_CATALYST, SpiderCatalyst.class),
    FLY_SWATTER(ItemType.FLY_SWATTER, FlySwatter.class),
    DIGESTED_MOSQUITO(ItemType.DIGESTED_MOSQUITO, DigestedMosquito.class),
    TARANTULA_SILK(ItemType.TARANTULA_SILK, TarantulaSilk.class),
    RECLUSE_FANG(ItemType.RECLUSE_FANG, RecluseFang.class),
    MOSQUITO_BOW(ItemType.MOSQUITO_BOW, MosquitoBow.class),
    SCORPION_BOW(ItemType.SCORPION_BOW, ScorpionBow.class),
    SCORPION_FOIL(ItemType.SCORPION_FOIL, ScorpionFoil.class),

    /**
     * Wolf Slayer
     */
    WOLF_TOOTH(ItemType.WOLF_TOOTH, WolfTooth.class),
    HAMSTER_WHEEL(ItemType.HAMSTER_WHEEL, HamsterWheel.class),
    RED_CLAW_EGG(ItemType.RED_CLAW_EGG, RedClawEgg.class),
    OVERFLUX_CAPACITOR(ItemType.OVERFLUX_CAPACITOR, OverfluxCapacitor.class),
    GRIZZLY_SALMON(ItemType.GRIZZLY_SALMON, GrizzlySalmon.class),
    GOLDEN_TOOTH(ItemType.GOLDEN_TOOTH, GoldenTooth.class),
    SHAMAN_SWORD(ItemType.SHAMAN_SWORD, ShamanSword.class),
    POOCH_SWORD(ItemType.POOCH_SWORD, PoochSword.class),
    EDIBLE_MACE(ItemType.EDIBLE_MACE, EdibleMace.class),
    WEIRD_TUBA(ItemType.WEIRD_TUBA, WeirdTuba.class),

    /**
     * Enderman Slayer
     */
    NULL_SPHERE(ItemType.NULL_SPHERE, NullSphere.class),
    TWILIGHT_ARROW_POISON(ItemType.TWILIGHT_ARROW_POISON, TwilightArrowPoison.class),
    SUMMONING_EYE(ItemType.SUMMONING_EYE, SummoningEye.class),
    TRANSMISSION_TUNER(ItemType.TRANSMISSION_TUNER, TransmissionTuner.class),
    NULL_ATOM(ItemType.NULL_ATOM, NullAtom.class),
    SINFUL_DICE(ItemType.SINFUL_DICE, SinfulDice.class),
    EXCEEDINGLY_RARE_ENDER_ARTIFACT_UPGRADER(ItemType.EXCEEDINGLY_RARE_ENDER_ARTIFACT_UPGRADER, ExceedinglyRareEnderArtifactUpgrader.class),
    ETHERWARP_MERGER(ItemType.ETHERWARP_MERGER, EtherwarpMerger.class),
    JUDGEMENT_CORE(ItemType.JUDGEMENT_CORE, JudgementCore.class),
    NULL_OVOID(ItemType.NULL_OVOID, NullOvoid.class),
    NULL_EDGE(ItemType.NULL_EDGE, NullEdge.class),
    NULL_BLADE(ItemType.NULL_BLADE, NullBlade.class),
    TESSELLATED_ENDER_PEARL(ItemType.TESSELLATED_ENDER_PEARL, TessellatedEnderPearl.class),
    VOIDWALKER_KATANA(ItemType.VOIDWALKER_KATANA, VoidwalkerKatana.class),
    VOIDEDGE_KATANA(ItemType.VOIDEDGE_KATANA, VoidedgeKatana.class),
    VORPAL_KATANA(ItemType.VORPAL_KATANA, VorpalKatana.class),
    ATOMSPLIT_KATANA(ItemType.ATOMSPLIT_KATANA, AtomsplitKatana.class),
    SINSEEKER_SCYTHE(ItemType.SINSEEKER_SCYTHE, SinseekerScythe.class),
    JUJU_SHORTBOW(ItemType.JUJU_SHORTBOW, JujuShortbow.class),
    TERMINATOR(ItemType.TERMINATOR, Terminator.class),
    BRAIDED_GRIFFIN_FEATHER(ItemType.BRAIDED_GRIFFIN_FEATHER, BraidedGriffinFeather.class),
    GYROKINETIC_WAND(ItemType.GYROKINETIC_WAND, GyrokineticWand.class),
    SOUL_ESOWARD(ItemType.SOUL_ESOWARD, SoulEsoward.class),
    GLOOMLOCK_GRIMOIRE(ItemType.GLOOMLOCK_GRIMOIRE, GloomlockGrimoire.class),
    ETHERWARP_CONDUIT(ItemType.ETHERWARP_CONDUIT, EtherwarpConduit.class),

    /**
     * Blaze Slayer
     */
    DERELICT_ASHE(ItemType.DERELICT_ASHE, DerelictAshe.class),
    BUNDLE_OF_MAGMA_ARROWS(ItemType.BUNDLE_OF_MAGMA_ARROWS, BundleOfMagmaArrows.class),
    MANA_DISINTEGRATOR(ItemType.MANA_DISINTEGRATOR, ManaDisintegrator.class),
    KELVIN_INVERTER(ItemType.KELVIN_INVERTER, KelvinInverter.class),
    BLAZE_ROD_DISTILLATE(ItemType.BLAZE_ROD_DISTILLATE, BlazeRodDistillate.class),
    MAGMA_CREAM_DISTILLATE(ItemType.MAGMA_CREAM_DISTILLATE, MagmaCreamDistillate.class),
    GLOWSTONE_DISTILLATE(ItemType.GLOWSTONE_DISTILLATE, GlowstoneDistillate.class),
    NETHER_WART_DISTILLATE(ItemType.NETHER_WART_DISTILLATE, NetherWartDistillate.class),
    GABAGOOL_DISTILLATE(ItemType.GABAGOOL_DISTILLATE, GabagoolDistillate.class),
    SCORCHED_POWER_CRYSTAL(ItemType.SCORCHED_POWER_CRYSTAL, ScorchedPowerCrystal.class),
    ARCHFIEND_DICE(ItemType.ARCHFIEND_DICE, ArchfiendDice.class),
    HIGH_CLASS_ARCHFIEND_DICE(ItemType.HIGH_CLASS_ARCHFIEND_DICE, HighClassArchfiendDice.class),
    WILSON_ENGINEERING_PLANS(ItemType.WILSON_ENGINEERING_PLANS, WilsonEngineeringPlans.class),
    SUBZERO_INVERTER(ItemType.SUBZERO_INVERTER, SubzeroInverter.class),

    /**
     * BrewingItem
     */

    CHEAP_COFFEE(ItemType.CHEAP_COFFEE, CheapCoffee.class),
    TEPID_GREEN_TEA(ItemType.TEPID_GREEN_TEA, TepidGreenTea.class),
    PULPOUS_ORANGE_JUICE(ItemType.PULPOUS_ORANGE_JUICE, PulpousOrangeJuice.class),
    BITTER_ICE_TEA(ItemType.BITTER_ICE_TEA, BitterIceTea.class),
    KNOCKOFF_COLA(ItemType.KNOCKOFF_COLA, KnockoffCola.class),
    DECENT_COFFEE(ItemType.DECENT_COFFEE, DecentCoffee.class),
    WOLF_FUR_MIXIN(ItemType.WOLF_FUR_MIXIN, WolfFurMixin.class),
    ZOMBIE_BRAIN_MIXIN(ItemType.ZOMBIE_BRAIN_MIXIN, ZombieBrainMixin.class),
    SPIDER_EGG_MIXIN(ItemType.SPIDER_EGG_MIXIN, SpiderEggMixin.class),
    END_PORTAL_FUMES(ItemType.END_PORTAL_FUMES, EndPortalFumes.class),
    GABAGOEY_MIXIN(ItemType.GABAGOEY_MIXIN, GabagoeyMixin.class),
    BLACK_COFFEE(ItemType.BLACK_COFFEE, BlackCoffee.class),

    /**
     * DungeonItem
     */
    ANCIENT_ROSE(ItemType.ANCIENT_ROSE, AncientRose.class),
    ARCHITECTS_FIRST_DRAFT(ItemType.ARCHITECTS_FIRST_DRAFT, ArchitectsFirstDraft.class),
    KISMET_FEATHER(ItemType.KISMET_FEATHER, KismetFeather.class),
    NECRONS_HANDLE(ItemType.NECRONS_HANDLE, NecronHandle.class),

    /**
     * Mythological Ritual
     */
    GRIFFIN_FEATHER(ItemType.GRIFFIN_FEATHER, GriffinFeather.class),
    ANCIENT_CLAW(ItemType.ANCIENT_CLAW, AncientClaw.class),
    ANTIQUE_REMEDIES(ItemType.ANTIQUE_REMEDIES, AntiqueRemedies.class),
    CROCHET_TIGER_PLUSHIE(ItemType.CROCHET_TIGER_PLUSHIE, CrochetTigerPlushie.class),
    DWARF_TURTLE_SHELMET(ItemType.DWARF_TURTLE_SHELMET, DwarfTurtleShelmet.class),
    DAEDALUS_STICK(ItemType.DAEDALUS_STICK, DaedalusStick.class),
    MINOS_RELIC(ItemType.MINOS_RELIC, MinosRelic.class),
    CROWN_OF_GREED(ItemType.CROWN_OF_GREED, CrownOfGreed.class),
    WASHED_UP_SOUVENIR(ItemType.WASHED_UP_SOUVENIR, WashedUpSouvenir.class),
    DAEDALUS_AXE(ItemType.DAEDALUS_AXE, DaedalusAxe.class),
    SWORD_OF_REVELATIONS(ItemType.SWORD_OF_REVELATIONS, SwordOfRevelations.class),

    /**
     * Spooky Festival
     */
    GREEN_CANDY(ItemType.GREEN_CANDY, GreenCandy.class),
    PRUPLE_CANDY(ItemType.PRUPLE_CANDY, PurpleCandy.class),
    WEREWOLF_SKIN(ItemType.WEREWOLF_SKIN, WerewolfSkin.class),
    SOUL_FRAGMENT(ItemType.SOUL_FRAGMENT, SoulFragment.class),
    ECTOPLASM(ItemType.ECTOPLASM, Ectoplasm.class),
    BLAST_O_LANTERN(ItemType.BLAST_O_LANTERN, BlastOLantern.class),
    PUMPKIN_GUTS(ItemType.PUMPKIN_GUTS, PumpkinGuts.class),
    SPOOKY_SHARD(ItemType.SPOOKY_SHARD, SpookyShard.class),
    HORSEMAN_CANDLE(ItemType.HORSEMAN_CANDLE, HorsemanCandle.class),
    BAT_FIREWORK(ItemType.BAT_FIREWORK, BatFirework.class),

    /**
     * Armor Sets
     */
    LEAFLET_HELMET(ItemType.LEAFLET_HELMET, LeafletHelmet.class),
    LEAFLET_CHESTPLATE(ItemType.LEAFLET_CHESTPLATE, LeafletChestplate.class),
    LEAFLET_LEGGINGS(ItemType.LEAFLET_LEGGINGS, LeafletLeggings.class),
    LEAFLET_BOOTS(ItemType.LEAFLET_BOOTS, LeafletBoots.class),
    MINERS_OUTFIT_HELMET(ItemType.MINERS_OUTFIT_HELMET, MinerOutfitHelmet.class),
    MINERS_OUTFIT_CHESTPLATE(ItemType.MINERS_OUTFIT_CHESTPLATE, MinerOutfitChestplate.class),
    MINERS_OUTFIT_LEGGINGS(ItemType.MINERS_OUTFIT_LEGGINGS, MinerOutfitLeggings.class),
    MINERS_OUTFIT_BOOTS(ItemType.MINERS_OUTFIT_BOOTS, MinerOutfitBoots.class),
    FARM_SUIT_HELMET(ItemType.FARM_SUIT_HELMET, FarmSuitHelmet.class),
    FARM_SUIT_CHESTPLATE(ItemType.FARM_SUIT_CHESTPLATE, FarmSuitChestplate.class),
    FARM_SUIT_LEGGINGS(ItemType.FARM_SUIT_LEGGINGS, FarmSuitLeggings.class),
    FARM_SUIT_BOOTS(ItemType.FARM_SUIT_BOOTS, FarmSuitBoots.class),
    ROSETTA_HELMET(ItemType.ROSETTA_HELMET, RosettaHelmet.class),
    ROSETTA_CHESTPLATE(ItemType.ROSETTA_CHESTPLATE, RosettaChestplate.class),
    ROSETTA_LEGGINGS(ItemType.ROSETTA_LEGGINGS, RosettaLeggings.class),
    ROSETTA_BOOTS(ItemType.ROSETTA_BOOTS, RosettaBoots.class),
    SQUIRE_HELMET(ItemType.SQUIRE_HELMET, SquireHelmet.class),
    SQUIRE_CHESTPLATE(ItemType.SQUIRE_CHESTPLATE, SquireChestplate.class),
    SQUIRE_LEGGINGS(ItemType.SQUIRE_LEGGINGS, SquireLeggings.class),
    SQUIRE_BOOTS(ItemType.SQUIRE_BOOTS, SquireBoots.class),
    MERCENARY_HELMET(ItemType.MERCENARY_HELMET, MercenaryHelmet.class),
    MERCENARY_CHESTPLATE(ItemType.MERCENARY_CHESTPLATE, MercenaryChestplate.class),
    MERCENARY_LEGGINGS(ItemType.MERCENARY_LEGGINGS, MercenaryLeggings.class),
    MERCENARY_BOOTS(ItemType.MERCENARY_BOOTS, MercenaryBoots.class),
    CELESTE_HELMET(ItemType.CELESTE_HELMET, CelesteHelmet.class),
    CELESTE_CHESTPLATE(ItemType.CELESTE_CHESTPLATE, CelesteChestplate.class),
    CELESTE_LEGGINGS(ItemType.CELESTE_LEGGINGS, CelesteLeggings.class),
    CELESTE_BOOTS(ItemType.CELESTE_BOOTS, CelesteBoots.class),
    STARLIGHT_HELMET(ItemType.STARLIGHT_HELMET, StarlightHelmet.class),
    STARLIGHT_CHESTPLATE(ItemType.STARLIGHT_CHESTPLATE, StarlightChestplate.class),
    STARLIGHT_LEGGINGS(ItemType.STARLIGHT_LEGGINGS, StarlightLeggings.class),
    STARLIGHT_BOOTS(ItemType.STARLIGHT_BOOTS, StarlightBoots.class),
    CHEAP_TUXEDO_CHESTPLATE(ItemType.CHEAP_TUXEDO_CHESTPLATE, CheapTuxedoChestplate.class),
    CHEAP_TUXEDO_LEGGINGS(ItemType.CHEAP_TUXEDO_LEGGINGS, CheapTuxedoLeggings.class),
    CHEAP_TUXEDO_BOOTS(ItemType.CHEAP_TUXEDO_BOOTS, CheapTuxedoBoots.class),
    FANCY_TUXEDO_CHESTPLATE(ItemType.FANCY_TUXEDO_CHESTPLATE, FancyTuxedoChestplate.class),
    FANCY_TUXEDO_LEGGINGS(ItemType.FANCY_TUXEDO_LEGGINGS, FancyTuxedoLeggings.class),
    FANCY_TUXEDO_BOOTS(ItemType.FANCY_TUXEDO_BOOTS, FancyTuxedoBoots.class),
    ELEGANT_TUXEDO_CHESTPLATE(ItemType.ELEGANT_TUXEDO_CHESTPLATE, ElegantTuxedoChestplate.class),
    ELEGANT_TUXEDO_LEGGINGS(ItemType.ELEGANT_TUXEDO_LEGGINGS, ElegantTuxedoLeggings.class),
    ELEGANT_TUXEDO_BOOTS(ItemType.ELEGANT_TUXEDO_BOOTS, ElegantTuxedoBoots.class),
    MUSHROOM_HELMET(ItemType.MUSHROOM_HELMET, MushroomHelmet.class),
    MUSHROOM_CHESTPLATE(ItemType.MUSHROOM_CHESTPLATE, MushroomChestplate.class),
    MUSHROOM_LEGGINGS(ItemType.MUSHROOM_LEGGINGS, MushroomLeggings.class),
    MUSHROOM_BOOTS(ItemType.MUSHROOM_BOOTS, MushroomBoots.class),
    PUMPKIN_HELMET(ItemType.PUMPKIN_HELMET, PumpkinHelmet.class),
    PUMPKIN_CHESTPLATE(ItemType.PUMPKIN_CHESTPLATE, PumpkinChestplate.class),
    PUMPKIN_LEGGINGS(ItemType.PUMPKIN_LEGGINGS, PumpkinLeggings.class),
    PUMPKIN_BOOTS(ItemType.PUMPKIN_BOOTS, PumpkinBoots.class),


    /**
     * Pickaxes
     */
    PIONEERS_PICKAXE(ItemType.PIONEERS_PICKAXE, PioneersPickaxe.class),
    DIAMOND_PICKAXE(ItemType.DIAMOND_PICKAXE, DiamondPickaxe.class),
    IRON_PICKAXE(ItemType.IRON_PICKAXE, IronPickaxe.class),
    STONE_PICKAXE(ItemType.STONE_PICKAXE, StonePickaxe.class),
    WOODEN_PICKAXE(ItemType.WOODEN_PICKAXE, WoodenPickaxe.class),
    GOLDEN_PICKAXE(ItemType.GOLDEN_PICKAXE, GoldenPickaxe.class),
    ROOKIE_PICKAXE(ItemType.ROOKIE_PICKAXE, RookiePickaxe.class),
    PICKONIMBUS_2000(ItemType.PICKONIMBUS_2000, Pickonimbus2000.class),

    /**
     * Swords
     */
    HYPERION(ItemType.HYPERION, Hyperion.class),
    ROGUE_SWORD(ItemType.ROGUE_SWORD, RogueSword.class),
    DIAMOND_SWORD(ItemType.DIAMOND_SWORD, DiamondSword.class),
    IRON_SWORD(ItemType.IRON_SWORD, IronSword.class),
    STONE_SWORD(ItemType.STONE_SWORD, StoneSword.class),
    WOODEN_SWORD(ItemType.WOODEN_SWORD, WoodenSword.class),
    GOLDEN_SWORD(ItemType.GOLDEN_SWORD, GoldenSword.class),
    UNDEAD_SWORD(ItemType.UNDEAD_SWORD, UndeadSword.class),
    END_SWORD(ItemType.END_SWORD, EndSword.class),
    SPIDER_SWORD(ItemType.SPIDER_SWORD, SpiderSword.class),
    ASPECT_OF_THE_JERRY(ItemType.ASPECT_OF_THE_JERRY, AspectOfTheJerry.class),
    FANCY_SWORD(ItemType.FANCY_SWORD, FancySword.class),
    HUNTER_KNIFE(ItemType.HUNTER_KNIFE, HunterKnife.class),
    PRISMARINE_BLADE(ItemType.PRISMARINE_BLADE, PrismarineBlade.class),
    SILVER_FANG(ItemType.SILVER_FANG, SilverFang.class),
    ASPECT_OF_THE_END(ItemType.ASPECT_OF_THE_END, AspectOfTheEnd.class),
    SQUIRE_SWORD(ItemType.SQUIRE_SWORD, SquireSword.class),
    MERCENARY_AXE(ItemType.MERCENARY_AXE, MercenaryAxe.class),

    /**
     * Shovels
     */
    WOODEN_SHOVEL(ItemType.WOODEN_SHOVEL, WoodenShovel.class),
    STONE_SHOVEL(ItemType.STONE_SHOVEL, StoneShovel.class),
    GOLDEN_SHOVEL(ItemType.GOLDEN_SHOVEL, GoldenShovel.class),
    IRON_SHOVEL(ItemType.IRON_SHOVEL, IronShovel.class),
    DIAMOND_SHOVEL(ItemType.DIAMOND_SHOVEL, DiamondShovel.class),

    /**
     * Bows
     */
    WITHER_BOW(ItemType.WITHER_BOW, WitherBow.class),
    ARTISANAL_SHORTBOW(ItemType.ARTISANAL_SHORTBOW, ArtisanalShortbow.class),
    BOW(ItemType.BOW, Bow.class),

    /**
     * Arrows
     */
    FLINT_ARROW(ItemType.FLINT_ARROW, FlintArrow.class),

    /**
     * Jerry's Workshop
     */
    WHITE_GIFT(ItemType.WHITE_GIFT, WhiteGift.class),
    GREEN_GIFT(ItemType.GREEN_GIFT, GreenGift.class),
    RED_GIFT(ItemType.RED_GIFT, RedGift.class),
    GLACIAL_FRAGMENT(ItemType.GLACIAL_FRAGMENT, GlacialFragment.class),

    /**
     * Mining Materials
     */
    ROUGH_AMBER_GEM(ItemType.ROUGH_AMBER_GEM, RoughAmber.class),
    ROUGH_TOPAZ_GEM(ItemType.ROUGH_TOPAZ_GEM, RoughTopaz.class),
    ROUGH_SAPPHIRE_GEM(ItemType.ROUGH_SAPPHIRE_GEM, RoughSapphire.class),
    ROUGH_AMETHYST_GEM(ItemType.ROUGH_AMETHYST_GEM, RoughAmethyst.class),
    ROUGH_JASPER_GEM(ItemType.ROUGH_JASPER_GEM, RoughJasper.class),
    ROUGH_RUBY_GEM(ItemType.ROUGH_RUBY_GEM, RoughRuby.class),
    ROUGH_JADE_GEM(ItemType.ROUGH_JADE_GEM, RoughJade.class),
    FLAWED_AMBER_GEM(ItemType.FLAWED_AMBER_GEM, FlawedAmber.class),
    FLAWED_TOPAZ_GEM(ItemType.FLAWED_TOPAZ_GEM, FlawedTopaz.class),
    FLAWED_SAPPHIRE_GEM(ItemType.FLAWED_SAPPHIRE_GEM, FlawedSapphire.class),
    FLAWED_AMETHYST_GEM(ItemType.FLAWED_AMETHYST_GEM, FlawedAmethyst.class),
    FLAWED_JASPER_GEM(ItemType.FLAWED_JASPER_GEM, FlawedJasper.class),
    FLAWED_RUBY_GEM(ItemType.FLAWED_RUBY_GEM, FlawedRuby.class),
    FLAWED_JADE_GEM(ItemType.FLAWED_JADE_GEM, FlawedJade.class),
    FINE_AMBER_GEM(ItemType.FINE_AMBER_GEM, FineAmber.class),
    FINE_TOPAZ_GEM(ItemType.FINE_TOPAZ_GEM, FineTopaz.class),
    FINE_SAPPHIRE_GEM(ItemType.FINE_SAPPHIRE_GEM, FineSapphire.class),
    FINE_AMETHYST_GEM(ItemType.FINE_AMETHYST_GEM, FineAmethyst.class),
    FINE_JASPER_GEM(ItemType.FINE_JASPER_GEM, FineJasper.class),
    FINE_RUBY_GEM(ItemType.FINE_RUBY_GEM, FineRuby.class),
    FINE_JADE_GEM(ItemType.FINE_JADE_GEM, FineJade.class),
    FLAWLESS_AMBER_GEM(ItemType.FLAWLESS_AMBER_GEM, FlawlessAmber.class),
    FLAWLESS_TOPAZ_GEM(ItemType.FLAWLESS_TOPAZ_GEM, FlawlessTopaz.class),
    FLAWLESS_SAPPHIRE_GEM(ItemType.FLAWLESS_SAPPHIRE_GEM, FlawlessSapphire.class),
    FLAWLESS_AMETHYST_GEM(ItemType.FLAWLESS_AMETHYST_GEM, FlawlessAmethyst.class),
    FLAWLESS_JASPER_GEM(ItemType.FLAWLESS_JASPER_GEM, FlawlessJasper.class),
    FLAWLESS_RUBY_GEM(ItemType.FLAWLESS_RUBY_GEM, FlawlessRuby.class),
    FLAWLESS_JADE_GEM(ItemType.FLAWLESS_JADE_GEM, FlawlessJade.class),
    PERFECT_AMBER_GEM(ItemType.PERFECT_AMBER_GEM, PerfectAmber.class),
    PERFECT_TOPAZ_GEM(ItemType.PERFECT_TOPAZ_GEM, PerfectTopaz.class),
    PERFECT_SAPPHIRE_GEM(ItemType.PERFECT_SAPPHIRE_GEM, PerfectSapphire.class),
    PERFECT_AMETHYST_GEM(ItemType.PERFECT_AMETHYST_GEM, PerfectAmethyst.class),
    PERFECT_JASPER_GEM(ItemType.PERFECT_JASPER_GEM, PerfectJasper.class),
    PERFECT_RUBY_GEM(ItemType.PERFECT_RUBY_GEM, PerfectRuby.class),
    PERFECT_JADE_GEM(ItemType.PERFECT_JADE_GEM, PerfectJade.class),
    HARD_STONE(ItemType.HARD_STONE, HardStone.class),
    MITHRIL(ItemType.MITHRIL, Mithril.class),
    TITANIUM(ItemType.TITANIUM, Titanium.class),
    CONCENTRATED_STONE(ItemType.CONCENTRATED_STONE, ConcentratedStone.class),

    /**
     * Forge Items
     */
    REFINED_MITHRIL(ItemType.REFINED_MITHRIL, RefinedMithril.class),
    REFINED_TITANIUM(ItemType.REFINED_TITANIUM, RefinedTitanium.class),

    /**
     * Other Mining Stuff
     */
    GOBLIN_EGG(ItemType.GOBLIN_EGG, GoblinEgg.class),
    YELLOW_GOBLIN_EGG(ItemType.YELLOW_GOBLIN_EGG, YellowGoblinEgg.class),
    RED_GOBLIN_EGG(ItemType.RED_GOBLIN_EGG, RedGoblinEgg.class),
    GREEN_GOBLIN_EGG(ItemType.GREEN_GOBLIN_EGG, GreenGoblinEgg.class),
    BLUE_GOBLIN_EGG(ItemType.BLUE_GOBLIN_EGG, BlueGoblinEgg.class),
    CONTROL_SWITCH(ItemType.CONTROL_SWITCH, ControlSwitch.class),
    ELECTRON_TRANSMITTER(ItemType.ELECTRON_TRANSMITTER, ElectronTransmitter.class),
    FTX_3070(ItemType.FTX_3070, FTX3070.class),
    ROBOTRON_REFLECTOR(ItemType.ROBOTRON_REFLECTOR, RobotronReflector.class),
    SUPERLITE_MOTOR(ItemType.SUPERLITE_MOTOR, SuperliteMotor.class),
    SYNTHETIC_HEART(ItemType.SYNTHETIC_HEART, SyntheticHeart.class),
    SLUDGE_JUICE(ItemType.SLUDGE_JUICE, SludgeJuice.class),
    OIL_BARREL(ItemType.OIL_BARREL, OilBarrel.class),
    TREASURITE(ItemType.TREASURITE, Treasurite.class),
    JUNGLE_KEY(ItemType.JUNGLE_KEY, JungleKey.class),
    WISHING_COMPASS(ItemType.WISHING_COMPASS, WishingCompass.class),
    WORM_MEMBRANE(ItemType.WORM_MEMBRANE, WormMembrane.class),
    MAGMA_CORE(ItemType.MAGMA_CORE, MagmaCore.class),
    ETERNAL_FLAME_RING(ItemType.ETERNAL_FLAME_RING, EternalFlameRing.class),
    HELIX(ItemType.HELIX, Helix.class),
    BOB_OMB(ItemType.BOB_OMB, Bobomb.class),
    PREHISTORIC_EGG(ItemType.PREHISTORIC_EGG, PrehistoricEgg.class),
    RECALL_POTION(ItemType.RECALL_POTION, RecallPotion.class),
    GEMSTONE_MIXTURE(ItemType.GEMSTONE_MIXTURE, GemstoneMixture.class),
    DIVAN_FRAGMENT(ItemType.DIVAN_FRAGMENT, DivanFragment.class),
    DIVAN_ALLOY(ItemType.DIVAN_ALLOY, DivanAlloy.class),
    GLACITE_JEWEL(ItemType.GLACITE_JEWEL, GlaciteJewel.class),
    STARFALL(ItemType.STARFALL, Starfall.class),
    SORROW(ItemType.SORROW, Sorrow.class),
    PLASMA(ItemType.PLASMA, Plasma.class),
    VOLTA(ItemType.VOLTA, Volta.class),

    /**
     * Travel Scrolls
     */
    HUB_CASTLE_TRAVEL_SCROLL(ItemType.HUB_CASTLE_TRAVEL_SCROLL, HubCastleTravelScroll.class),
    HUB_MUSEUM_TRAVEL_SCROLL(ItemType.HUB_MUSEUM_TRAVEL_SCROLL, HubMuseumTravelScroll.class),
    HUB_CRYPT_TRAVEL_SCROLL(ItemType.HUB_CRYPT_TRAVEL_SCROLL, HubCryptsTravelScroll.class),

    /**
     * Crimson Isles
     */
    FLAMES(ItemType.FLAMES, Flames.class),

    /**
     * Vanilla Items
     */
    CRAFTING_TABLE(ItemType.CRAFTING_TABLE, CraftingTable.class),
    ANVIL(ItemType.ANVIL, Anvil.class),
    STICK(ItemType.STICK, Stick.class),
    ACACIA_WOOD(ItemType.ACACIA_WOOD, Acacia.class),
    BAKED_POTATO(ItemType.BAKED_POTATO, BakedPotato.class),
    BIRCH_WOOD(ItemType.BIRCH_WOOD, Birch.class),
    BLAZE_ROD(ItemType.BLAZE_ROD, BlazeRod.class),
    BONE(ItemType.BONE, Bone.class),
    BONE_MEAL(ItemType.BONE_MEAL, BoneMeal.class),
    CARROT(ItemType.CARROT, Carrot.class),
    COAL(ItemType.COAL, Coal.class),
    COCOA_BEANS(ItemType.COCOA_BEANS, CocoaBean.class),
    DARK_OAK_WOOD(ItemType.DARK_OAK_WOOD, DarkOak.class),
    DIAMOND(ItemType.DIAMOND, Diamond.class),
    DIAMOND_BLOCK(ItemType.DIAMOND_BLOCK, DiamondBlock.class),
    EGG(ItemType.EGG, Egg.class),
    EMERALD(ItemType.EMERALD, Emerald.class),
    EMERALD_BLOCK(ItemType.EMERALD_BLOCK, EmeraldBlock.class),
    ENDER_PEARL(ItemType.ENDER_PEARL, EnderPearl.class),
    FEATHER(ItemType.FEATHER, Feather.class),
    FLINT(ItemType.FLINT, Flint.class),
    GLOWSTONE_DUST(ItemType.GLOWSTONE_DUST, GlowstoneDust.class),
    GOLD_INGOT(ItemType.GOLD_INGOT, GoldIngot.class),
    GOLD_BLOCK(ItemType.GOLD_BLOCK, GoldBlock.class),
    GUNPOWDER(ItemType.GUNPOWDER, Gunpowder.class),
    END_STONE(ItemType.END_STONE, EndStone.class),
    EYE_OF_ENDER(ItemType.EYE_OF_ENDER, EyeOfEnder.class),
    GHAST_TEAR(ItemType.GHAST_TEAR, GhastTear.class),
    ICE(ItemType.ICE, Ice.class),
    IRON_INGOT(ItemType.IRON_INGOT, IronIngot.class),
    GRAVEL(ItemType.GRAVEL, Gravel.class),
    LAPIS_LAZULI(ItemType.LAPIS_LAZULI, LapisLazuli.class),
    IRON_BLOCK(ItemType.IRON_BLOCK, IronBlock.class),
    JUNGLE_WOOD(ItemType.JUNGLE_WOOD, Jungle.class),
    LEATHER(ItemType.LEATHER, Leather.class),
    OAK_WOOD(ItemType.OAK_WOOD, Oak.class),
    OAK_LOG(ItemType.OAK_LOG, Oak.class),
    SPRUCE_LOG(ItemType.SPRUCE_LOG, Spruce.class),
    ACACIA_LOG(ItemType.ACACIA_LOG, Acacia.class),
    BIRCH_LOG(ItemType.BIRCH_LOG, Birch.class),
    DARK_OAK_LOG(ItemType.DARK_OAK_LOG, DarkOak.class),
    JUNGLE_LOG(ItemType.JUNGLE_LOG, Jungle.class),
    OBSIDIAN(ItemType.OBSIDIAN, Obsidian.class),
    POTATO(ItemType.POTATO, Potato.class),
    PUMPKIN(ItemType.PUMPKIN, Pumpkin.class),
    REDSTONE(ItemType.REDSTONE, Redstone.class),
    REDSTONE_BLOCK(ItemType.REDSTONE_BLOCK, RedstoneBlock.class),
    ROTTEN_FLESH(ItemType.ROTTEN_FLESH, RottenFlesh.class),
    SLIME_BALL(ItemType.SLIME_BALL, SlimeBall.class),
    SPONGE(ItemType.SPONGE, Sponge.class),
    SPRUCE_WOOD(ItemType.SPRUCE_WOOD, Spruce.class),
    STRING(ItemType.STRING, StringItem.class),
    SUGAR_CANE(ItemType.SUGAR_CANE, SugarCane.class),
    SUGAR(ItemType.SUGAR, Sugar.class),
    SNOW(ItemType.SNOW, Snow.class),
    SNOW_BLOCK(ItemType.SNOW_BLOCK, SnowBlock.class),
    SNOWBALL(ItemType.SNOWBALL, Snowball.class),
    MELON_SLICE(ItemType.MELON_SLICE, Melon.class),
    RED_MUSHROOM(ItemType.RED_MUSHROOM, Mushroom.class),
    BROWN_MUSHROOM(ItemType.BROWN_MUSHROOM, Mushroom.class),
    MUTTON(ItemType.MUTTON, Mutton.class),
    NETHER_WART(ItemType.NETHER_WART, NetherWart.class),
    CHICKEN(ItemType.CHICKEN, RawChicken.class),
    PORKCHOP(ItemType.PORKCHOP, RawPorkchop.class),
    RABBIT(ItemType.RABBIT, RawRabbit.class),
    WHEAT_SEEDS(ItemType.WHEAT_SEEDS, Seeds.class),
    MYCELIUM(ItemType.MYCELIUM, Mycelium.class),
    RED_SAND(ItemType.RED_SAND, RedSand.class),
    QUARTZ(ItemType.QUARTZ, Quartz.class),
    NETHERRACK(ItemType.NETHERRACK, Netherrack.class),
    SAND(ItemType.SAND, Sand.class),
    CLAY_BALL(ItemType.CLAY_BALL, ClayBall.class),
    TROPICAL_FISH(ItemType.TROPICAL_FISH, TropicalFish.class),
    INK_SAC(ItemType.INK_SAC, InkSac.class),
    LILY_PAD(ItemType.LILY_PAD, LilyPad.class),
    PRISMARINE_CRYSTALS(ItemType.PRISMARINE_CRYSTALS, PrismarineCrystals.class),
    PRISMARINE_SHARD(ItemType.PRISMARINE_SHARD, PrismarineShard.class),
    PUFFERFISH(ItemType.PUFFERFISH, Pufferfish.class),
    COD(ItemType.COD, Cod.class),
    SALMON(ItemType.SALMON, Salmon.class),
    SPIDER_EYE(ItemType.SPIDER_EYE, SpiderEye.class),
    CACTUS_GREEN(ItemType.CACTUS_GREEN, CactusGreen.class),
    FERMENTED_SPIDER_EYE(ItemType.FERMENTED_SPIDER_EYE, FermentedSpiderEye.class),
    GLISTERING_MELON(ItemType.GLISTERING_MELON, GlisteringMelon.class),
    GLOWSTONE(ItemType.GLOWSTONE, Glowstone.class),
    HAY_BALE(ItemType.HAY_BALE, HayBale.class),
    POISONOUS_POTATO(ItemType.POISONOUS_POTATO, Potato.class),
    RABBIT_FOOT(ItemType.RABBIT_FOOT, RabbitFoot.class),
    RABBIT_HIDE(ItemType.RABBIT_HIDE, RabbitHide.class),
    MAGMA_CREAM(ItemType.MAGMA_CREAM, MagmaCream.class),
    BEEF(ItemType.BEEF, Beef.class),
    COAL_BLOCK(ItemType.COAL_BLOCK, CoalBlock.class),
    LAPIS_LAZULI_BLOCK(ItemType.LAPIS_LAZULI_BLOCK, LapisLazuliBlock.class),
    BLAZE_POWDER(ItemType.BLAZE_POWDER, BlazePowder.class),
    GOLDEN_CARROT(ItemType.GOLDEN_CARROT, GoldenCarrot.class),
    GOLD_NUGGET(ItemType.GOLD_NUGGET, GoldNugget.class),
    CACTUS(ItemType.CACTUS, Cactus.class),
    CHEST(ItemType.CHEST, Chest.class),
    IRON_HELMET(ItemType.IRON_HELMET, IronHelmet.class),
    IRON_CHESTPLATE(ItemType.IRON_CHESTPLATE, IronChestplate.class),
    IRON_LEGGINGS(ItemType.IRON_LEGGINGS, IronLeggings.class),
    IRON_BOOTS(ItemType.IRON_BOOTS, IronBoots.class),
    LEATHER_HELMET(ItemType.LEATHER_HELMET, LeatherHelmet.class),
    LEATHER_CHESTPLATE(ItemType.LEATHER_CHESTPLATE, LeatherChestplate.class),
    LEATHER_LEGGINGS(ItemType.LEATHER_LEGGINGS, LeatherLeggings.class),
    LEATHER_BOOTS(ItemType.LEATHER_BOOTS, LeatherBoots.class),
    GOLDEN_HELMET(ItemType.GOLDEN_HELMET, GoldenHelmet.class),
    GOLDEN_CHESTPLATE(ItemType.GOLDEN_CHESTPLATE, GoldenChestplate.class),
    GOLDEN_LEGGINGS(ItemType.GOLDEN_LEGGINGS, GoldenLeggings.class),
    GOLDEN_BOOTS(ItemType.GOLDEN_BOOTS, GoldenBoots.class),
    DIAMOND_HELMET(ItemType.DIAMOND_HELMET, DiamondHelmet.class),
    DIAMOND_CHESTPLATE(ItemType.DIAMOND_CHESTPLATE, DiamondChestplate.class),
    DIAMOND_LEGGINGS(ItemType.DIAMOND_LEGGINGS, DiamondLeggings.class),
    DIAMOND_BOOTS(ItemType.DIAMOND_BOOTS, DiamondBoots.class),
    SLIME_BLOCK(ItemType.SLIME_BLOCK, SlimeBlock.class),
    OAK_PLANKS(ItemType.OAK_PLANKS, OakPlanks.class),
    OAK_SLAB(ItemType.OAK_SLAB, OakSlab.class),
    OAK_STAIRS(ItemType.OAK_STAIRS, OakStairs.class),
    OAK_FENCE(ItemType.OAK_FENCE, OakFence.class),
    OAK_FENCE_GATE(ItemType.OAK_FENCE_GATE, OakFenceGate.class),
    OAK_DOOR(ItemType.OAK_DOOR, OakDoor.class),
    OAK_TRAPDOOR(ItemType.OAK_TRAPDOOR, OakTrapdoor.class),
    OAK_PRESSURE_PLATE(ItemType.OAK_PRESSURE_PLATE, OakPressurePlate.class),
    ACACIA_PLANKS(ItemType.ACACIA_PLANKS, AcaciaPlanks.class),
    ACACIA_SLAB(ItemType.ACACIA_SLAB, AcaciaSlab.class),
    ACACIA_STAIRS(ItemType.ACACIA_STAIRS, AcaciaStairs.class),
    ACACIA_FENCE(ItemType.ACACIA_FENCE, AcaciaFence.class),
    ACACIA_FENCE_GATE(ItemType.ACACIA_FENCE_GATE, AcaciaFenceGate.class),
    ACACIA_DOOR(ItemType.ACACIA_DOOR, AcaciaDoor.class),
    ACACIA_TRAPDOOR(ItemType.ACACIA_TRAPDOOR, AcaciaTrapdoor.class),
    ACACIA_PRESSURE_PLATE(ItemType.ACACIA_PRESSURE_PLATE, AcaciaPressurePlate.class),
    BIRCH_PLANKS(ItemType.BIRCH_PLANKS, BirchPlanks.class),
    BIRCH_SLAB(ItemType.BIRCH_SLAB, BirchSlab.class),
    BIRCH_STAIRS(ItemType.BIRCH_STAIRS, BirchStairs.class),
    BIRCH_FENCE(ItemType.BIRCH_FENCE, BirchFence.class),
    BIRCH_FENCE_GATE(ItemType.BIRCH_FENCE_GATE, BirchFenceGate.class),
    BIRCH_DOOR(ItemType.BIRCH_DOOR, BirchDoor.class),
    BIRCH_TRAPDOOR(ItemType.BIRCH_TRAPDOOR, BirchTrapdoor.class),
    BIRCH_PRESSURE_PLATE(ItemType.BIRCH_PRESSURE_PLATE, BirchPressurePlate.class),
    DARK_OAK_PLANKS(ItemType.DARK_OAK_PLANKS, DarkOakPlanks.class),
    DARK_OAK_SLAB(ItemType.DARK_OAK_SLAB, DarkOakSlab.class),
    DARK_OAK_STAIRS(ItemType.DARK_OAK_STAIRS, DarkOakStairs.class),
    DARK_OAK_FENCE(ItemType.DARK_OAK_FENCE, DarkOakFence.class),
    DARK_OAK_FENCE_GATE(ItemType.DARK_OAK_FENCE_GATE, DarkOakFenceGate.class),
    DARK_OAK_DOOR(ItemType.DARK_OAK_DOOR, DarkOakDoor.class),
    DARK_OAK_TRAPDOOR(ItemType.DARK_OAK_TRAPDOOR, DarkOakTrapdoor.class),
    DARK_OAK_PRESSURE_PLATE(ItemType.DARK_OAK_PRESSURE_PLATE, DarkOakPressurePlate.class),
    JUNGLE_PLANKS(ItemType.JUNGLE_PLANKS, JunglePlanks.class),
    JUNGLE_SLAB(ItemType.JUNGLE_SLAB, JungleSlab.class),
    JUNGLE_STAIRS(ItemType.JUNGLE_STAIRS, JungleStairs.class),
    JUNGLE_FENCE(ItemType.JUNGLE_FENCE, JungleFence.class),
    JUNGLE_FENCE_GATE(ItemType.JUNGLE_FENCE_GATE, JungleFenceGate.class),
    JUNGLE_DOOR(ItemType.JUNGLE_DOOR, JungleDoor.class),
    JUNGLE_TRAPDOOR(ItemType.JUNGLE_TRAPDOOR, JungleTrapdoor.class),
    JUNGLE_PRESSURE_PLATE(ItemType.JUNGLE_PRESSURE_PLATE, JunglePressurePlate.class),
    SPRUCE_PLANKS(ItemType.SPRUCE_PLANKS, SprucePlanks.class),
    SPRUCE_SLAB(ItemType.SPRUCE_SLAB, SpruceSlab.class),
    SPRUCE_STAIRS(ItemType.SPRUCE_STAIRS, SpruceStairs.class),
    SPRUCE_FENCE(ItemType.SPRUCE_FENCE, SpruceFence.class),
    SPRUCE_FENCE_GATE(ItemType.SPRUCE_FENCE_GATE, SpruceFenceGate.class),
    SPRUCE_DOOR(ItemType.SPRUCE_DOOR, SpruceDoor.class),
    SPRUCE_TRAPDOOR(ItemType.SPRUCE_TRAPDOOR, SpruceTrapdoor.class),
    SPRUCE_PRESSURE_PLATE(ItemType.SPRUCE_PRESSURE_PLATE, SprucePressurePlate.class),
    LADDER(ItemType.LADDER, Ladder.class),
    STONE(ItemType.STONE, Stone.class),
    STONE_SLAB(ItemType.STONE_SLAB, StoneSlab.class),
    STONE_STAIRS(ItemType.STONE_STAIRS, StoneStairs.class),
    SMOOTH_STONE(ItemType.SMOOTH_STONE, SmoothStone.class),
    SMOOTH_STONE_SLAB(ItemType.SMOOTH_STONE_SLAB, SmoothStoneSlab.class),
    GRANITE(ItemType.GRANITE, Granite.class),
    GRANITE_SLAB(ItemType.GRANITE_SLAB, GraniteSlab.class),
    GRANITE_STAIRS(ItemType.GRANITE_STAIRS, GraniteStairs.class),
    GRANITE_WALL(ItemType.GRANITE_WALL, GraniteWall.class),
    POLISHED_GRANITE(ItemType.POLISHED_GRANITE, PolishedGranite.class),
    POLISHED_GRANITE_SLAB(ItemType.POLISHED_GRANITE_SLAB, PolishedGraniteSlab.class),
    POLISHED_GRANITE_STAIRS(ItemType.POLISHED_GRANITE_STAIRS, PolishedGraniteStairs.class),
    DIORITE(ItemType.DIORITE, Diorite.class),
    DIORITE_SLAB(ItemType.DIORITE_SLAB, DioriteSlab.class),
    DIORITE_STAIRS(ItemType.DIORITE_STAIRS, DioriteStairs.class),
    DIORITE_WALL(ItemType.DIORITE_WALL, DioriteWall.class),
    POLISHED_DIORITE(ItemType.POLISHED_DIORITE, PolishedDiorite.class),
    POLISHED_DIORITE_SLAB(ItemType.POLISHED_DIORITE_SLAB, PolishedDioriteSlab.class),
    POLISHED_DIORITE_STAIRS(ItemType.POLISHED_DIORITE_STAIRS, PolishedDioriteStairs.class),
    ANDESITE(ItemType.ANDESITE, Andesite.class),
    ANDESITE_SLAB(ItemType.ANDESITE_SLAB, AndesiteSlab.class),
    ANDESITE_STAIRS(ItemType.ANDESITE_STAIRS, AndesiteStairs.class),
    ANDESITE_WALL(ItemType.ANDESITE_WALL, AndesiteWall.class),
    POLISHED_ANDESITE(ItemType.POLISHED_ANDESITE, PolishedAndesite.class),
    POLISHED_ANDESITE_SLAB(ItemType.POLISHED_ANDESITE_SLAB, PolishedAndesiteSlab.class),
    POLISHED_ANDESITE_STAIRS(ItemType.POLISHED_ANDESITE_STAIRS, PolishedAndesiteStairs.class),
    COBBLESTONE(ItemType.COBBLESTONE, Cobblestone.class),
    COBBLESTONE_SLAB(ItemType.COBBLESTONE_SLAB, CobblestoneSlab.class),
    COBBLESTONE_STAIRS(ItemType.COBBLESTONE_STAIRS, CobblestoneStairs.class),
    COBBLESTONE_WALL(ItemType.COBBLESTONE_WALL, CobblestoneWall.class),
    SANDSTONE(ItemType.SANDSTONE, Sandstone.class),
    SANDSTONE_SLAB(ItemType.SANDSTONE_SLAB, SandstoneSlab.class),
    SANDSTONE_STAIRS(ItemType.SANDSTONE_STAIRS, SandstoneStairs.class),
    SANDSTONE_WALL(ItemType.SANDSTONE_WALL, SandstoneWall.class),
    CUT_SANDSTONE(ItemType.CUT_SANDSTONE, CutSandstone.class),
    CUT_SANDSTONE_SLAB(ItemType.CUT_SANDSTONE_SLAB, CutSandstoneSlab.class),
    CHISELED_SANDSTONE(ItemType.CHISELED_SANDSTONE, ChiseledSandstone.class),
    RED_SANDSTONE(ItemType.RED_SANDSTONE, RedSandstone.class),
    RED_SANDSTONE_SLAB(ItemType.RED_SANDSTONE_SLAB, RedSandstoneSlab.class),
    RED_SANDSTONE_STAIRS(ItemType.RED_SANDSTONE_STAIRS, RedSandstoneStairs.class),
    RED_SANDSTONE_WALL(ItemType.RED_SANDSTONE_WALL, RedSandstoneWall.class),
    CUT_RED_SANDSTONE(ItemType.CUT_RED_SANDSTONE, CutRedSandstone.class),
    CUT_RED_SANDSTONE_SLAB(ItemType.CUT_RED_SANDSTONE_SLAB, CutRedSandstoneSlab.class),
    CHISELED_RED_SANDSTONE(ItemType.CHISELED_RED_SANDSTONE, ChiseledRedSandstone.class),
    STONE_BRICKS(ItemType.STONE_BRICKS, StoneBricks.class),
    STONE_BRICK_SLAB(ItemType.STONE_BRICK_SLAB, StoneBrickSlab.class),
    STONE_BRICK_STAIRS(ItemType.STONE_BRICK_STAIRS, StoneBrickStairs.class),
    STONE_BRICK_WALL(ItemType.STONE_BRICK_WALL, StoneBrickWall.class),
    MOSSY_STONE_BRICKS(ItemType.MOSSY_STONE_BRICKS, MossyStoneBricks.class),
    MOSSY_STONE_BRICK_SLAB(ItemType.MOSSY_STONE_BRICK_SLAB, MossyStoneBrickSlab.class),
    MOSSY_STONE_BRICK_STAIRS(ItemType.MOSSY_STONE_BRICK_STAIRS, MossyStoneBrickStairs.class),
    MOSSY_STONE_BRICK_WALL(ItemType.MOSSY_STONE_BRICK_WALL, MossyStoneBrickWall.class),
    QUARTZ_BLOCK(ItemType.QUARTZ_BLOCK, QuartzBlock.class),
    QUARTZ_SLAB(ItemType.QUARTZ_SLAB, QuartzSlab.class),
    QUARTZ_STAIRS(ItemType.QUARTZ_STAIRS, QuartzStairs.class),
    QUARTZ_PILLAR(ItemType.QUARTZ_PILLAR, QuartzPillar.class),
    CHISELED_QUARTZ_BLOCK(ItemType.CHISELED_QUARTZ_BLOCK, ChiseledQuartzBlock.class),
    NETHER_BRICKS(ItemType.NETHER_BRICKS, NetherBricks.class),
    NETHER_BRICK_SLAB(ItemType.NETHER_BRICK_SLAB, NetherBrickSlab.class),
    NETHER_BRICK_STAIRS(ItemType.NETHER_BRICK_STAIRS, NetherBrickStairs.class),
    NETHER_BRICK_WALL(ItemType.NETHER_BRICK_WALL, NetherBrickWall.class),
    NETHER_BRICK_FENCE(ItemType.NETHER_BRICK_FENCE, NetherBrickFence.class),
    WHITE_DYE(ItemType.WHITE_DYE, WhiteDye.class),
    ORANGE_DYE(ItemType.ORANGE_DYE, OrangeDye.class),
    MAGENTA_DYE(ItemType.MAGENTA_DYE, MagentaDye.class),
    LIGHT_BLUE_DYE(ItemType.LIGHT_BLUE_DYE, LightBlueDye.class),
    YELLOW_DYE(ItemType.YELLOW_DYE, YellowDye.class),
    LIME_DYE(ItemType.LIME_DYE, LimeDye.class),
    PINK_DYE(ItemType.PINK_DYE, PinkDye.class),
    GRAY_DYE(ItemType.GRAY_DYE, GrayDye.class),
    LIGHT_GRAY_DYE(ItemType.LIGHT_GRAY_DYE, LightGrayDye.class),
    CYAN_DYE(ItemType.CYAN_DYE, CyanDye.class),
    PURPLE_DYE(ItemType.PURPLE_DYE, PurpleDye.class),
    BLUE_DYE(ItemType.BLUE_DYE, BlueDye.class),
    BROWN_DYE(ItemType.BROWN_DYE, BrownDye.class),
    RED_DYE(ItemType.RED_DYE, RedDye.class),
    BLACK_DYE(ItemType.BLACK_DYE, BlackDye.class),
    WHITE_WOOL(ItemType.WHITE_WOOL, WhiteWool.class),
    ORANGE_WOOL(ItemType.ORANGE_WOOL, OrangeWool.class),
    MAGENTA_WOOL(ItemType.MAGENTA_WOOL, MagentaWool.class),
    LIGHT_BLUE_WOOL(ItemType.LIGHT_BLUE_WOOL, LightBlueWool.class),
    YELLOW_WOOL(ItemType.YELLOW_WOOL, YellowWool.class),
    LIME_WOOL(ItemType.LIME_WOOL, LimeWool.class),
    PINK_WOOL(ItemType.PINK_WOOL, PinkWool.class),
    GRAY_WOOL(ItemType.GRAY_WOOL, GrayWool.class),
    LIGHT_GRAY_WOOL(ItemType.LIGHT_GRAY_WOOL, LightGrayWool.class),
    CYAN_WOOL(ItemType.CYAN_WOOL, CyanWool.class),
    PURPLE_WOOL(ItemType.PURPLE_WOOL, PurpleWool.class),
    BLUE_WOOL(ItemType.BLUE_WOOL, BlueWool.class),
    BROWN_WOOL(ItemType.BROWN_WOOL, BrownWool.class),
    GREEN_WOOL(ItemType.GREEN_WOOL, GreenWool.class),
    RED_WOOL(ItemType.RED_WOOL, RedWool.class),
    BLACK_WOOL(ItemType.BLACK_WOOL, BlackWool.class),
    WHITE_CARPET(ItemType.WHITE_CARPET, WhiteCarpet.class),
    ORANGE_CARPET(ItemType.ORANGE_CARPET, OrangeCarpet.class),
    MAGENTA_CARPET(ItemType.MAGENTA_CARPET, MagentaCarpet.class),
    LIGHT_BLUE_CARPET(ItemType.LIGHT_BLUE_CARPET, LightBlueCarpet.class),
    YELLOW_CARPET(ItemType.YELLOW_CARPET, YellowCarpet.class),
    LIME_CARPET(ItemType.LIME_CARPET, LimeCarpet.class),
    PINK_CARPET(ItemType.PINK_CARPET, PinkCarpet.class),
    GRAY_CARPET(ItemType.GRAY_CARPET, GrayCarpet.class),
    LIGHT_GRAY_CARPET(ItemType.LIGHT_GRAY_CARPET, LightGrayCarpet.class),
    CYAN_CARPET(ItemType.CYAN_CARPET, CyanCarpet.class),
    PURPLE_CARPET(ItemType.PURPLE_CARPET, PurpleCarpet.class),
    BLUE_CARPET(ItemType.BLUE_CARPET, BlueCarpet.class),
    BROWN_CARPET(ItemType.BROWN_CARPET, BrownCarpet.class),
    GREEN_CARPET(ItemType.GREEN_CARPET, GreenCarpet.class),
    RED_CARPET(ItemType.RED_CARPET, RedCarpet.class),
    BLACK_CARPET(ItemType.BLACK_CARPET, BlackCarpet.class),
    GLASS(ItemType.GLASS, Glass.class),
    WHITE_STAINED_GLASS(ItemType.WHITE_STAINED_GLASS, WhiteStainedGlass.class),
    ORANGE_STAINED_GLASS(ItemType.ORANGE_STAINED_GLASS, OrangeStainedGlass.class),
    MAGENTA_STAINED_GLASS(ItemType.MAGENTA_STAINED_GLASS, MagentaStainedGlass.class),
    LIGHT_BLUE_STAINED_GLASS(ItemType.LIGHT_BLUE_STAINED_GLASS, LightBlueStainedGlass.class),
    YELLOW_STAINED_GLASS(ItemType.YELLOW_STAINED_GLASS, YellowStainedGlass.class),
    LIME_STAINED_GLASS(ItemType.LIME_STAINED_GLASS, LimeStainedGlass.class),
    PINK_STAINED_GLASS(ItemType.PINK_STAINED_GLASS, PinkStainedGlass.class),
    GRAY_STAINED_GLASS(ItemType.GRAY_STAINED_GLASS, GrayStainedGlass.class),
    LIGHT_GRAY_STAINED_GLASS(ItemType.LIGHT_GRAY_STAINED_GLASS, LightGrayStainedGlass.class),
    CYAN_STAINED_GLASS(ItemType.CYAN_STAINED_GLASS, CyanStainedGlass.class),
    PURPLE_STAINED_GLASS(ItemType.PURPLE_STAINED_GLASS, PurpleStainedGlass.class),
    BLUE_STAINED_GLASS(ItemType.BLUE_STAINED_GLASS, BlueStainedGlass.class),
    BROWN_STAINED_GLASS(ItemType.BROWN_STAINED_GLASS, BrownStainedGlass.class),
    GREEN_STAINED_GLASS(ItemType.GREEN_STAINED_GLASS, GreenStainedGlass.class),
    RED_STAINED_GLASS(ItemType.RED_STAINED_GLASS, RedStainedGlass.class),
    BLACK_STAINED_GLASS(ItemType.BLACK_STAINED_GLASS, BlackStainedGlass.class),
    GLASS_PANE(ItemType.GLASS_PANE, GlassPane.class),
    WHITE_STAINED_GLASS_PANE(ItemType.WHITE_STAINED_GLASS_PANE, WhiteStainedGlassPane.class),
    ORANGE_STAINED_GLASS_PANE(ItemType.ORANGE_STAINED_GLASS_PANE, OrangeStainedGlassPane.class),
    MAGENTA_STAINED_GLASS_PANE(ItemType.MAGENTA_STAINED_GLASS_PANE, MagentaStainedGlassPane.class),
    LIGHT_BLUE_STAINED_GLASS_PANE(ItemType.LIGHT_BLUE_STAINED_GLASS_PANE, LightBlueStainedGlassPane.class),
    YELLOW_STAINED_GLASS_PANE(ItemType.YELLOW_STAINED_GLASS_PANE, YellowStainedGlassPane.class),
    LIME_STAINED_GLASS_PANE(ItemType.LIME_STAINED_GLASS_PANE, LimeStainedGlassPane.class),
    PINK_STAINED_GLASS_PANE(ItemType.PINK_STAINED_GLASS_PANE, PinkStainedGlassPane.class),
    GRAY_STAINED_GLASS_PANE(ItemType.GRAY_STAINED_GLASS_PANE, GrayStainedGlassPane.class),
    LIGHT_GRAY_STAINED_GLASS_PANE(ItemType.LIGHT_GRAY_STAINED_GLASS_PANE, LightGrayStainedGlassPane.class),
    CYAN_STAINED_GLASS_PANE(ItemType.CYAN_STAINED_GLASS_PANE, CyanStainedGlassPane.class),
    PURPLE_STAINED_GLASS_PANE(ItemType.PURPLE_STAINED_GLASS_PANE, PurpleStainedGlassPane.class),
    BLUE_STAINED_GLASS_PANE(ItemType.BLUE_STAINED_GLASS_PANE, BlueStainedGlassPane.class),
    BROWN_STAINED_GLASS_PANE(ItemType.BROWN_STAINED_GLASS_PANE, BrownStainedGlassPane.class),
    GREEN_STAINED_GLASS_PANE(ItemType.GREEN_STAINED_GLASS_PANE, GreenStainedGlassPane.class),
    RED_STAINED_GLASS_PANE(ItemType.RED_STAINED_GLASS_PANE, RedStainedGlassPane.class),
    BLACK_STAINED_GLASS_PANE(ItemType.BLACK_STAINED_GLASS_PANE, BlackStainedGlassPane.class),

    /**
     * Enchanted Items
     */
    ENCHANTED_ACACIA_WOOD(ItemType.ENCHANTED_ACACIA_WOOD, EnchantedAcaciaWood.class),
    ENCHANTED_BAKED_POTATO(ItemType.ENCHANTED_BAKED_POTATO, EnchantedBakedPotato.class),
    ENCHANTED_BIRCH_WOOD(ItemType.ENCHANTED_BIRCH_WOOD, EnchantedBirchWood.class),
    ENCHANTED_BLAZE_ROD(ItemType.ENCHANTED_BLAZE_ROD, EnchantedBlazeRod.class),
    ENCHANTED_BLAZE_POWDER(ItemType.ENCHANTED_BLAZE_POWDER, EnchantedBlazePowder.class),
    ENCHANTED_BONE(ItemType.ENCHANTED_BONE, EnchantedBone.class),
    ENCHANTED_CARROT(ItemType.ENCHANTED_CARROT, EnchantedCarrot.class),
    ENCHANTED_CHARCOAL(ItemType.ENCHANTED_CHARCOAL, EnchantedCharcoal.class),
    ENCHANTED_COAL(ItemType.ENCHANTED_COAL, EnchantedCoal.class),
    ENCHANTED_COBBLESTONE(ItemType.ENCHANTED_COBBLESTONE, EnchantedCobblestone.class),
    ENCHANTED_COCOA_BEANS(ItemType.ENCHANTED_COCOA_BEANS, EnchantedCocoaBeans.class),
    ENCHANTED_DARK_OAK_WOOD(ItemType.ENCHANTED_DARK_OAK_WOOD, EnchantedDarkOakWood.class),
    ENCHANTED_DIAMOND(ItemType.ENCHANTED_DIAMOND, EnchantedDiamond.class),
    ENCHANTED_EGG(ItemType.ENCHANTED_EGG, EnchantedEgg.class),
    SUPER_ENCHANTED_EGG(ItemType.SUPER_ENCHANTED_EGG, SuperEnchantedEgg.class),
    OMEGA_ENCHANTED_EGG(ItemType.OMEGA_ENCHANTED_EGG, OmegaEnchantedEgg.class),
    ENCHANTED_EMERALD(ItemType.ENCHANTED_EMERALD, EnchantedEmerald.class),
    ENCHANTED_ENDER_PEARL(ItemType.ENCHANTED_ENDER_PEARL, EnchantedEnderPearl.class),
    ENCHANTED_DIAMOND_BLOCK(ItemType.ENCHANTED_DIAMOND_BLOCK, EnchantedDiamondBlock.class),
    ENCHANTED_EMERALD_BLOCK(ItemType.ENCHANTED_EMERALD_BLOCK, EnchantedEmeraldBlock.class),
    ENCHANTED_GOLD_INGOT(ItemType.ENCHANTED_GOLD_INGOT, EnchantedGold.class),
    ENCHANTED_GOLD_BLOCK(ItemType.ENCHANTED_GOLD_BLOCK, EnchantedGoldBlock.class),
    ENCHANTED_JUNGLE_WOOD(ItemType.ENCHANTED_JUNGLE_WOOD, EnchantedJungleWood.class),
    ENCHANTED_GUNPOWDER(ItemType.ENCHANTED_GUNPOWDER, EnchantedGunpowder.class),
    ENCHANTED_IRON_INGOT(ItemType.ENCHANTED_IRON_INGOT, EnchantedIron.class),
    ENCHANTED_IRON_BLOCK(ItemType.ENCHANTED_IRON_BLOCK, EnchantedIronBlock.class),
    ENCHANTED_LEATHER(ItemType.ENCHANTED_LEATHER, EnchantedLeather.class),
    ENCHANTED_OAK_WOOD(ItemType.ENCHANTED_OAK_WOOD, EnchantedOakWood.class),
    ENCHANTED_OBSIDIAN(ItemType.ENCHANTED_OBSIDIAN, EnchantedObsidian.class),
    ENCHANTED_PACKED_ICE(ItemType.ENCHANTED_PACKED_ICE, EnchantedPackedIce.class),
    ENCHANTED_POTATO(ItemType.ENCHANTED_POTATO, EnchantedPotato.class),
    ENCHANTED_PUMPKIN(ItemType.ENCHANTED_PUMPKIN, EnchantedPumpkin.class),
    ENCHANTED_REDSTONE(ItemType.ENCHANTED_REDSTONE, EnchantedRedstone.class),
    ENCHANTED_REDSTONE_BLOCK(ItemType.ENCHANTED_REDSTONE_BLOCK, EnchantedRedstoneBlock.class),
    ENCHANTED_ROTTEN_FLESH(ItemType.ENCHANTED_ROTTEN_FLESH, EnchantedRottenFlesh.class),
    ENCHANTED_SPONGE(ItemType.ENCHANTED_SPONGE, EnchantedSponge.class),
    ENCHANTED_SPRUCE_WOOD(ItemType.ENCHANTED_SPRUCE_WOOD, EnchantedSpruceWood.class),
    ENCHANTED_STRING(ItemType.ENCHANTED_STRING, EnchantedString.class),
    ENCHANTED_SUGAR(ItemType.ENCHANTED_SUGAR, EnchantedSugar.class),
    ENCHANTED_SNOW_BLOCK(ItemType.ENCHANTED_SNOW_BLOCK, EnchantedSnowBlock.class),
    ENCHANTED_BROWN_MUSHROOM(ItemType.ENCHANTED_BROWN_MUSHROOM, EnchantedBrownMushroom.class),
    ENCHANTED_RED_MUSHROOM(ItemType.ENCHANTED_RED_MUSHROOM, EnchantedRedMushroom.class),
    ENCHANTED_CACTUS_GREEN(ItemType.ENCHANTED_CACTUS_GREEN, EnchantedCactusGreen.class),
    ENCHANTED_CACTUS(ItemType.ENCHANTED_CACTUS, EnchantedCactus.class),
    ENCHANTED_ENDSTONE(ItemType.ENCHANTED_ENDSTONE, EnchantedEndStone.class),
    ENCHANTED_EYE_OF_ENDER(ItemType.ENCHANTED_EYE_OF_ENDER, EnchantedEyeOfEnder.class),
    ENCHANTED_ICE(ItemType.ENCHANTED_ICE, EnchantedIce.class),
    ENCHANTED_MAGMA_CREAM(ItemType.ENCHANTED_MAGMA_CREAM, EnchantedMagmaCream.class),
    ENCHANTED_BROWN_MUSHROOM_BLOCK(ItemType.ENCHANTED_BROWN_MUSHROOM_BLOCK, EnchantedBrownMushroomBlock.class),
    ENCHANTED_CLAY(ItemType.ENCHANTED_CLAY, EnchantedClay.class),
    ENCHANTED_CLOWNFISH(ItemType.ENCHANTED_CLOWNFISH, EnchantedClownfish.class),
    ENCHANTED_COD(ItemType.ENCHANTED_COD, EnchantedRawFish.class),
    ENCHANTED_COOKED_COD(ItemType.ENCHANTED_COOKED_COD, EnchantedCookedFish.class),
    ENCHANTED_MUTTON(ItemType.ENCHANTED_MUTTON, EnchantedMutton.class),
    ENCHANTED_COOKED_MUTTON(ItemType.ENCHANTED_COOKED_MUTTON, EnchantedCookedMutton.class),
    ENCHANTED_RAW_SALMON(ItemType.ENCHANTED_RAW_SALMON, EnchantedRawSalmon.class),
    ENCHANTED_COOKED_SALMON(ItemType.ENCHANTED_COOKED_SALMON, EnchantedCookedSalmon.class),
    ENCHANTED_COOKIE(ItemType.ENCHANTED_COOKIE, EnchantedCookie.class),
    ENCHANTED_DANDELION(ItemType.ENCHANTED_DANDELION, EnchantedDandelion.class),
    ENCHANTED_FEATHER(ItemType.ENCHANTED_FEATHER, EnchantedFeather.class),
    ENCHANTED_SPIDER_EYE(ItemType.ENCHANTED_SPIDER_EYE, EnchantedSpiderEye.class),
    ENCHANTED_FERMENTED_SPIDER_EYE(ItemType.ENCHANTED_FERMENTED_SPIDER_EYE, EnchantedFermentedSpiderEye.class),
    ENCHANTED_FIREWORK_ROCKET(ItemType.ENCHANTED_FIREWORK_ROCKET, EnchantedFireworkRocket.class),
    ENCHANTED_FLINT(ItemType.ENCHANTED_FLINT, EnchantedFlint.class),
    ENCHANTED_GHAST_TEAR(ItemType.ENCHANTED_GHAST_TEAR, EnchantedGhastTear.class),
    ENCHANTED_GLISTERING_MELON(ItemType.ENCHANTED_GLISTERING_MELON, EnchantedGlisteringMelon.class),
    ENCHANTED_GLOWSTONE_DUST(ItemType.ENCHANTED_GLOWSTONE_DUST, EnchantedGlowstoneDust.class),
    ENCHANTED_GLOWSTONE(ItemType.ENCHANTED_GLOWSTONE, EnchantedGlowstone.class),
    ENCHANTED_GOLDEN_CARROT(ItemType.ENCHANTED_GOLDEN_CARROT, EnchantedGoldenCarrot.class),
    ENCHANTED_PORK(ItemType.ENCHANTED_PORK, EnchantedPork.class),
    ENCHANTED_GRILLED_PORK(ItemType.ENCHANTED_GRILLED_PORK, EnchantedGrilledPork.class),
    ENCHANTED_HARD_STONE(ItemType.ENCHANTED_HARD_STONE, EnchantedHardstone.class),
    ENCHANTED_HAY_BALE(ItemType.ENCHANTED_HAY_BALE, EnchantedHayBale.class),
    ENCHANTED_ANCIENT_CLAW(ItemType.ENCHANTED_ANCIENT_CLAW, EnchantedAncientClaw.class),
    ENCHANTED_COAL_BLOCK(ItemType.ENCHANTED_COAL_BLOCK, EnchantedCoalBlock.class),
    ENCHANTED_BONE_BLOCK(ItemType.ENCHANTED_BONE_BLOCK, EnchantedBoneBlock.class),
    ENCHANTED_BREAD(ItemType.ENCHANTED_BREAD, EnchantedBread.class),
    ENCHANTED_RED_MUSHROOM_BLOCK(ItemType.ENCHANTED_RED_MUSHROOM_BLOCK, EnchantedRedMushroomBlock.class),
    ENCHANTED_INK_SAC(ItemType.ENCHANTED_INK_SAC, EnchantedInkSac.class),
    ENCHANTED_LAPIS_LAZULI(ItemType.ENCHANTED_LAPIS_LAZULI, EnchantedLapisLazuli.class),
    ENCHANTED_LAPIS_LAZULI_BLOCK(ItemType.ENCHANTED_LAPIS_LAZULI_BLOCK, EnchantedLapisLazuliBlock.class),
    ENCHANTED_LILY_PAD(ItemType.ENCHANTED_LILY_PAD, EnchantedLilyPad.class),
    ENCHANTED_MELON(ItemType.ENCHANTED_MELON, EnchantedMelon.class),
    ENCHANTED_MELON_BLOCK(ItemType.ENCHANTED_MELON_BLOCK, EnchantedMelonBlock.class),
    ENCHANTED_MITHRIL(ItemType.ENCHANTED_MITHRIL, EnchantedMithril.class),
    ENCHANTED_MYCELIUM(ItemType.ENCHANTED_MYCELIUM, EnchantedMycelium.class),
    ENCHANTED_MYCELIUM_CUBE(ItemType.ENCHANTED_MYCELIUM_CUBE, EnchantedMyceliumCube.class),
    ENCHANTED_NETHER_WART(ItemType.ENCHANTED_NETHER_WART, EnchantedNetherWart.class),
    ENCHANTED_NETHERRACK(ItemType.ENCHANTED_NETHERRACK, EnchantedNetherrack.class),
    ENCHANTED_PAPER(ItemType.ENCHANTED_PAPER, EnchantedPaper.class),
    ENCHANTED_POISONOUS_POTATO(ItemType.ENCHANTED_POISONOUS_POTATO, EnchantedPoisonousPotato.class),
    ENCHANTED_POPPY(ItemType.ENCHANTED_POPPY, EnchantedPoppy.class),
    ENCHANTED_PRISMARINE_CRYSTALS(ItemType.ENCHANTED_PRISMARINE_CRYSTALS, EnchantedPrismarineCrystals.class),
    ENCHANTED_PRISMARINE_SHARD(ItemType.ENCHANTED_PRISMARINE_SHARD, EnchantedPrismarineShard.class),
    ENCHANTED_PUFFERFISH(ItemType.ENCHANTED_PUFFERFISH, EnchantedPufferfish.class),
    ENCHANTED_QUARTZ(ItemType.ENCHANTED_QUARTZ, EnchantedQuartz.class),
    ENCHANTED_QUARTZ_BLOCK(ItemType.ENCHANTED_QUARTZ_BLOCK, EnchantedQuartzBlock.class),
    ENCHANTED_RABBIT_FOOT(ItemType.ENCHANTED_RABBIT_FOOT, EnchantedRabbitFoot.class),
    ENCHANTED_RABBIT_HIDE(ItemType.ENCHANTED_RABBIT_HIDE, EnchantedRabbitHide.class),
    ENCHANTED_RAW_BEEF(ItemType.ENCHANTED_RAW_BEEF, EnchantedRawBeef.class),
    ENCHANTED_RAW_CHICKEN(ItemType.ENCHANTED_RAW_CHICKEN, EnchantedRawChicken.class),
    ENCHANTED_RAW_RABBIT(ItemType.ENCHANTED_RAW_RABBIT, EnchantedRawRabbit.class),
    ENCHANTED_SAND(ItemType.ENCHANTED_SAND, EnchantedSand.class),
    ENCHANTED_SEEDS(ItemType.ENCHANTED_SEEDS, EnchantedSeeds.class),
    ENCHANTED_SLIMEBALL(ItemType.ENCHANTED_SLIMEBALL, EnchantedSlimeball.class),
    ENCHANTED_SLIME_BLOCK(ItemType.ENCHANTED_SLIME_BLOCK, EnchantedSlimeBlock.class),
    ENCHANTED_SUGAR_CANE(ItemType.ENCHANTED_SUGAR_CANE, EnchantedSugarCane.class),
    ENCHANTED_SULPHUR(ItemType.ENCHANTED_SULPHUR, EnchantedSulphur.class),
    ENCHANTED_SULPHUR_CUBE(ItemType.ENCHANTED_SULPHUR_CUBE, EnchantedSulphurCube.class),
    ENCHANTED_TITANIUM(ItemType.ENCHANTED_TITANIUM, EnchantedTitanium.class),
    ENCHANTED_WET_SPONGE(ItemType.ENCHANTED_WET_SPONGE, EnchantedWetSponge.class),
    ENCHANTED_WOOL(ItemType.ENCHANTED_WOOL, EnchantedWool.class),
    ENCHANTED_SHARK_FIN(ItemType.ENCHANTED_SHARK_FIN, EnchantedSharkFin.class),
    ENCHANTED_RED_SAND(ItemType.ENCHANTED_RED_SAND, EnchantedRedSand.class),
    ENCHANTED_RED_SAND_CUBE(ItemType.ENCHANTED_RED_SAND_CUBE, EnchantedRedSandCube.class),
    ENCHANTED_BONE_MEAL(ItemType.ENCHANTED_BONE_MEAL, EnchantedBoneMeal.class),
    ;

    public final ItemType type;
    public final Class<? extends CustomSkyBlockItem> clazz;

    ItemTypeLinker(ItemType type, Class<? extends CustomSkyBlockItem> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    @SneakyThrows
    public String getDisplayName(@Nullable SkyBlockItem item) {
        if (clazz == null)
            return StringUtility.toNormalCase(this.name());
        if (clazz.newInstance() instanceof CustomDisplayName name)
            return name.getDisplayName(item);
        return StringUtility.toNormalCase(this.name());
    }

    @SneakyThrows
    public <T extends CustomSkyBlockItem> T getNewInstance(Class<T> toCastTo) {
        if (clazz == null)
            return null;
        return toCastTo.cast(clazz.newInstance());
    }

    public static ItemTypeLinker get(String name) {
        try {
            return ItemTypeLinker.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasLinker(ItemType type) {
        return fromType(type) != null;
    }

    public static @Nullable ItemTypeLinker fromType(ItemType type) {
        for (ItemTypeLinker linker : values()) {
            if (linker.type == type)
                return linker;
        }
        return null;
    }

    public static @Nullable ItemTypeLinker fromMaterial(Material material) {
        return new SkyBlockItem(material).getAttributeHandler().getPotentialClassLinker();
    }

    public static boolean isVanillaReplaced(String item) {
        return get(item) != null;
    }
}
