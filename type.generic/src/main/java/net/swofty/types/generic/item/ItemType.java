package net.swofty.types.generic.item;

import lombok.SneakyThrows;
import net.minestom.server.item.Material;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.items.SandboxItem;
import net.swofty.types.generic.item.items.accessories.*;
import net.swofty.types.generic.item.items.accessories.abicases.*;
import net.swofty.types.generic.item.items.accessories.dungeon.*;
import net.swofty.types.generic.item.items.accessories.spider.SpiderArtifact;
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
import net.swofty.types.generic.item.items.minion.farming.WheatMinion;
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
import net.swofty.types.generic.item.items.pet.BeePet;
import net.swofty.types.generic.item.items.pet.petitems.*;
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
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

public enum ItemType {
    SANDBOX_ITEM(Material.BLAZE_POWDER, Rarity.COMMON, SandboxItem.class),

    /**
     * Miscellaneous
     */
    ENCHANTED_BOOK(Material.ENCHANTED_BOOK, Rarity.UNCOMMON, EnchantedBook.class),
    SKYBLOCK_MENU(Material.NETHER_STAR, Rarity.COMMON, SkyBlockMenu.class),
    MOVE_JERRY(Material.VILLAGER_SPAWN_EGG, Rarity.COMMON, MoveJerry.class),
    HOT_POTATO_BOOK(Material.BOOK, Rarity.EPIC, HotPotatoBook.class),
    GAME_BREAKER(Material.TNT, Rarity.SPECIAL, GameBreaker.class),
    GAME_ANNIHILATOR(Material.PLAYER_HEAD, Rarity.SPECIAL, GameAnnihilator.class),
    QUALITY_MAP(Material.MAP, Rarity.SPECIAL, QualityMap.class),
    DEAD_BUSH_OF_LOVE(Material.DEAD_BUSH, Rarity.SPECIAL, DeadBushofLove.class),

    /**
     * Accessories
     */
    ZOMBIE_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, ZombieTalisman.class),
    ZOMBIE_RING(Material.PLAYER_HEAD, Rarity.UNCOMMON, ZombieRing.class),
    ZOMBIE_ARTIFACT(Material.PLAYER_HEAD, Rarity.RARE, ZombieArtifact.class),
    SPEED_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, SpeedTalisman.class),
    SKELETON_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, SkeletonTalisman.class),
    HASTE_RING(Material.PLAYER_HEAD, Rarity.RARE, HasteRing.class),
    FARMING_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, FarmingTalisman.class),
    LAVA_TALISMAN(Material.PLAYER_HEAD, Rarity.UNCOMMON, LavaTalisman.class),
    POTATO_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, PotatoTalisman.class),
    TALISMAN_OF_POWER(Material.PLAYER_HEAD, Rarity.COMMON, TalismanOfPower.class),
    BAT_TALISMAN(Material.PLAYER_HEAD, Rarity.RARE, BatTalisman.class),
    FIRE_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, FireTalisman.class),
    AUTO_RECOMBOBULATOR(Material.PLAYER_HEAD, Rarity.LEGENDARY, AutoRecombobulator.class),
    CATACOMBS_EXPERT_RING(Material.PLAYER_HEAD, Rarity.EPIC, CatacombsExpertRing.class),
    WITHER_RELIC(Material.PLAYER_HEAD, Rarity.LEGENDARY, WitherRelic.class),
    SCARFS_GRIMOIRE(Material.PLAYER_HEAD, Rarity.LEGENDARY, ScarfsGrimoire.class),
    SCARFS_THESIS(Material.PLAYER_HEAD, Rarity.EPIC, ScarfsThesis.class),
    SCARFS_STUDIES(Material.PLAYER_HEAD, Rarity.RARE, ScarfsStudies.class),
    TREASURE_ARTIFACT(Material.PLAYER_HEAD, Rarity.LEGENDARY, TreasureArtifact.class),
    TREASURE_RING(Material.PLAYER_HEAD, Rarity.EPIC, TreasureRing.class),
    TREASURE_TALISMAN(Material.PLAYER_HEAD, Rarity.RARE, TreasureTalisman.class),
    SPIDER_TALISMAN(Material.PLAYER_HEAD, Rarity.UNCOMMON, SpiderTalisman.class),
    SPIDER_ARTIFACT(Material.PLAYER_HEAD, Rarity.EPIC, SpiderArtifact.class),
    MINE_AFFINITY_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, MineAffinityTalisman.class),
    VILLAGE_AFFINITY_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, VillageAffinityTalisman.class),
    BITS_TALISMAN(Material.PLAYER_HEAD, Rarity.RARE, BitsTalisman.class),
    ACTUALLY_BLUE_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, ActuallyBlueAbicase.class),
    BLUE_BUT_GREEN_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, BlueButGreenAbicase.class),
    BLUE_BUT_RED_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, BlueButRedAbicase.class),
    BLUE_BUT_YELLOW_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, BlueButYellowAbicase.class),
    LIGHTER_BLUE_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, LighterBlueAbicase.class),
    REZAR_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, RezarAbicase.class),
    SUMSUNG_G3_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, SumsungG3Abicase.class),
    SUMSUNG_GG_ABICASE(Material.PLAYER_HEAD, Rarity.RARE, SumsungGGAbicase.class),

    /**
     * Community Shop Items
     */
    BOOSTER_COOKIE(Material.COOKIE, Rarity.LEGENDARY, BoosterCookie.class),
    GOD_POTION(Material.PLAYER_HEAD, Rarity.SPECIAL, GodPotion.class),
    KAT_FLOWER(Material.POPPY, Rarity.SPECIAL, KatFlower.class),
    KAT_BOUQUET(Material.ROSE_BUSH, Rarity.SPECIAL, KatBouquet.class),
    HYPER_CATALYST_UPGRADER(Material.PLAYER_HEAD, Rarity.SPECIAL, HyperCatalystUpgrader.class),
    ULTIMATE_CARROT_CANDY_UPGRADE(Material.PLAYER_HEAD, Rarity.SPECIAL, UltimateCarrotCandyUpgrade.class),
    COLOSSAL_EXPERIENCE_BOTTLE_UPGRADE(Material.PLAYER_HEAD, Rarity.SPECIAL, ColossalExperienceBottleUpgrade.class),
    MINION_STORAGE_EXPANDER(Material.PLAYER_HEAD, Rarity.SPECIAL, MinionStorageExpander.class),
    MATRIARCHS_PERFUME(Material.RABBIT_STEW, Rarity.SPECIAL, MatriarchsPerfume.class),
    HOLOGRAM(Material.PLAYER_HEAD, Rarity.LEGENDARY, Hologram.class),
    DITTO_BLOB(Material.PLAYER_HEAD, Rarity.LEGENDARY, DittoBlob.class),
    BUILDERS_WAND(Material.BLAZE_ROD, Rarity.LEGENDARY, BuildersWand.class),
    BLOCK_ZAPPER(Material.FLINT, Rarity.EPIC, BlockZapper.class),
    PORTALIZER(Material.PLAYER_HEAD, Rarity.SPECIAL, Portalizer.class),
    AUTOPET_RULES_2_PACK(Material.PLAYER_HEAD, Rarity.SPECIAL, AutopetRules2Pack.class),
    POCKET_SACK_IN_A_SACK(Material.PLAYER_HEAD, Rarity.SPECIAL, PocketSackInASack.class),
    DUNGEON_SACK(Material.PLAYER_HEAD, Rarity.LEGENDARY, DungeonSack.class),
    RUNE_SACK(Material.PLAYER_HEAD, Rarity.EPIC, RuneSack.class),
    FLOWER_SACK(Material.PLAYER_HEAD, Rarity.EPIC, FlowerSack.class),
    DWARVEN_SACK(Material.PLAYER_HEAD, Rarity.EPIC, DwarvenSack.class),
    CRYSTAL_HOLLOWS_SACK(Material.PLAYER_HEAD, Rarity.EPIC, CrystalHollowsSack.class),
    ABIPHONE_CONTACTS_TRIO(Material.COMPARATOR, Rarity.SPECIAL, AbiphoneContactsTrio.class),
    PURE_WHITE_DYE(Material.BONE_MEAL, Rarity.EPIC, PureWhiteDye.class),
    PURE_BLACK_DYE(Material.INK_SAC, Rarity.EPIC, PureBlackDye.class),
    ACCESSORY_ENRICHMENT_SWAPPER(Material.COMPARATOR, Rarity.SPECIAL, AccessoryEnrichmentSwapper.class),
    ATTACK_SPEED_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, AttackSpeedEnrichment.class),
    CRITICAL_CHANCE_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, CriticalChanceEnrichment.class),
    CRITICAL_DAMAGE_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, CriticalDamageEnrichment.class),
    DEFENSE_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, DefenseEnrichment.class),
    FEROCITY_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, FerocityEnrichment.class),
    HEALTH_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, HealthEnrichment.class),
    INTELLIGENCE_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, IntelligenceEnrichment.class),
    MAGIC_FIND_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, MagicFindEnrichment.class),
    SEA_CREATURE_CHANCE_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, SeaCreatureChanceEnrichment.class),
    SPEED_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, SpeedEnrichment.class),
    STRENGTH_ENRICHMENT(Material.PLAYER_HEAD, Rarity.SPECIAL, StrengthEnrichment.class),
    CHAMPION(Material.ENCHANTED_BOOK, Rarity.COMMON, Champion.class),
    COMPACT(Material.ENCHANTED_BOOK, Rarity.COMMON, Compact.class),
    CULTIVATING(Material.ENCHANTED_BOOK, Rarity.COMMON, Cultivating.class),
    EXPERTISE(Material.ENCHANTED_BOOK, Rarity.COMMON, Expertise.class),
    HECATOMB(Material.ENCHANTED_BOOK, Rarity.COMMON, Hecatomb.class),
    INFERNO_FUE_BLOCK(Material.PLAYER_HEAD, Rarity.RARE, InfernoFuelBlock.class),

    /**
     * Runes
     */
    BLOOD_RUNE(Material.PLAYER_HEAD, Rarity.COMMON, BloodRune.class),

    /**
     * Minions
     */
    COBBLESTONE_MINION(Material.PLAYER_HEAD, Rarity.RARE, CobblestoneMinion.class),
    COAL_MINION(Material.PLAYER_HEAD, Rarity.RARE, CoalMinion.class),
    SNOW_MINION(Material.PLAYER_HEAD, Rarity.RARE, SnowMinion.class),
    ACACIA_MINION(Material.PLAYER_HEAD, Rarity.RARE, AcaciaMinion.class),
    BIRCH_MINION(Material.PLAYER_HEAD, Rarity.RARE, BirchMinion.class),
    DARK_OAK_MINION(Material.PLAYER_HEAD, Rarity.RARE, DarkOakMinion.class),
    JUNGLE_MINION(Material.PLAYER_HEAD, Rarity.RARE, JungleMinion.class),
    OAK_MINION(Material.PLAYER_HEAD, Rarity.RARE, OakMinion.class),
    DIAMOND_MINION(Material.PLAYER_HEAD, Rarity.RARE, DiamondMinion.class),
    EMERALD_MINION(Material.PLAYER_HEAD, Rarity.RARE, EmeraldMinion.class),
    IRON_MINION(Material.PLAYER_HEAD, Rarity.RARE, IronMinion.class),
    GOLD_MINION(Material.PLAYER_HEAD, Rarity.RARE, GoldMinion.class),
    LAPIS_MINION(Material.PLAYER_HEAD, Rarity.RARE, LapisMinion.class),
    REDSTONE_MINION(Material.PLAYER_HEAD, Rarity.RARE, RedstoneMinion.class),
    ENDSTONE_MINION(Material.PLAYER_HEAD, Rarity.RARE, EndstoneMinion.class),
    ICE_MINION(Material.PLAYER_HEAD, Rarity.RARE, IceMinion.class),
    QUARTZ_MINION(Material.PLAYER_HEAD, Rarity.RARE, QuartzMinion.class),
    OBSIDIAN_MINION(Material.PLAYER_HEAD, Rarity.RARE, ObsidianMinion.class),
    SAND_MINION(Material.PLAYER_HEAD, Rarity.RARE, SandMinion.class),
    GRAVEL_MINION(Material.PLAYER_HEAD, Rarity.RARE, GravelMinion.class),
    SPRUCE_MINION(Material.PLAYER_HEAD, Rarity.RARE, SpruceMinion.class),
    WHEAT_MINION(Material.PLAYER_HEAD, Rarity.RARE, WheatMinion.class),

    /**
    * Minion Upgrades
    */
    AUTO_SMELTER(Material.FURNACE, Rarity.COMMON, AutoSmelter.class),
    COMPACTOR(Material.DISPENSER, Rarity.UNCOMMON, Compactor.class),
    SUPER_COMPACTOR_3000(Material.DISPENSER, Rarity.RARE, SuperCompactor3000.class),
    BUDGET_HOPPER(Material.HOPPER, Rarity.UNCOMMON, BudgetHopper.class),
    ENCHANTED_HOPPER(Material.HOPPER, Rarity.RARE, EnchantedHopper.class),
    FLY_CATCHER(Material.COBWEB, Rarity.EPIC, FlyCatcher.class),
    DIAMOND_SPREADING(Material.DIAMOND, Rarity.RARE, DiamondSpreading.class),
    MINION_EXPANDER(Material.COMMAND_BLOCK, Rarity.RARE, MinionExpander.class),
    MITHRIL_INFUSION(Material.PLAYER_HEAD, Rarity.UNCOMMON, MithrilInfusion.class),

    /**
     * Minion Fuels
     */
    ENCHANTED_LAVA_BUCKET(Material.LAVA_BUCKET, Rarity.RARE, EnchantedLavaBucket.class),
    MAGMA_BUCKET(Material.LAVA_BUCKET, Rarity.LEGENDARY, MagmaBucket.class),
    PLASMA_BUCKET(Material.LAVA_BUCKET, Rarity.LEGENDARY, PlasmaBucket.class),
    EVERBURNING_FLAME(Material.PLAYER_HEAD, Rarity.EPIC, EverburningFlame.class),
    HEAT_CORE(Material.PLAYER_HEAD, Rarity.SPECIAL, HeatCore.class),

    /**
     * Minion Skins
     */
    BEE_MINION_SKIN(Material.PLAYER_HEAD, Rarity.COMMON, BeeMinionSkin.class),

    /**
     * Pets
     */
    BEE_PET(Material.PLAYER_HEAD, Rarity.COMMON, BeePet.class),

    /**
     * Pet Items
     */
    ALL_SKILLS_EXP_BOOST(Material.DIAMOND, Rarity.COMMON, AllSkillsExpBoost.class),
    MINING_EXP_BOOST_COMMON(Material.IRON_PICKAXE, Rarity.COMMON, MiningExpBoostCommon.class),
    MINING_EXP_BOOST_RARE(Material.IRON_PICKAXE, Rarity.RARE, MiningExpBoostRare.class),
    FARMING_EXP_BOOST_COMMON(Material.IRON_HOE, Rarity.COMMON, FarmingExpBoostCommon.class),
    FARMING_EXP_BOOST_RARE(Material.IRON_HOE, Rarity.RARE, FarmingExpBoostRare.class),
    FISHING_EXP_BOOST(Material.COD, Rarity.COMMON, FishingExpBoost.class),
    FORAGING_EXP_BOOST(Material.IRON_AXE, Rarity.COMMON, ForagingExpBoost.class),
    COMBAT_EXP_BOOST(Material.IRON_SWORD, Rarity.COMMON, CombatExpBoost.class),
    BIG_TEETH(Material.GHAST_TEAR, Rarity.COMMON, BigTeeth.class),
    IRON_CLAWS(Material.IRON_INGOT, Rarity.COMMON, IronClaws.class),
    HARDENED_SCALES(Material.PRISMARINE_CRYSTALS, Rarity.UNCOMMON, HardenedScales.class),
    SHARPENED_CLAWS(Material.PRISMARINE_SHARD, Rarity.UNCOMMON, SharpenedClaws.class),
    BUBBLEGUM(Material.PLAYER_HEAD, Rarity.RARE, Bubblegum.class),

    /**
     * Backpacks
     */
    SMALL_BACKPACK(Material.PLAYER_HEAD, Rarity.UNCOMMON, SmallBackpack.class),
    MEDIUM_BACKPACK(Material.PLAYER_HEAD, Rarity.RARE, MediumBackpack.class),
    LARGE_BACKPACK(Material.PLAYER_HEAD, Rarity.EPIC, LargeBackpack.class),
    GREATER_BACKPACK(Material.PLAYER_HEAD, Rarity.EPIC, GreaterBackpack.class),
    JUMBO_BACKPACK(Material.PLAYER_HEAD, Rarity.LEGENDARY, JumboBackpack.class),
    JUMBO_BACKPACK_UPGRADE(Material.PLAYER_HEAD, Rarity.SPECIAL, JumboBackpackUpgrade.class),

    /**
     * Decoration items
     */
    DECORATION_ANCIENT_FRUIT(Material.PLAYER_HEAD, Rarity.COMMON, AncientFruit.class),
    DECORATION_APPALLED_PUMPKIN(Material.PLAYER_HEAD, Rarity.COMMON, AppalledPumpkin.class),
    DECORATION_APPLE(Material.PLAYER_HEAD, Rarity.COMMON, Apple.class),
    DECORATION_BANANA_BUNCH(Material.PLAYER_HEAD, Rarity.COMMON, BananaBunch.class),
    DECORATION_BEETROOT(Material.PLAYER_HEAD, Rarity.COMMON, Beetroot.class),
    DECORATION_BERRY(Material.PLAYER_HEAD, Rarity.COMMON, Berry.class),
    DECORATION_BERRY_BUSH(Material.PLAYER_HEAD, Rarity.COMMON, BerryBush.class),
    DECORATION_BLUE_CORN(Material.PLAYER_HEAD, Rarity.COMMON, BlueCorn.class),
    DECORATION_BROWN_MUSHROOM(Material.PLAYER_HEAD, Rarity.COMMON, BrownMushroom.class),
    DECORATION_BUSH(Material.PLAYER_HEAD, Rarity.COMMON, Bush.class),
    DECORATION_CACTUS(Material.PLAYER_HEAD, Rarity.COMMON, Cactus.class),
    DECORATION_CHESTO_BERRY(Material.PLAYER_HEAD, Rarity.COMMON, ChestoBerry.class),
    DECORATION_CORN(Material.PLAYER_HEAD, Rarity.COMMON, Corn.class),
    DECORATION_KIWI(Material.PLAYER_HEAD, Rarity.COMMON, Kiwi.class),
    DECORATION_LEMON(Material.PLAYER_HEAD, Rarity.COMMON, Lemon.class),
    DECORATION_LETTUCE(Material.PLAYER_HEAD, Rarity.COMMON, Lettuce.class),
    DECORATION_LILAC_FRUIT(Material.PLAYER_HEAD, Rarity.COMMON, LilacFruit.class),
    DECORATION_MELON(Material.PLAYER_HEAD, Rarity.COMMON, Melon.class),
    DECORATION_ONION(Material.PLAYER_HEAD, Rarity.COMMON, Onion.class),
    DECORATION_ORANGE(Material.PLAYER_HEAD, Rarity.COMMON, Orange.class),
    DECORATION_PINK_BERRY(Material.PLAYER_HEAD, Rarity.COMMON, PinkBerry.class),

    /**
     * Farming Props
     */
    ROOKIE_HOE(Material.STONE_HOE, Rarity.COMMON, RookieHoe.class),
    WOODEN_HOE(Material.WOODEN_HOE, Rarity.COMMON, WoodenHoe.class),
    STONE_HOE(Material.STONE_HOE, Rarity.COMMON, StoneHoe.class),
    GOLDEN_HOE(Material.GOLDEN_HOE, Rarity.COMMON, GoldenHoe.class),
    IRON_HOE(Material.IRON_HOE, Rarity.COMMON, IronHoe.class),
    DIAMOND_HOE(Material.DIAMOND_HOE, Rarity.UNCOMMON, DiamondHoe.class),
    WHEAT(Material.WHEAT, Rarity.COMMON, Wheat.class),
    WHEAT_CRYSTAL(Material.PLAYER_HEAD, Rarity.SPECIAL, WheatCrystal.class),
    FLOWER_CRYSTAL(Material.PLAYER_HEAD, Rarity.SPECIAL, FlowerCrystal.class),
    CARROT_CRYSTAL(Material.PLAYER_HEAD, Rarity.SPECIAL, CarrotCrystal.class),
    POTATO_CRYSTAL(Material.PLAYER_HEAD, Rarity.SPECIAL, PotatoCrystal.class),
    PUMPKIN_AND_MELON_CRYSTAL(Material.PLAYER_HEAD, Rarity.SPECIAL, PumpkinAndMelonCrystal.class),
    MUTANT_NETHER_WART(Material.PLAYER_HEAD, Rarity.RARE, MutantNetherWart.class),
    POLISHED_PUMPKIN(Material.PLAYER_HEAD, Rarity.RARE, PolishedPumpkin.class),
    BOX_OF_SEEDS(Material.PLAYER_HEAD, Rarity.RARE, BoxOfSeeds.class),
    ENCHANTED_BOOKSHELF(Material.BOOKSHELF, Rarity.UNCOMMON, EnchantedBookshelf.class),
    TIGHTLY_TIED_HAY_BALE(Material.PLAYER_HEAD, Rarity.RARE, TightlyTiedHayBale.class),
    COMPOST(Material.PLAYER_HEAD, Rarity.UNCOMMON, Compost.class),
    COMPOST_BUNDLE(Material.PLAYER_HEAD, Rarity.RARE, CompostBundle.class),
    DUNG(Material.BROWN_WOOL, Rarity.UNCOMMON, Dung.class),
    HONEY_JAR(Material.PLAYER_HEAD, Rarity.UNCOMMON, HoneyJar.class),
    PLANT_MATTER(Material.OAK_LEAVES, Rarity.UNCOMMON, PlantMatter.class),

    /**
     * Foraging Props
     */
    WOODEN_AXE(Material.WOODEN_AXE, Rarity.COMMON, WoodenAxe.class),
    STONE_AXE(Material.STONE_AXE, Rarity.COMMON, StoneAxe.class),
    GOLDEN_AXE(Material.GOLDEN_AXE, Rarity.COMMON, GoldenAxe.class),
    IRON_AXE(Material.IRON_AXE, Rarity.COMMON, IronAxe.class),
    DIAMOND_AXE(Material.DIAMOND_AXE, Rarity.UNCOMMON, DiamondAxe.class),
    ROOKIE_AXE(Material.STONE_AXE, Rarity.COMMON, RookieAxe.class),
    PROMISING_AXE(Material.IRON_AXE, Rarity.UNCOMMON, PromisingAxe.class),
    SWEET_AXE(Material.IRON_AXE, Rarity.UNCOMMON, SweetAxe.class),
    EFFICIENT_AXE(Material.IRON_AXE, Rarity.UNCOMMON, EfficientAxe.class),

    /**
     * Fishing Props
     */
    SHARK_FIN(Material.PRISMARINE_SHARD, Rarity.RARE, SharkFin.class),
    NURSE_SHARK_TOOTH(Material.GHAST_TEAR, Rarity.UNCOMMON, NurseSharkTooth.class),
    BLUE_SHARK_TOOTH(Material.GHAST_TEAR, Rarity.RARE, BlueSharkTooth.class),
    TIGER_SHARK_TOOTH(Material.GHAST_TEAR, Rarity.EPIC, TigerSharkTooth.class),
    GREAT_WHITE_SHARK_TOOTH(Material.GHAST_TEAR, Rarity.LEGENDARY, GreatWhiteSharkTooth.class),
    MAGMAFISH(Material.PLAYER_HEAD, Rarity.RARE, Magmafish.class),
    SILVER_MAGMAFISH(Material.PLAYER_HEAD, Rarity.EPIC, SilverMagmafish.class),
    GOLD_MAGMAFISH(Material.PLAYER_HEAD, Rarity.LEGENDARY, GoldMagmafish.class),
    DIAMOND_MAGMAFISH(Material.PLAYER_HEAD, Rarity.MYTHIC, DiamondMagmafish.class),
    FISHING_ROD(Material.FISHING_ROD, Rarity.COMMON, FishingRod.class),

    /**
     * Combat Props
     */
    CHILI_PEPPER(Material.PLAYER_HEAD, Rarity.UNCOMMON, ChiliPepper.class),
    STUFFED_CHILI_PEPPER(Material.PLAYER_HEAD, Rarity.RARE, StuffedChiliPepper.class),
    ABSOLUTE_ENDER_PEARL(Material.PLAYER_HEAD, Rarity.RARE, AbsoluteEnderPearl.class),
    WHIPPED_MAGMA_CREAM(Material.PLAYER_HEAD, Rarity.RARE, WhippedMagmaCream.class),
    ZOMBIE_HEART(Material.PLAYER_HEAD, Rarity.RARE, ZombieHeart.class),
    SOUL_STRING(Material.STRING, Rarity.RARE, SoulString.class),

    /**
     * Zombie Slayer
     */
    FOUL_FLESH(Material.CHARCOAL, Rarity.RARE, FoulFlesh.class),
    REVENANT_FLESH(Material.ROTTEN_FLESH, Rarity.UNCOMMON, RevenantFlesh.class),
    UNDEAD_CATALYST(Material.PLAYER_HEAD, Rarity.RARE, UndeadCatalyst.class),
    BEHEADED_HORROR(Material.PLAYER_HEAD, Rarity.EPIC, BeheadedHorror.class),
    REVENANT_CATALYST(Material.PLAYER_HEAD, Rarity.EPIC, RevenantCatalyst.class),
    SCYTHE_BLADE(Material.DIAMOND, Rarity.LEGENDARY, ScytheBlade.class),
    SHARD_OF_THE_SHREDDED(Material.PLAYER_HEAD, Rarity.LEGENDARY, ShardOfTheShredded.class),
    WARDEN_HEART(Material.PLAYER_HEAD, Rarity.LEGENDARY, WardenHeart.class),
    REVENANT_VISCERA(Material.COOKED_PORKCHOP, Rarity.RARE, RevenantViscera.class),
    CRYSTALLIZED_HEART(Material.PLAYER_HEAD, Rarity.RARE, CrystallizedHeart.class),
    REVIVED_HEART(Material.PLAYER_HEAD, Rarity.EPIC, RevivedHeart.class),
    REAPER_MASK(Material.PLAYER_HEAD, Rarity.LEGENDARY, ReaperMask.class),
    WARDEN_HELMET(Material.PLAYER_HEAD, Rarity.LEGENDARY, WardenHelmet.class),
    REVENANT_FALCHION(Material.DIAMOND_SWORD, Rarity.RARE, RevenantFalchion.class),
    REAPER_FALCHION(Material.DIAMOND_SWORD, Rarity.EPIC, ReaperFalchion.class),
    AXE_OF_THE_SHREDDED(Material.DIAMOND_AXE, Rarity.LEGENDARY, AxeOfTheShreeded.class),
    REAPER_SCYTHE(Material.DIAMOND_HOE, Rarity.LEGENDARY, ReaperScythe.class),

    /**
     * Spider Slayer
     */
    TARANTULA_WEB(Material.STRING, Rarity.UNCOMMON, TarantulaWeb.class),
    TOXIC_ARROW_POISON(Material.LIME_DYE, Rarity.UNCOMMON, ToxicArrowPoison.class),
    SPIDER_CATALYST(Material.PLAYER_HEAD, Rarity.RARE, SpiderCatalyst.class),
    FLY_SWATTER(Material.GOLDEN_SHOVEL, Rarity.EPIC, FlySwatter.class),
    DIGESTED_MOSQUITO(Material.ROTTEN_FLESH, Rarity.LEGENDARY, DigestedMosquito.class),
    TARANTULA_SILK(Material.COBWEB, Rarity.RARE, TarantulaSilk.class),
    RECLUSE_FANG(Material.IRON_SWORD, Rarity.RARE, RecluseFang.class),
    MOSQUITO_BOW(Material.BOW, Rarity.LEGENDARY, MosquitoBow.class),
    SCORPION_BOW(Material.BOW, Rarity.EPIC, ScorpionBow.class),
    SCORPION_FOIL(Material.WOODEN_SWORD, Rarity.EPIC, ScorpionFoil.class),

    /**
     * Wolf Slayer
     */
    WOLF_TOOTH(Material.GHAST_TEAR, Rarity.UNCOMMON, WolfTooth.class),
    HAMSTER_WHEEL(Material.OAK_TRAPDOOR, Rarity.RARE, HamsterWheel.class),
    RED_CLAW_EGG(Material.MOOSHROOM_SPAWN_EGG, Rarity.EPIC, RedClawEgg.class),
    OVERFLUX_CAPACITOR(Material.QUARTZ, Rarity.EPIC, OverfluxCapacitor.class),
    GRIZZLY_SALMON(Material.COOKED_SALMON, Rarity.RARE, GrizzlySalmon.class),
    GOLDEN_TOOTH(Material.GOLD_NUGGET, Rarity.RARE, GoldenTooth.class),
    SHAMAN_SWORD(Material.IRON_SWORD, Rarity.EPIC, ShamanSword.class),
    POOCH_SWORD(Material.GOLDEN_SWORD, Rarity.LEGENDARY, PoochSword.class),
    EDIBLE_MACE(Material.MUTTON, Rarity.RARE, EdibleMace.class),
    WEIRD_TUBA(Material.HOPPER, Rarity.RARE, WeirdTuba.class),

    /**
     * Enderman Slayer
     */
    NULL_SPHERE(Material.FIREWORK_STAR, Rarity.UNCOMMON, NullSphere.class),
    TWILIGHT_ARROW_POISON(Material.PURPLE_DYE, Rarity.UNCOMMON, TwilightArrowPoison.class),
    SUMMONING_EYE(Material.PLAYER_HEAD, Rarity.EPIC, SummoningEye.class),
    TRANSMISSION_TUNER(Material.PLAYER_HEAD, Rarity.EPIC, TransmissionTuner.class),
    NULL_ATOM(Material.OAK_BUTTON, Rarity.RARE, NullAtom.class),
    SINFUL_DICE(Material.PLAYER_HEAD, Rarity.EPIC, SinfulDice.class),
    EXCEEDINGLY_RARE_ENDER_ARTIFACT_UPGRADER(Material.PLAYER_HEAD, Rarity.LEGENDARY, ExceedinglyRareEnderArtifactUpgrader.class),
    ETHERWARP_MERGER(Material.PLAYER_HEAD, Rarity.EPIC, EtherwarpMerger.class),
    JUDGEMENT_CORE(Material.PLAYER_HEAD, Rarity.LEGENDARY, JudgementCore.class),
    NULL_OVOID(Material.ENDERMAN_SPAWN_EGG, Rarity.RARE, NullOvoid.class),
    NULL_EDGE(Material.STICK, Rarity.EPIC, NullEdge.class),
    NULL_BLADE(Material.SHEARS, Rarity.LEGENDARY, NullBlade.class),
    TESSELLATED_ENDER_PEARL(Material.PLAYER_HEAD, Rarity.LEGENDARY, TessellatedEnderPearl.class),
    VOIDWALKER_KATANA(Material.IRON_SWORD, Rarity.UNCOMMON, VoidwalkerKatana.class),
    VOIDEDGE_KATANA(Material.DIAMOND_SWORD, Rarity.RARE, VoidedgeKatana.class),
    VORPAL_KATANA(Material.DIAMOND_SWORD, Rarity.EPIC, VorpalKatana.class),
    ATOMSPLIT_KATANA(Material.DIAMOND_SWORD, Rarity.LEGENDARY, AtomsplitKatana.class),
    SINSEEKER_SCYTHE(Material.GOLDEN_HOE, Rarity.EPIC, SinseekerScythe.class),
    JUJU_SHORTBOW(Material.BOW, Rarity.EPIC, JujuShortbow.class),
    TERMINATOR(Material.BOW, Rarity.LEGENDARY, Terminator.class),
    BRAIDED_GRIFFIN_FEATHER(Material.STRING, Rarity.EPIC, BraidedGriffinFeather.class),
    GYROKINETIC_WAND(Material.BLAZE_ROD, Rarity.EPIC, GyrokineticWand.class),
    SOUL_ESOWARD(Material.BIRCH_SAPLING, Rarity.RARE, SoulEsoward.class),
    GLOOMLOCK_GRIMOIRE(Material.WRITTEN_BOOK, Rarity.EPIC, GloomlockGrimoire.class),
    ETHERWARP_CONDUIT(Material.PLAYER_HEAD, Rarity.EPIC, EtherwarpConduit.class),

    /**
     * Blaze Slayer
     */
    DERELICT_ASHE(Material.GUNPOWDER, Rarity.UNCOMMON, DerelictAshe.class),
    BUNDLE_OF_MAGMA_ARROWS(Material.PLAYER_HEAD, Rarity.EPIC, BundleOfMagmaArrows.class),
    MANA_DISINTEGRATOR(Material.PLAYER_HEAD, Rarity.RARE, ManaDisintegrator.class),
    KELVIN_INVERTER(Material.PLAYER_HEAD, Rarity.RARE, KelvinInverter.class),
    BLAZE_ROD_DISTILLATE(Material.PLAYER_HEAD, Rarity.RARE, BlazeRodDistillate.class),
    MAGMA_CREAM_DISTILLATE(Material.PLAYER_HEAD, Rarity.RARE, MagmaCreamDistillate.class),
    GLOWSTONE_DISTILLATE(Material.PLAYER_HEAD, Rarity.RARE, GlowstoneDistillate.class),
    NETHER_WART_DISTILLATE(Material.PLAYER_HEAD, Rarity.RARE, NetherWartDistillate.class),
    GABAGOOL_DISTILLATE(Material.PLAYER_HEAD, Rarity.RARE, GabagoolDistillate.class),
    SCORCHED_POWER_CRYSTAL(Material.PLAYER_HEAD, Rarity.LEGENDARY, ScorchedPowerCrystal.class),
    ARCHFIEND_DICE(Material.PLAYER_HEAD, Rarity.EPIC, ArchfiendDice.class),
    HIGH_CLASS_ARCHFIEND_DICE(Material.PLAYER_HEAD, Rarity.LEGENDARY, HighClassArchfiendDice.class),
    WILSON_ENGINEERING_PLANS(Material.PAPER, Rarity.LEGENDARY, WilsonEngineeringPlans.class),
    SUBZERO_INVERTER(Material.PLAYER_HEAD, Rarity.LEGENDARY, SubzeroInverter.class),

    /**
     * BrewingItem
     */

    CHEAP_COFFEE(Material.PLAYER_HEAD, Rarity.COMMON, CheapCoffee.class),
    TEPID_GREEN_TEA(Material.PLAYER_HEAD, Rarity.COMMON, TepidGreenTea.class),
    PULPOUS_ORANGE_JUICE(Material.PLAYER_HEAD, Rarity.COMMON, PulpousOrangeJuice.class),
    BITTER_ICE_TEA(Material.PLAYER_HEAD, Rarity.COMMON, BitterIceTea.class),
    KNOCKOFF_COLA(Material.PLAYER_HEAD, Rarity.COMMON, KnockoffCola.class),
    DECENT_COFFEE(Material.PLAYER_HEAD, Rarity.UNCOMMON, DecentCoffee.class),
    WOLF_FUR_MIXIN(Material.PLAYER_HEAD, Rarity.RARE, WolfFurMixin.class),
    ZOMBIE_BRAIN_MIXIN(Material.PLAYER_HEAD, Rarity.RARE, ZombieBrainMixin.class),
    SPIDER_EGG_MIXIN(Material.PLAYER_HEAD, Rarity.RARE, SpiderEggMixin.class),
    END_PORTAL_FUMES(Material.PLAYER_HEAD, Rarity.RARE, EndPortalFumes.class),
    GABAGOEY_MIXIN(Material.PLAYER_HEAD, Rarity.RARE, GabagoeyMixin.class),
    BLACK_COFFEE(Material.PLAYER_HEAD, Rarity.RARE, BlackCoffee.class),

    /**
     * DungeonItem
     */
    ANCIENT_ROSE(Material.POPPY, Rarity.RARE, AncientRose.class),
    ARCHITECTS_FIRST_DRAFT(Material.PAPER, Rarity.EPIC, ArchitectsFirstDraft.class),
    KISMET_FEATHER(Material.FEATHER, Rarity.RARE, KismetFeather.class),
    NECRONS_HANDLE(Material.STICK, Rarity.EPIC, NecronHandle.class),

    /**
     * Mythological Ritual
     */
    GRIFFIN_FEATHER(Material.FEATHER, Rarity.RARE, GriffinFeather.class),
    ANCIENT_CLAW(Material.FLINT, Rarity.RARE, AncientClaw.class),
    ANTIQUE_REMEDIES(Material.AZURE_BLUET, Rarity.EPIC, AntiqueRemedies.class),
    CROCHET_TIGER_PLUSHIE(Material.PLAYER_HEAD, Rarity.EPIC, CrochetTigerPlushie.class),
    DWARF_TURTLE_SHELMET(Material.PLAYER_HEAD, Rarity.RARE, DwarfTurtleShelmet.class),
    DAEDALUS_STICK(Material.STICK, Rarity.LEGENDARY, DaedalusStick.class),
    MINOS_RELIC(Material.PLAYER_HEAD, Rarity.EPIC, MinosRelic.class),
    CROWN_OF_GREED(Material.GOLDEN_HELMET, Rarity.LEGENDARY, CrownOfGreed.class),
    WASHED_UP_SOUVENIR(Material.PLAYER_HEAD, Rarity.LEGENDARY, WashedUpSouvenir.class),
    DAEDALUS_AXE(Material.GOLDEN_AXE, Rarity.LEGENDARY, DaedalusAxe.class),
    SWORD_OF_REVELATIONS(Material.WOODEN_SWORD, Rarity.EPIC, SwordOfRevelations.class),

    /**
     * Spooky Festival
     */
    GREEN_CANDY(Material.PLAYER_HEAD, Rarity.UNCOMMON, GreenCandy.class),
    PRUPLE_CANDY(Material.PLAYER_HEAD, Rarity.EPIC, PurpleCandy.class),
    WEREWOLF_SKIN(Material.ROTTEN_FLESH, Rarity.RARE, WerewolfSkin.class),
    SOUL_FRAGMENT(Material.PLAYER_HEAD, Rarity.EPIC, SoulFragment.class),
    ECTOPLASM(Material.PRISMARINE_CRYSTALS, Rarity.RARE, Ectoplasm.class),
    BLAST_O_LANTERN(Material.PLAYER_HEAD, Rarity.RARE, BlastOLantern.class),
    PUMPKIN_GUTS(Material.PUMPKIN_SEEDS, Rarity.UNCOMMON, PumpkinGuts.class),
    SPOOKY_SHARD(Material.PLAYER_HEAD, Rarity.EPIC, SpookyShard.class),
    HORSEMAN_CANDLE(Material.PLAYER_HEAD, Rarity.EPIC, HorsemanCandle.class),
    BAT_FIREWORK(Material.FIREWORK_ROCKET, Rarity.RARE, BatFirework.class),

    /**
     * Armor Sets
     */
    LEAFLET_HELMET(Material.OAK_LEAVES, Rarity.COMMON, LeafletHelmet.class),
    LEAFLET_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.COMMON, LeafletChestplate.class),
    LEAFLET_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.COMMON, LeafletLeggings.class),
    LEAFLET_BOOTS(Material.LEATHER_BOOTS, Rarity.COMMON, LeafletBoots.class),
    MINERS_OUTFIT_HELMET(Material.LEATHER_HELMET, Rarity.UNCOMMON, MinerOutfitHelmet.class),
    MINERS_OUTFIT_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.UNCOMMON, MinerOutfitChestplate.class),
    MINERS_OUTFIT_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.UNCOMMON, MinerOutfitLeggings.class),
    MINERS_OUTFIT_BOOTS(Material.LEATHER_BOOTS, Rarity.UNCOMMON, MinerOutfitBoots.class),
    FARM_SUIT_HELMET(Material.LEATHER_HELMET, Rarity.COMMON, FarmSuitHelmet.class),
    FARM_SUIT_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.COMMON, FarmSuitChestplate.class),
    FARM_SUIT_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.COMMON, FarmSuitLeggings.class),
    FARM_SUIT_BOOTS(Material.LEATHER_BOOTS, Rarity.COMMON, FarmSuitBoots.class),
    ROSETTA_HELMET(Material.DIAMOND_HELMET, Rarity.UNCOMMON, RosettaHelmet.class),
    ROSETTA_CHESTPLATE(Material.DIAMOND_CHESTPLATE, Rarity.UNCOMMON, RosettaChestplate.class),
    ROSETTA_LEGGINGS(Material.DIAMOND_LEGGINGS, Rarity.UNCOMMON, RosettaLeggings.class),
    ROSETTA_BOOTS(Material.DIAMOND_BOOTS, Rarity.UNCOMMON, RosettaBoots.class),
    SQUIRE_HELMET(Material.CHAINMAIL_HELMET, Rarity.UNCOMMON, SquireHelmet.class),
    SQUIRE_CHESTPLATE(Material.IRON_CHESTPLATE, Rarity.UNCOMMON, SquireChestplate.class),
    SQUIRE_LEGGINGS(Material.CHAINMAIL_LEGGINGS, Rarity.UNCOMMON, SquireLeggings.class),
    SQUIRE_BOOTS(Material.LEATHER_BOOTS, Rarity.UNCOMMON, SquireBoots.class),
    MERCENARY_HELMET(Material.CHAINMAIL_HELMET, Rarity.RARE, MercenaryHelmet.class),
    MERCENARY_CHESTPLATE(Material.IRON_CHESTPLATE, Rarity.RARE, MercenaryChestplate.class),
    MERCENARY_LEGGINGS(Material.CHAINMAIL_LEGGINGS, Rarity.RARE, MercenaryLeggings.class),
    MERCENARY_BOOTS(Material.LEATHER_BOOTS, Rarity.RARE, MercenaryBoots.class),
    CELESTE_HELMET(Material.LEATHER_HELMET, Rarity.UNCOMMON, CelesteHelmet.class),
    CELESTE_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.UNCOMMON, CelesteChestplate.class),
    CELESTE_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.UNCOMMON, CelesteLeggings.class),
    CELESTE_BOOTS(Material.LEATHER_BOOTS, Rarity.UNCOMMON, CelesteBoots.class),
    STARLIGHT_HELMET(Material.GOLDEN_HELMET, Rarity.RARE, StarlightHelmet.class),
    STARLIGHT_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.RARE, StarlightChestplate.class),
    STARLIGHT_LEGGINGS(Material.GOLDEN_LEGGINGS, Rarity.RARE, StarlightLeggings.class),
    STARLIGHT_BOOTS(Material.LEATHER_BOOTS, Rarity.RARE, StarlightBoots.class),
    CHEAP_TUXEDO_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.EPIC, CheapTuxedoChestplate.class),
    CHEAP_TUXEDO_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.EPIC, CheapTuxedoLeggings.class),
    CHEAP_TUXEDO_BOOTS(Material.LEATHER_BOOTS, Rarity.EPIC, CheapTuxedoBoots.class),
    FANCY_TUXEDO_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.LEGENDARY, FancyTuxedoChestplate.class),
    FANCY_TUXEDO_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.LEGENDARY, FancyTuxedoLeggings.class),
    FANCY_TUXEDO_BOOTS(Material.LEATHER_BOOTS, Rarity.LEGENDARY, FancyTuxedoBoots.class),
    ELEGANT_TUXEDO_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.LEGENDARY, ElegantTuxedoChestplate.class),
    ELEGANT_TUXEDO_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.LEGENDARY, ElegantTuxedoLeggings.class),
    ELEGANT_TUXEDO_BOOTS(Material.LEATHER_BOOTS, Rarity.LEGENDARY, ElegantTuxedoBoots.class),
    MUSHROOM_HELMET(Material.LEATHER_HELMET, Rarity.COMMON, MushroomHelmet.class),
    MUSHROOM_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.COMMON, MushroomChestplate.class),
    MUSHROOM_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.COMMON, MushroomLeggings.class),
    MUSHROOM_BOOTS(Material.LEATHER_BOOTS, Rarity.COMMON, MushroomBoots.class),
    PUMPKIN_HELMET(Material.LEATHER_HELMET, Rarity.COMMON, PumpkinHelmet.class),
    PUMPKIN_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.COMMON, PumpkinChestplate.class),
    PUMPKIN_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.COMMON, PumpkinLeggings.class),
    PUMPKIN_BOOTS(Material.LEATHER_BOOTS, Rarity.COMMON, PumpkinBoots.class),


    /**
     * Pickaxes
     */
    PIONEERS_PICKAXE(Material.WOODEN_PICKAXE, Rarity.SPECIAL, PioneersPickaxe.class),
    DIAMOND_PICKAXE(Material.DIAMOND_PICKAXE, Rarity.UNCOMMON, DiamondPickaxe.class),
    IRON_PICKAXE(Material.IRON_PICKAXE, Rarity.COMMON, IronPickaxe.class),
    STONE_PICKAXE(Material.STONE_PICKAXE, Rarity.COMMON, StonePickaxe.class),
    WOODEN_PICKAXE(Material.WOODEN_PICKAXE, Rarity.COMMON, WoodenPickaxe.class),
    GOLDEN_PICKAXE(Material.GOLDEN_PICKAXE, Rarity.COMMON, GoldenPickaxe.class),
    ROOKIE_PICKAXE(Material.STONE_PICKAXE, Rarity.COMMON, RookiePickaxe.class),
    PROMISING_PICKAXE(Material.IRON_PICKAXE, Rarity.UNCOMMON),
    PICKONIMBUS_2000(Material.DIAMOND_PICKAXE, Rarity.EPIC, Pickonimbus2000.class),

    /**
     * Swords
     */
    HYPERION(Material.IRON_SWORD, Rarity.LEGENDARY, Hyperion.class),
    ROGUE_SWORD(Material.GOLDEN_SWORD, Rarity.COMMON, RogueSword.class),
    DIAMOND_SWORD(Material.DIAMOND_SWORD, Rarity.UNCOMMON, DiamondSword.class),
    IRON_SWORD(Material.IRON_SWORD, Rarity.COMMON, IronSword.class),
    STONE_SWORD(Material.STONE_SWORD, Rarity.COMMON, StoneSword.class),
    WOODEN_SWORD(Material.WOODEN_SWORD, Rarity.COMMON, WoodenSword.class),
    GOLDEN_SWORD(Material.GOLDEN_SWORD, Rarity.COMMON, GoldenSword.class),
    UNDEAD_SWORD(Material.IRON_SWORD, Rarity.COMMON, UndeadSword.class),
    END_SWORD(Material.DIAMOND_SWORD, Rarity.UNCOMMON, EndSword.class),
    SPIDER_SWORD(Material.IRON_SWORD, Rarity.COMMON, SpiderSword.class),
    ASPECT_OF_THE_JERRY(Material.WOODEN_SWORD, Rarity.COMMON, AspectOfTheJerry.class),
    FANCY_SWORD(Material.GOLDEN_SWORD, Rarity.COMMON, FancySword.class),
    HUNTER_KNIFE(Material.IRON_SWORD, Rarity.UNCOMMON, HunterKnife.class),
    PRISMARINE_BLADE(Material.PRISMARINE_SHARD, Rarity.UNCOMMON, PrismarineBlade.class),
    SILVER_FANG(Material.GHAST_TEAR, Rarity.UNCOMMON, SilverFang.class),
    ASPECT_OF_THE_END(Material.DIAMOND_SWORD, Rarity.RARE, AspectOfTheEnd.class),
    SQUIRE_SWORD(Material.IRON_SWORD, Rarity.UNCOMMON, SquireSword.class),
    MERCENARY_AXE(Material.IRON_AXE, Rarity.RARE, MercenaryAxe.class),

    /**
     * Shovels
     */
    WOODEN_SHOVEL(Material.WOODEN_SHOVEL, Rarity.COMMON, WoodenShovel.class),
    STONE_SHOVEL(Material.STONE_SHOVEL, Rarity.COMMON, StoneShovel.class),
    GOLDEN_SHOVEL(Material.GOLDEN_SHOVEL, Rarity.COMMON, GoldenShovel.class),
    IRON_SHOVEL(Material.IRON_SHOVEL, Rarity.COMMON, IronShovel.class),
    DIAMOND_SHOVEL(Material.DIAMOND_SHOVEL, Rarity.UNCOMMON, DiamondShovel.class),

    /**
     * Bows
     */
    WITHER_BOW(Material.BOW, Rarity.UNCOMMON, WitherBow.class),
    ARTISANAL_SHORTBOW(Material.BOW, Rarity.RARE, ArtisanalShortbow.class),
    BOW(Material.BOW, Rarity.COMMON, Bow.class),

    /**
     * Arrows
     */
    FLINT_ARROW(Material.ARROW, Rarity.COMMON, FlintArrow.class),

    /**
     * Jerry's Workshop
     */
    WHITE_GIFT(Material.PLAYER_HEAD, Rarity.COMMON, WhiteGift.class),
    GREEN_GIFT(Material.PLAYER_HEAD, Rarity.UNCOMMON, GreenGift.class),
    RED_GIFT(Material.PLAYER_HEAD, Rarity.RARE, RedGift.class),
    GLACIAL_FRAGMENT(Material.PLAYER_HEAD, Rarity.EPIC, GlacialFragment.class),

    /**
     * Mining Materials
     */
    ROUGH_AMBER_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughAmber.class),
    ROUGH_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughTopaz.class),
    ROUGH_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughSapphire.class),
    ROUGH_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughAmethyst.class),
    ROUGH_JASPER_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughJasper.class),
    ROUGH_RUBY_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughRuby.class),
    ROUGH_JADE_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughJade.class),
    FLAWED_AMBER_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedAmber.class),
    FLAWED_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedTopaz.class),
    FLAWED_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedSapphire.class),
    FLAWED_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedAmethyst.class),
    FLAWED_JASPER_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedJasper.class),
    FLAWED_RUBY_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedRuby.class),
    FLAWED_JADE_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedJade.class),
    FINE_AMBER_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineAmber.class),
    FINE_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineTopaz.class),
    FINE_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineSapphire.class),
    FINE_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineAmethyst.class),
    FINE_JASPER_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineJasper.class),
    FINE_RUBY_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineRuby.class),
    FINE_JADE_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineJade.class),
    FLAWLESS_AMBER_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessAmber.class),
    FLAWLESS_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessTopaz.class),
    FLAWLESS_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessSapphire.class),
    FLAWLESS_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessAmethyst.class),
    FLAWLESS_JASPER_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessJasper.class),
    FLAWLESS_RUBY_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessRuby.class),
    FLAWLESS_JADE_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessJade.class),
    PERFECT_AMBER_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectAmber.class),
    PERFECT_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectTopaz.class),
    PERFECT_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectSapphire.class),
    PERFECT_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectAmethyst.class),
    PERFECT_JASPER_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectJasper.class),
    PERFECT_RUBY_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectRuby.class),
    PERFECT_JADE_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectJade.class),
    HARD_STONE(Material.STONE, Rarity.COMMON, HardStone.class),
    MITHRIL(Material.PRISMARINE_CRYSTALS, Rarity.COMMON, Mithril.class),
    TITANIUM(Material.PLAYER_HEAD, Rarity.RARE, Titanium.class),
    SULPHUR(Material.GLOWSTONE_DUST, Rarity.UNCOMMON),
    CONCENTRATED_STONE(Material.PLAYER_HEAD, Rarity.RARE, ConcentratedStone.class),

    /**
     * Forge Items
     */
    REFINED_MITHRIL(Material.PLAYER_HEAD, Rarity.EPIC, RefinedMithril.class),
    REFINED_TITANIUM(Material.PLAYER_HEAD, Rarity.EPIC, RefinedTitanium.class),

    /**
     * Other Mining Stuff
     */
    GOBLIN_EGG(Material.EGG, Rarity.RARE, GoblinEgg.class),
    YELLOW_GOBLIN_EGG(Material.EGG, Rarity.RARE, YellowGoblinEgg.class),
    RED_GOBLIN_EGG(Material.EGG, Rarity.RARE, RedGoblinEgg.class),
    GREEN_GOBLIN_EGG(Material.EGG, Rarity.UNCOMMON, GreenGoblinEgg.class),
    BLUE_GOBLIN_EGG(Material.EGG, Rarity.RARE, BlueGoblinEgg.class),
    CONTROL_SWITCH(Material.PLAYER_HEAD, Rarity.RARE, ControlSwitch.class),
    ELECTRON_TRANSMITTER(Material.PLAYER_HEAD, Rarity.RARE, ElectronTransmitter.class),
    FTX_3070(Material.PLAYER_HEAD, Rarity.RARE, FTX3070.class),
    ROBOTRON_REFLECTOR(Material.PLAYER_HEAD, Rarity.RARE, RobotronReflector.class),
    SUPERLITE_MOTOR(Material.PLAYER_HEAD, Rarity.RARE, SuperliteMotor.class),
    SYNTHETIC_HEART(Material.PLAYER_HEAD, Rarity.RARE, SyntheticHeart.class),
    SLUDGE_JUICE(Material.PLAYER_HEAD, Rarity.UNCOMMON, SludgeJuice.class),
    OIL_BARREL(Material.PLAYER_HEAD, Rarity.UNCOMMON, OilBarrel.class),
    TREASURITE(Material.PLAYER_HEAD, Rarity.EPIC, Treasurite.class),
    JUNGLE_KEY(Material.TRIPWIRE_HOOK, Rarity.EPIC, JungleKey.class),
    WISHING_COMPASS(Material.PLAYER_HEAD, Rarity.UNCOMMON, WishingCompass.class),
    WORM_MEMBRANE(Material.ROTTEN_FLESH, Rarity.UNCOMMON, WormMembrane.class),
    MAGMA_CORE(Material.MAGMA_CREAM, Rarity.RARE, MagmaCore.class),
    ETERNAL_FLAME_RING(Material.PLAYER_HEAD, Rarity.UNCOMMON, EternalFlameRing.class),
    HELIX(Material.PLAYER_HEAD, Rarity.LEGENDARY, Helix.class),
    BOB_OMB(Material.PLAYER_HEAD, Rarity.RARE, Bobomb.class),
    PREHISTORIC_EGG(Material.PLAYER_HEAD, Rarity.COMMON, PrehistoricEgg.class),
    RECALL_POTION(Material.PLAYER_HEAD, Rarity.EPIC, RecallPotion.class),
    GEMSTONE_MIXTURE(Material.PLAYER_HEAD, Rarity.RARE, GemstoneMixture.class),
    DIVAN_FRAGMENT(Material.PLAYER_HEAD, Rarity.EPIC, DivanFragment.class),
    DIVAN_ALLOY(Material.PLAYER_HEAD, Rarity.LEGENDARY, DivanAlloy.class),
    GLACITE_JEWEL(Material.PLAYER_HEAD, Rarity.RARE, GlaciteJewel.class),
    STARFALL(Material.NETHER_STAR, Rarity.RARE, Starfall.class),
    SORROW(Material.GHAST_TEAR, Rarity.RARE, Sorrow.class),
    PLASMA(Material.PLAYER_HEAD, Rarity.RARE, Plasma.class),
    VOLTA(Material.PLAYER_HEAD, Rarity.RARE, Volta.class),

    /**
     * Travel Scrolls
     */
    HUB_CASTLE_TRAVEL_SCROLL(Material.MAP, Rarity.EPIC, HubCastleTravelScroll.class),
    HUB_MUSEUM_TRAVEL_SCROLL(Material.MAP, Rarity.EPIC, HubMuseumTravelScroll.class),
    HUB_CRYPT_TRAVEL_SCROLL(Material.MAP, Rarity.EPIC, HubCryptsTravelScroll.class),

    /**
     * Crimson Isles
     */
    FLAMES(Material.RED_SAND, Rarity.COMMON, Flames.class),

    /**
     * Vanilla Items
     */
    AIR(Material.AIR, Rarity.COMMON),
    DIRT(Material.DIRT, Rarity.COMMON),
    CRAFTING_TABLE(Material.CRAFTING_TABLE, Rarity.COMMON , CraftingTable.class),
    ANVIL(Material.ANVIL, Rarity.COMMON, Anvil.class),
    OAK_LEAVES(Material.OAK_LEAVES, Rarity.COMMON , OakPlanks.class),
    STICK(Material.STICK, Rarity.COMMON, Stick.class),
    ACACIA_WOOD(Material.ACACIA_WOOD, Rarity.COMMON, Acacia.class),
    BAKED_POTATO(Material.BAKED_POTATO, Rarity.COMMON, BakedPotato.class),
    BIRCH_WOOD(Material.BIRCH_WOOD, Rarity.COMMON, Birch.class),
    BLAZE_ROD(Material.BLAZE_ROD, Rarity.COMMON, BlazeRod.class),
    BONE(Material.BONE, Rarity.COMMON, Bone.class),
    BONE_MEAL(Material.BONE_MEAL, Rarity.COMMON, BoneMeal.class),
    BOOK(Material.BOOK, Rarity.COMMON),
    BOOKSHELF(Material.BOOKSHELF, Rarity.COMMON),
    BOWL(Material.BOWL, Rarity.COMMON),
    BREAD(Material.BREAD, Rarity.COMMON),
    CARROT(Material.CARROT, Rarity.COMMON, Carrot.class),
    CHARCOAL(Material.CHARCOAL, Rarity.COMMON),
    COAL(Material.COAL, Rarity.COMMON, Coal.class),
    COCOA_BEANS(Material.COCOA_BEANS, Rarity.COMMON, CocoaBean.class),
    DARK_OAK_WOOD(Material.DARK_OAK_WOOD, Rarity.COMMON, DarkOak.class),
    DIAMOND(Material.DIAMOND, Rarity.COMMON, Diamond.class),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, Rarity.COMMON, DiamondBlock.class),
    EGG(Material.EGG, Rarity.COMMON, Egg.class),
    EMERALD(Material.EMERALD, Rarity.COMMON, Emerald.class),
    EMERALD_BLOCK(Material.EMERALD_BLOCK, Rarity.COMMON, EmeraldBlock.class),
    ENDER_PEARL(Material.ENDER_PEARL, Rarity.COMMON, EnderPearl.class),
    FEATHER(Material.FEATHER, Rarity.COMMON, Feather.class),
    FLINT(Material.FLINT, Rarity.COMMON, Flint.class),
    GLOWSTONE_DUST(Material.GLOWSTONE_DUST, Rarity.COMMON, GlowstoneDust.class),
    GOLD_INGOT(Material.GOLD_INGOT, Rarity.COMMON, GoldIngot.class),
    GOLD_BLOCK(Material.GOLD_BLOCK, Rarity.COMMON, GoldBlock.class),
    GUNPOWDER(Material.GUNPOWDER, Rarity.COMMON, Gunpowder.class),
    END_STONE(Material.END_STONE, Rarity.COMMON, EndStone.class),
    EYE_OF_ENDER(Material.ENDER_EYE, Rarity.COMMON, EyeOfEnder.class),
    GHAST_TEAR(Material.GHAST_TEAR, Rarity.COMMON, GhastTear.class),
    ICE(Material.ICE, Rarity.COMMON, Ice.class),
    IRON_INGOT(Material.IRON_INGOT, Rarity.COMMON, IronIngot.class),
    GRAVEL(Material.GRAVEL, Rarity.COMMON, Gravel.class),
    LAPIS_LAZULI(Material.LAPIS_LAZULI, Rarity.COMMON, LapisLazuli.class),
    IRON_BLOCK(Material.IRON_BLOCK, Rarity.COMMON, IronBlock.class),
    JUNGLE_WOOD(Material.JUNGLE_WOOD, Rarity.COMMON, Jungle.class),
    LEATHER(Material.LEATHER, Rarity.COMMON, Leather.class),
    MAGMA_CREAM(Material.MAGMA_CREAM, Rarity.COMMON),
    OAK_WOOD(Material.OAK_WOOD, Rarity.COMMON, Oak.class),
    OAK_LOG(Material.OAK_LOG, Rarity.COMMON, Oak.class),
    SPRUCE_LOG(Material.SPRUCE_LOG, Rarity.COMMON, Spruce.class),
    ACACIA_LOG(Material.ACACIA_LOG, Rarity.COMMON, Acacia.class),
    BIRCH_LOG(Material.BIRCH_LOG, Rarity.COMMON, Birch.class),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, Rarity.COMMON, DarkOak.class),
    JUNGLE_LOG(Material.JUNGLE_LOG, Rarity.COMMON, Jungle.class),
    OBSIDIAN(Material.OBSIDIAN, Rarity.COMMON, Obsidian.class),
    PACKED_ICE(Material.PACKED_ICE, Rarity.COMMON),
    VINES(Material.VINE, Rarity.COMMON),
    PAPER(Material.PAPER, Rarity.COMMON),
    POTATO(Material.POTATO, Rarity.COMMON, Potato.class),
    PUMPKIN(Material.PUMPKIN, Rarity.COMMON, Pumpkin.class),
    REDSTONE(Material.REDSTONE, Rarity.COMMON, Redstone.class),
    REDSTONE_BLOCK(Material.REDSTONE_BLOCK, Rarity.COMMON, RedstoneBlock.class),
    ROTTEN_FLESH(Material.ROTTEN_FLESH, Rarity.COMMON, RottenFlesh.class),
    SLIME_BALL(Material.SLIME_BALL, Rarity.COMMON, SlimeBall.class),
    SPONGE(Material.SPONGE, Rarity.COMMON, Sponge.class),
    SPRUCE_WOOD(Material.SPRUCE_WOOD, Rarity.COMMON, Spruce.class),
    STRING(Material.STRING, Rarity.COMMON, StringItem.class),
    SUGAR_CANE(Material.SUGAR_CANE, Rarity.COMMON, SugarCane.class),
    SUGAR(Material.SUGAR, Rarity.COMMON, Sugar.class),
    SNOW(Material.SNOW, Rarity.COMMON, Snow.class),
    SNOW_BLOCK(Material.SNOW_BLOCK, Rarity.COMMON, SnowBlock.class),
    SNOWBALL(Material.SNOWBALL, Rarity.COMMON, Snowball.class),
    MELON_SLICE(Material.MELON_SLICE, Rarity.COMMON, Melon.class),
    RED_MUSHROOM(Material.RED_MUSHROOM, Rarity.COMMON, Mushroom.class),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, Rarity.COMMON, Mushroom.class),
    MUTTON(Material.MUTTON, Rarity.COMMON, Mutton.class),
    NETHER_WART(Material.NETHER_WART, Rarity.COMMON, NetherWart.class),
    CHICKEN(Material.CHICKEN, Rarity.COMMON, RawChicken.class),
    PORKCHOP(Material.PORKCHOP, Rarity.COMMON, RawPorkchop.class),
    RABBIT(Material.RABBIT, Rarity.COMMON, RawRabbit.class),
    WHEAT_SEEDS(Material.WHEAT_SEEDS, Rarity.COMMON, Seeds.class),
    MYCELIUM(Material.MYCELIUM, Rarity.COMMON, Mycelium.class),
    RED_SAND(Material.RED_SAND, Rarity.COMMON, RedSand.class),
    QUARTZ(Material.QUARTZ, Rarity.COMMON, Quartz.class),
    NETHERRACK(Material.NETHERRACK, Rarity.COMMON, Netherrack.class),
    NETHER_BRICK(Material.NETHER_BRICK, Rarity.COMMON),
    SAND(Material.SAND, Rarity.COMMON, Sand.class),
    CLAY_BALL(Material.CLAY_BALL, Rarity.COMMON, ClayBall.class),
    TROPICAL_FISH(Material.TROPICAL_FISH, Rarity.COMMON, TropicalFish.class),
    INK_SAC(Material.INK_SAC, Rarity.COMMON, InkSac.class),
    LILY_PAD(Material.LILY_PAD, Rarity.COMMON, LilyPad.class),
    PRISMARINE_CRYSTALS(Material.PRISMARINE_CRYSTALS, Rarity.COMMON, PrismarineCrystals.class),
    PRISMARINE_SHARD(Material.PRISMARINE_SHARD, Rarity.COMMON, PrismarineShard.class),
    PUFFERFISH(Material.PUFFERFISH, Rarity.COMMON, Pufferfish.class),
    COD(Material.COD, Rarity.COMMON, Cod.class),
    SALMON(Material.SALMON, Rarity.COMMON, Salmon.class),
    SPIDER_EYE(Material.SPIDER_EYE, Rarity.COMMON, SpiderEye.class),
    CACTUS_GREEN(Material.GREEN_DYE, Rarity.COMMON, CactusGreen.class),
    DANDELION(Material.DANDELION, Rarity.COMMON),
    FERMENTED_SPIDER_EYE(Material.FERMENTED_SPIDER_EYE, Rarity.COMMON, FermentedSpiderEye.class),
    GLISTERING_MELON(Material.GLISTERING_MELON_SLICE, Rarity.COMMON, GlisteringMelon.class),
    GLOWSTONE(Material.GLOWSTONE, Rarity.COMMON, Glowstone.class),
    HAY_BALE(Material.HAY_BLOCK, Rarity.COMMON, HayBale.class),
    POISONOUS_POTATO(Material.POISONOUS_POTATO, Rarity.COMMON, Potato.class),
    POPPY(Material.POPPY, Rarity.COMMON),
    RABBIT_FOOT(Material.RABBIT_FOOT, Rarity.COMMON, RabbitFoot.class),
    RABBIT_HIDE(Material.RABBIT_HIDE, Rarity.COMMON, RabbitHide.class),
    BEEF(Material.BEEF, Rarity.COMMON, Beef.class),
    COAL_BLOCK(Material.COAL_BLOCK, Rarity.COMMON, CoalBlock.class),
    LAPIS_LAZULI_BLOCK(Material.LAPIS_BLOCK, Rarity.COMMON, LapisLazuliBlock.class),
    BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, Rarity.COMMON),
    RED_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, Rarity.COMMON),
    BLAZE_POWDER(Material.BLAZE_POWDER, Rarity.COMMON, BlazePowder.class),
    GOLDEN_CARROT(Material.GOLDEN_CARROT, Rarity.COMMON, GoldenCarrot.class),
    GOLD_NUGGET(Material.GOLD_NUGGET, Rarity.COMMON, GoldNugget.class),
    WATER_BOTTLE(Material.POTION, Rarity.COMMON),
    CACTUS(Material.CACTUS, Rarity.COMMON, Cactus.class),
    CHEST(Material.CHEST, Rarity.COMMON, Chest.class),
    IRON_HELMET(Material.IRON_HELMET, Rarity.COMMON, IronHelmet.class),
    IRON_CHESTPLATE(Material.IRON_CHESTPLATE, Rarity.COMMON, IronChestplate.class),
    IRON_LEGGINGS(Material.IRON_LEGGINGS, Rarity.COMMON, IronLeggings.class),
    IRON_BOOTS(Material.IRON_BOOTS, Rarity.COMMON, IronBoots.class),
    LEATHER_HELMET(Material.LEATHER_HELMET, Rarity.COMMON, LeatherHelmet.class),
    LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE, Rarity.COMMON, LeatherChestplate.class),
    LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS, Rarity.COMMON, LeatherLeggings.class),
    LEATHER_BOOTS(Material.LEATHER_BOOTS, Rarity.COMMON, LeatherBoots.class),
    GOLDEN_HELMET(Material.GOLDEN_HELMET, Rarity.COMMON, GoldenHelmet.class),
    GOLDEN_CHESTPLATE(Material.GOLDEN_CHESTPLATE, Rarity.COMMON, GoldenChestplate.class),
    GOLDEN_LEGGINGS(Material.GOLDEN_LEGGINGS, Rarity.COMMON, GoldenLeggings.class),
    GOLDEN_BOOTS(Material.GOLDEN_BOOTS, Rarity.COMMON, GoldenBoots.class),
    DIAMOND_HELMET(Material.DIAMOND_HELMET, Rarity.UNCOMMON, DiamondHelmet.class),
    DIAMOND_CHESTPLATE(Material.DIAMOND_CHESTPLATE, Rarity.UNCOMMON, DiamondChestplate.class),
    DIAMOND_LEGGINGS(Material.DIAMOND_LEGGINGS, Rarity.UNCOMMON, DiamondLeggings.class),
    DIAMOND_BOOTS(Material.DIAMOND_BOOTS, Rarity.UNCOMMON, DiamondBoots.class),
    APPLE(Material.APPLE, Rarity.COMMON),
    SLIME_BLOCK(Material.SLIME_BLOCK, Rarity.COMMON, SlimeBlock.class),
    OAK_PLANKS(Material.OAK_PLANKS, Rarity.COMMON, OakPlanks.class),
    OAK_SLAB(Material.OAK_SLAB, Rarity.COMMON, OakSlab.class),
    OAK_STAIRS(Material.OAK_STAIRS, Rarity.COMMON, OakStairs.class),
    OAK_FENCE(Material.OAK_FENCE, Rarity.COMMON, OakFence.class),
    OAK_FENCE_GATE(Material.OAK_FENCE_GATE, Rarity.COMMON, OakFenceGate.class),
    OAK_DOOR(Material.OAK_DOOR, Rarity.COMMON, OakDoor.class),
    OAK_TRAPDOOR(Material.OAK_TRAPDOOR, Rarity.COMMON, OakTrapdoor.class),
    OAK_PRESSURE_PLATE(Material.OAK_PRESSURE_PLATE, Rarity.COMMON, OakPressurePlate.class),
    ACACIA_PLANKS(Material.ACACIA_PLANKS, Rarity.COMMON, AcaciaPlanks.class),
    ACACIA_SLAB(Material.ACACIA_SLAB, Rarity.COMMON, AcaciaSlab.class),
    ACACIA_STAIRS(Material.ACACIA_STAIRS, Rarity.COMMON, AcaciaStairs.class),
    ACACIA_FENCE(Material.ACACIA_FENCE, Rarity.COMMON, AcaciaFence.class),
    ACACIA_FENCE_GATE(Material.ACACIA_FENCE_GATE, Rarity.COMMON, AcaciaFenceGate.class),
    ACACIA_DOOR(Material.ACACIA_DOOR, Rarity.COMMON, AcaciaDoor.class),
    ACACIA_TRAPDOOR(Material.ACACIA_TRAPDOOR, Rarity.COMMON, AcaciaTrapdoor.class),
    ACACIA_PRESSURE_PLATE(Material.ACACIA_PRESSURE_PLATE, Rarity.COMMON, AcaciaPressurePlate.class),
    BIRCH_PLANKS(Material.BIRCH_PLANKS, Rarity.COMMON, BirchPlanks.class),
    BIRCH_SLAB(Material.BIRCH_SLAB, Rarity.COMMON, BirchSlab.class),
    BIRCH_STAIRS(Material.BIRCH_STAIRS, Rarity.COMMON, BirchStairs.class),
    BIRCH_FENCE(Material.BIRCH_FENCE, Rarity.COMMON, BirchFence.class),
    BIRCH_FENCE_GATE(Material.BIRCH_FENCE_GATE, Rarity.COMMON, BirchFenceGate.class),
    BIRCH_DOOR(Material.BIRCH_DOOR, Rarity.COMMON, BirchDoor.class),
    BIRCH_TRAPDOOR(Material.BIRCH_TRAPDOOR, Rarity.COMMON, BirchTrapdoor.class),
    BIRCH_PRESSURE_PLATE(Material.BIRCH_PRESSURE_PLATE, Rarity.COMMON, BirchPressurePlate.class),
    DARK_OAK_PLANKS(Material.DARK_OAK_PLANKS, Rarity.COMMON, DarkOakPlanks.class),
    DARK_OAK_SLAB(Material.DARK_OAK_SLAB, Rarity.COMMON, DarkOakSlab.class),
    DARK_OAK_STAIRS(Material.DARK_OAK_STAIRS, Rarity.COMMON, DarkOakStairs.class),
    DARK_OAK_FENCE(Material.DARK_OAK_FENCE, Rarity.COMMON, DarkOakFence.class),
    DARK_OAK_FENCE_GATE(Material.DARK_OAK_FENCE_GATE, Rarity.COMMON, DarkOakFenceGate.class),
    DARK_OAK_DOOR(Material.DARK_OAK_DOOR, Rarity.COMMON, DarkOakDoor.class),
    DARK_OAK_TRAPDOOR(Material.DARK_OAK_TRAPDOOR, Rarity.COMMON, DarkOakTrapdoor.class),
    DARK_OAK_PRESSURE_PLATE(Material.DARK_OAK_PRESSURE_PLATE, Rarity.COMMON, DarkOakPressurePlate.class),
    JUNGLE_PLANKS(Material.JUNGLE_PLANKS, Rarity.COMMON, JunglePlanks.class),
    JUNGLE_SLAB(Material.JUNGLE_SLAB, Rarity.COMMON, JungleSlab.class),
    JUNGLE_STAIRS(Material.JUNGLE_STAIRS, Rarity.COMMON, JungleStairs.class),
    JUNGLE_FENCE(Material.JUNGLE_FENCE, Rarity.COMMON, JungleFence.class),
    JUNGLE_FENCE_GATE(Material.JUNGLE_FENCE_GATE, Rarity.COMMON, JungleFenceGate.class),
    JUNGLE_DOOR(Material.JUNGLE_DOOR, Rarity.COMMON, JungleDoor.class),
    JUNGLE_TRAPDOOR(Material.JUNGLE_TRAPDOOR, Rarity.COMMON, JungleTrapdoor.class),
    JUNGLE_PRESSURE_PLATE(Material.JUNGLE_PRESSURE_PLATE, Rarity.COMMON, JunglePressurePlate.class),
    SPRUCE_PLANKS(Material.SPRUCE_PLANKS, Rarity.COMMON, SprucePlanks.class),
    SPRUCE_SLAB(Material.SPRUCE_SLAB, Rarity.COMMON, SpruceSlab.class),
    SPRUCE_STAIRS(Material.SPRUCE_STAIRS, Rarity.COMMON, SpruceStairs.class),
    SPRUCE_FENCE(Material.SPRUCE_FENCE, Rarity.COMMON, SpruceFence.class),
    SPRUCE_FENCE_GATE(Material.SPRUCE_FENCE_GATE, Rarity.COMMON, SpruceFenceGate.class),
    SPRUCE_DOOR(Material.SPRUCE_DOOR, Rarity.COMMON, SpruceDoor.class),
    SPRUCE_TRAPDOOR(Material.SPRUCE_TRAPDOOR, Rarity.COMMON, SpruceTrapdoor.class),
    SPRUCE_PRESSURE_PLATE(Material.SPRUCE_PRESSURE_PLATE, Rarity.COMMON, SprucePressurePlate.class),
    LADDER(Material.LADDER, Rarity.COMMON, Ladder.class),
    STONE(Material.STONE, Rarity.COMMON, Stone.class),
    STONE_SLAB(Material.STONE_SLAB, Rarity.COMMON, StoneSlab.class),
    STONE_STAIRS(Material.STONE_STAIRS, Rarity.COMMON, StoneStairs.class),
    SMOOTH_STONE(Material.SMOOTH_STONE, Rarity.COMMON, SmoothStone.class),
    SMOOTH_STONE_SLAB(Material.SMOOTH_STONE_SLAB, Rarity.COMMON, SmoothStoneSlab.class),
    GRANITE(Material.GRANITE, Rarity.COMMON, Granite.class),
    GRANITE_SLAB(Material.GRANITE_SLAB, Rarity.COMMON, GraniteSlab.class),
    GRANITE_STAIRS(Material.GRANITE_STAIRS, Rarity.COMMON, GraniteStairs.class),
    GRANITE_WALL(Material.GRANITE_WALL, Rarity.COMMON, GraniteWall.class),
    POLISHED_GRANITE(Material.POLISHED_GRANITE, Rarity.COMMON, PolishedGranite.class),
    POLISHED_GRANITE_SLAB(Material.POLISHED_GRANITE_SLAB, Rarity.COMMON, PolishedGraniteSlab.class),
    POLISHED_GRANITE_STAIRS(Material.POLISHED_GRANITE_STAIRS, Rarity.COMMON, PolishedGraniteStairs.class),
    DIORITE(Material.DIORITE, Rarity.COMMON, Diorite.class),
    DIORITE_SLAB(Material.DIORITE_SLAB, Rarity.COMMON, DioriteSlab.class),
    DIORITE_STAIRS(Material.DIORITE_STAIRS, Rarity.COMMON, DioriteStairs.class),
    DIORITE_WALL(Material.DIORITE_WALL, Rarity.COMMON, DioriteWall.class),
    POLISHED_DIORITE(Material.POLISHED_DIORITE, Rarity.COMMON, PolishedDiorite.class),
    POLISHED_DIORITE_SLAB(Material.POLISHED_DIORITE_SLAB, Rarity.COMMON, PolishedDioriteSlab.class),
    POLISHED_DIORITE_STAIRS(Material.POLISHED_DIORITE_STAIRS, Rarity.COMMON, PolishedDioriteStairs.class),
    ANDESITE(Material.ANDESITE, Rarity.COMMON, Andesite.class),
    ANDESITE_SLAB(Material.ANDESITE_SLAB, Rarity.COMMON, AndesiteSlab.class),
    ANDESITE_STAIRS(Material.ANDESITE_STAIRS, Rarity.COMMON, AndesiteStairs.class),
    ANDESITE_WALL(Material.ANDESITE_WALL, Rarity.COMMON, AndesiteWall.class),
    POLISHED_ANDESITE(Material.POLISHED_ANDESITE, Rarity.COMMON, PolishedAndesite.class),
    POLISHED_ANDESITE_SLAB(Material.POLISHED_ANDESITE_SLAB, Rarity.COMMON, PolishedAndesiteSlab.class),
    POLISHED_ANDESITE_STAIRS(Material.POLISHED_ANDESITE_STAIRS, Rarity.COMMON, PolishedAndesiteStairs.class),
    COBBLESTONE(Material.COBBLESTONE, Rarity.COMMON, Cobblestone.class),
    COBBLESTONE_SLAB(Material.COBBLESTONE_SLAB, Rarity.COMMON, CobblestoneSlab.class),
    COBBLESTONE_STAIRS(Material.COBBLESTONE_STAIRS, Rarity.COMMON, CobblestoneStairs.class),
    COBBLESTONE_WALL(Material.COBBLESTONE_WALL, Rarity.COMMON, CobblestoneWall.class),
    SANDSTONE(Material.SANDSTONE, Rarity.COMMON, Sandstone.class),
    SANDSTONE_SLAB(Material.SANDSTONE_SLAB, Rarity.COMMON, SandstoneSlab.class),
    SANDSTONE_STAIRS(Material.SANDSTONE_STAIRS, Rarity.COMMON, SandstoneStairs.class),
    SANDSTONE_WALL(Material.SANDSTONE_WALL, Rarity.COMMON, SandstoneWall.class),
    CUT_SANDSTONE(Material.CUT_SANDSTONE, Rarity.COMMON, CutSandstone.class),
    CUT_SANDSTONE_SLAB(Material.CUT_SANDSTONE_SLAB, Rarity.COMMON, CutSandstoneSlab.class),
    CHISELED_SANDSTONE(Material.CHISELED_SANDSTONE, Rarity.COMMON, ChiseledSandstone.class),
    RED_SANDSTONE(Material.RED_SANDSTONE, Rarity.COMMON, RedSandstone.class),
    RED_SANDSTONE_SLAB(Material.RED_SANDSTONE_SLAB, Rarity.COMMON, RedSandstoneSlab.class),
    RED_SANDSTONE_STAIRS(Material.RED_SANDSTONE_STAIRS, Rarity.COMMON, RedSandstoneStairs.class),
    RED_SANDSTONE_WALL(Material.RED_SANDSTONE_WALL, Rarity.COMMON, RedSandstoneWall.class),
    CUT_RED_SANDSTONE(Material.CUT_RED_SANDSTONE, Rarity.COMMON, CutRedSandstone.class),
    CUT_RED_SANDSTONE_SLAB(Material.CUT_RED_SANDSTONE_SLAB, Rarity.COMMON, CutRedSandstoneSlab.class),
    CHISELED_RED_SANDSTONE(Material.CHISELED_RED_SANDSTONE, Rarity.COMMON, ChiseledRedSandstone.class),
    STONE_BRICKS(Material.STONE_BRICKS, Rarity.COMMON, StoneBricks.class),
    STONE_BRICK_SLAB(Material.STONE_BRICK_SLAB, Rarity.COMMON, StoneBrickSlab.class),
    STONE_BRICK_STAIRS(Material.STONE_BRICK_STAIRS, Rarity.COMMON, StoneBrickStairs.class),
    STONE_BRICK_WALL(Material.STONE_BRICK_WALL, Rarity.COMMON, StoneBrickWall.class),
    MOSSY_STONE_BRICKS(Material.MOSSY_STONE_BRICKS, Rarity.COMMON, MossyStoneBricks.class),
    MOSSY_STONE_BRICK_SLAB(Material.MOSSY_STONE_BRICK_SLAB, Rarity.COMMON, MossyStoneBrickSlab.class),
    MOSSY_STONE_BRICK_STAIRS(Material.MOSSY_STONE_BRICK_STAIRS, Rarity.COMMON, MossyStoneBrickStairs.class),
    MOSSY_STONE_BRICK_WALL(Material.MOSSY_STONE_BRICK_WALL, Rarity.COMMON, MossyStoneBrickWall.class),
    QUARTZ_BLOCK(Material.QUARTZ_BLOCK, Rarity.COMMON, QuartzBlock.class),
    QUARTZ_SLAB(Material.QUARTZ_SLAB, Rarity.COMMON, QuartzSlab.class),
    QUARTZ_STAIRS(Material.QUARTZ_STAIRS, Rarity.COMMON, QuartzStairs.class),
    QUARTZ_PILLAR(Material.QUARTZ_PILLAR, Rarity.COMMON, QuartzPillar.class),
    CHISELED_QUARTZ_BLOCK(Material.CHISELED_QUARTZ_BLOCK, Rarity.COMMON, ChiseledQuartzBlock.class),
    NETHER_BRICKS(Material.NETHER_BRICKS, Rarity.COMMON, NetherBricks.class),
    NETHER_BRICK_SLAB(Material.NETHER_BRICK_SLAB, Rarity.COMMON, NetherBrickSlab.class),
    NETHER_BRICK_STAIRS(Material.NETHER_BRICK_STAIRS, Rarity.COMMON, NetherBrickStairs.class),
    NETHER_BRICK_WALL(Material.NETHER_BRICK_WALL, Rarity.COMMON, NetherBrickWall.class),
    NETHER_BRICK_FENCE(Material.NETHER_BRICK_FENCE, Rarity.COMMON, NetherBrickFence.class),
    WHITE_DYE(Material.WHITE_DYE, Rarity.COMMON, WhiteDye.class),
    ORANGE_DYE(Material.ORANGE_DYE, Rarity.COMMON, OrangeDye.class),
    MAGENTA_DYE(Material.MAGENTA_DYE, Rarity.COMMON, MagentaDye.class),
    LIGHT_BLUE_DYE(Material.LIGHT_BLUE_DYE, Rarity.COMMON, LightBlueDye.class),
    YELLOW_DYE(Material.YELLOW_DYE, Rarity.COMMON, YellowDye.class),
    LIME_DYE(Material.LIME_DYE, Rarity.COMMON, LimeDye.class),
    PINK_DYE(Material.PINK_DYE, Rarity.COMMON, PinkDye.class),
    GRAY_DYE(Material.GRAY_DYE, Rarity.COMMON, GrayDye.class),
    LIGHT_GRAY_DYE(Material.LIGHT_GRAY_DYE, Rarity.COMMON, LightGrayDye.class),
    CYAN_DYE(Material.CYAN_DYE, Rarity.COMMON, CyanDye.class),
    PURPLE_DYE(Material.PURPLE_DYE, Rarity.COMMON, PurpleDye.class),
    BLUE_DYE(Material.BLUE_DYE, Rarity.COMMON, BlueDye.class),
    BROWN_DYE(Material.BROWN_DYE, Rarity.COMMON, BrownDye.class),
    RED_DYE(Material.RED_DYE, Rarity.COMMON, RedDye.class),
    BLACK_DYE(Material.BLACK_DYE, Rarity.COMMON, BlackDye.class),
    WHITE_WOOL(Material.WHITE_WOOL, Rarity.COMMON, WhiteWool.class),
    ORANGE_WOOL(Material.ORANGE_WOOL, Rarity.COMMON, OrangeWool.class),
    MAGENTA_WOOL(Material.MAGENTA_WOOL, Rarity.COMMON, MagentaWool.class),
    LIGHT_BLUE_WOOL(Material.LIGHT_BLUE_WOOL, Rarity.COMMON, LightBlueWool.class),
    YELLOW_WOOL(Material.YELLOW_WOOL, Rarity.COMMON, YellowWool.class),
    LIME_WOOL(Material.LIME_WOOL, Rarity.COMMON, LimeWool.class),
    PINK_WOOL(Material.PINK_WOOL, Rarity.COMMON, PinkWool.class),
    GRAY_WOOL(Material.GRAY_WOOL, Rarity.COMMON, GrayWool.class),
    LIGHT_GRAY_WOOL(Material.LIGHT_GRAY_WOOL, Rarity.COMMON, LightGrayWool.class),
    CYAN_WOOL(Material.CYAN_WOOL, Rarity.COMMON, CyanWool.class),
    PURPLE_WOOL(Material.PURPLE_WOOL, Rarity.COMMON, PurpleWool.class),
    BLUE_WOOL(Material.BLUE_WOOL, Rarity.COMMON, BlueWool.class),
    BROWN_WOOL(Material.BROWN_WOOL, Rarity.COMMON, BrownWool.class),
    GREEN_WOOL(Material.GREEN_WOOL, Rarity.COMMON, GreenWool.class),
    RED_WOOL(Material.RED_WOOL, Rarity.COMMON, RedWool.class),
    BLACK_WOOL(Material.BLACK_WOOL, Rarity.COMMON, BlackWool.class),
    WHITE_CARPET(Material.WHITE_CARPET, Rarity.COMMON, WhiteCarpet.class),
    ORANGE_CARPET(Material.ORANGE_CARPET, Rarity.COMMON, OrangeCarpet.class),
    MAGENTA_CARPET(Material.MAGENTA_CARPET, Rarity.COMMON, MagentaCarpet.class),
    LIGHT_BLUE_CARPET(Material.LIGHT_BLUE_CARPET, Rarity.COMMON, LightBlueCarpet.class),
    YELLOW_CARPET(Material.YELLOW_CARPET, Rarity.COMMON, YellowCarpet.class),
    LIME_CARPET(Material.LIME_CARPET, Rarity.COMMON, LimeCarpet.class),
    PINK_CARPET(Material.PINK_CARPET, Rarity.COMMON, PinkCarpet.class),
    GRAY_CARPET(Material.GRAY_CARPET, Rarity.COMMON, GrayCarpet.class),
    LIGHT_GRAY_CARPET(Material.LIGHT_GRAY_CARPET, Rarity.COMMON, LightGrayCarpet.class),
    CYAN_CARPET(Material.CYAN_CARPET, Rarity.COMMON, CyanCarpet.class),
    PURPLE_CARPET(Material.PURPLE_CARPET, Rarity.COMMON, PurpleCarpet.class),
    BLUE_CARPET(Material.BLUE_CARPET, Rarity.COMMON, BlueCarpet.class),
    BROWN_CARPET(Material.BROWN_CARPET, Rarity.COMMON, BrownCarpet.class),
    GREEN_CARPET(Material.GREEN_CARPET, Rarity.COMMON, GreenCarpet.class),
    RED_CARPET(Material.RED_CARPET, Rarity.COMMON, RedCarpet.class),
    BLACK_CARPET(Material.BLACK_CARPET, Rarity.COMMON, BlackCarpet.class),
    GLASS(Material.GLASS, Rarity.COMMON, Glass.class),
    WHITE_STAINED_GLASS(Material.WHITE_STAINED_GLASS, Rarity.COMMON, WhiteStainedGlass.class),
    ORANGE_STAINED_GLASS(Material.ORANGE_STAINED_GLASS, Rarity.COMMON, OrangeStainedGlass.class),
    MAGENTA_STAINED_GLASS(Material.MAGENTA_STAINED_GLASS, Rarity.COMMON, MagentaStainedGlass.class),
    LIGHT_BLUE_STAINED_GLASS(Material.LIGHT_BLUE_STAINED_GLASS, Rarity.COMMON, LightBlueStainedGlass.class),
    YELLOW_STAINED_GLASS(Material.YELLOW_STAINED_GLASS, Rarity.COMMON, YellowStainedGlass.class),
    LIME_STAINED_GLASS(Material.LIME_STAINED_GLASS, Rarity.COMMON, LimeStainedGlass.class),
    PINK_STAINED_GLASS(Material.PINK_STAINED_GLASS, Rarity.COMMON, PinkStainedGlass.class),
    GRAY_STAINED_GLASS(Material.GRAY_STAINED_GLASS, Rarity.COMMON, GrayStainedGlass.class),
    LIGHT_GRAY_STAINED_GLASS(Material.LIGHT_GRAY_STAINED_GLASS, Rarity.COMMON, LightGrayStainedGlass.class),
    CYAN_STAINED_GLASS(Material.CYAN_STAINED_GLASS, Rarity.COMMON, CyanStainedGlass.class),
    PURPLE_STAINED_GLASS(Material.PURPLE_STAINED_GLASS, Rarity.COMMON, PurpleStainedGlass.class),
    BLUE_STAINED_GLASS(Material.BLUE_STAINED_GLASS, Rarity.COMMON, BlueStainedGlass.class),
    BROWN_STAINED_GLASS(Material.BROWN_STAINED_GLASS, Rarity.COMMON, BrownStainedGlass.class),
    GREEN_STAINED_GLASS(Material.GREEN_STAINED_GLASS, Rarity.COMMON, GreenStainedGlass.class),
    RED_STAINED_GLASS(Material.RED_STAINED_GLASS, Rarity.COMMON, RedStainedGlass.class),
    BLACK_STAINED_GLASS(Material.BLACK_STAINED_GLASS, Rarity.COMMON, BlackStainedGlass.class),
    GLASS_PANE(Material.GLASS_PANE, Rarity.COMMON, GlassPane.class),
    WHITE_STAINED_GLASS_PANE(Material.WHITE_STAINED_GLASS_PANE, Rarity.COMMON, WhiteStainedGlassPane.class),
    ORANGE_STAINED_GLASS_PANE(Material.ORANGE_STAINED_GLASS_PANE, Rarity.COMMON, OrangeStainedGlassPane.class),
    MAGENTA_STAINED_GLASS_PANE(Material.MAGENTA_STAINED_GLASS_PANE, Rarity.COMMON, MagentaStainedGlassPane.class),
    LIGHT_BLUE_STAINED_GLASS_PANE(Material.LIGHT_BLUE_STAINED_GLASS_PANE, Rarity.COMMON, LightBlueStainedGlassPane.class),
    YELLOW_STAINED_GLASS_PANE(Material.YELLOW_STAINED_GLASS_PANE, Rarity.COMMON, YellowStainedGlassPane.class),
    LIME_STAINED_GLASS_PANE(Material.LIME_STAINED_GLASS_PANE, Rarity.COMMON, LimeStainedGlassPane.class),
    PINK_STAINED_GLASS_PANE(Material.PINK_STAINED_GLASS_PANE, Rarity.COMMON, PinkStainedGlassPane.class),
    GRAY_STAINED_GLASS_PANE(Material.GRAY_STAINED_GLASS_PANE, Rarity.COMMON, GrayStainedGlassPane.class),
    LIGHT_GRAY_STAINED_GLASS_PANE(Material.LIGHT_GRAY_STAINED_GLASS_PANE, Rarity.COMMON, LightGrayStainedGlassPane.class),
    CYAN_STAINED_GLASS_PANE(Material.CYAN_STAINED_GLASS_PANE, Rarity.COMMON, CyanStainedGlassPane.class),
    PURPLE_STAINED_GLASS_PANE(Material.PURPLE_STAINED_GLASS_PANE, Rarity.COMMON, PurpleStainedGlassPane.class),
    BLUE_STAINED_GLASS_PANE(Material.BLUE_STAINED_GLASS_PANE, Rarity.COMMON, BlueStainedGlassPane.class),
    BROWN_STAINED_GLASS_PANE(Material.BROWN_STAINED_GLASS_PANE, Rarity.COMMON, BrownStainedGlassPane.class),
    GREEN_STAINED_GLASS_PANE(Material.GREEN_STAINED_GLASS_PANE, Rarity.COMMON, GreenStainedGlassPane.class),
    RED_STAINED_GLASS_PANE(Material.RED_STAINED_GLASS_PANE, Rarity.COMMON, RedStainedGlassPane.class),
    BLACK_STAINED_GLASS_PANE(Material.BLACK_STAINED_GLASS_PANE, Rarity.COMMON, BlackStainedGlassPane.class),
    WITHER_ROSE(Material.WITHER_ROSE, Rarity.COMMON),
    CORNFLOWER(Material.CORNFLOWER, Rarity.COMMON),
    LILY_OF_THE_VALLEY(Material.LILY_OF_THE_VALLEY, Rarity.COMMON),
    SUNFLOWER(Material.SUNFLOWER, Rarity.COMMON),
    BEETROOT(Material.BEETROOT, Rarity.COMMON),
    RED_TULIP(Material.RED_TULIP, Rarity.COMMON),
    ORANGE_TULIP(Material.ORANGE_TULIP, Rarity.COMMON),
    PINK_TULIP(Material.PINK_TULIP, Rarity.COMMON),
    WHITE_TULIP(Material.WHITE_TULIP, Rarity.COMMON),
    ROSE_BUSH(Material.ROSE_BUSH, Rarity.COMMON),
    TORCHFLOWER(Material.TORCHFLOWER, Rarity.COMMON),
    PITCHER_PLANT(Material.PITCHER_PLANT, Rarity.COMMON),
    BLUE_ORCHID(Material.BLUE_ORCHID, Rarity.COMMON),
    PEONY(Material.PEONY, Rarity.COMMON),
    PINK_PETALS(Material.PINK_PETALS, Rarity.COMMON),
    LILAC(Material.LILAC, Rarity.COMMON),
    ALLIUM(Material.ALLIUM, Rarity.COMMON),
    AZURE_BLUET(Material.AZURE_BLUET, Rarity.COMMON),
    OXEYE_DAISY(Material.OXEYE_DAISY, Rarity.COMMON),


    /**
     * Enchanted Items
     */
    ENCHANTED_ACACIA_WOOD(Material.ACACIA_LOG, Rarity.UNCOMMON, EnchantedAcaciaWood.class),
    ENCHANTED_BAKED_POTATO(Material.BAKED_POTATO, Rarity.UNCOMMON, EnchantedBakedPotato.class),
    ENCHANTED_BIRCH_WOOD(Material.BIRCH_LOG, Rarity.UNCOMMON, EnchantedBirchWood.class),
    ENCHANTED_BLAZE_ROD(Material.BLAZE_ROD, Rarity.UNCOMMON, EnchantedBlazeRod.class),
    ENCHANTED_BLAZE_POWDER(Material.BLAZE_POWDER, Rarity.UNCOMMON, EnchantedBlazePowder.class),
    ENCHANTED_BONE(Material.BONE, Rarity.UNCOMMON, EnchantedBone.class),
    ENCHANTED_CARROT(Material.CARROT, Rarity.UNCOMMON, EnchantedCarrot.class),
    ENCHANTED_CHARCOAL(Material.CHARCOAL, Rarity.UNCOMMON, EnchantedCharcoal.class),
    ENCHANTED_COAL(Material.COAL, Rarity.UNCOMMON, EnchantedCoal.class),
    ENCHANTED_COBBLESTONE(Material.COBBLESTONE, Rarity.UNCOMMON, EnchantedCobblestone.class),
    ENCHANTED_COCOA_BEANS(Material.COCOA_BEANS, Rarity.UNCOMMON, EnchantedCocoaBeans.class),
    ENCHANTED_DARK_OAK_WOOD(Material.DARK_OAK_LOG, Rarity.UNCOMMON, EnchantedDarkOakWood.class),
    ENCHANTED_DIAMOND(Material.DIAMOND, Rarity.UNCOMMON, EnchantedDiamond.class),
    ENCHANTED_EGG(Material.EGG, Rarity.UNCOMMON, EnchantedEgg.class),
    SUPER_ENCHANTED_EGG(Material.POLAR_BEAR_SPAWN_EGG, Rarity.RARE, SuperEnchantedEgg.class),
    OMEGA_ENCHANTED_EGG(Material.ENDERMITE_SPAWN_EGG, Rarity.EPIC, OmegaEnchantedEgg.class),
    ENCHANTED_EMERALD(Material.EMERALD, Rarity.UNCOMMON, EnchantedEmerald.class),
    ENCHANTED_ENDER_PEARL(Material.ENDER_PEARL, Rarity.UNCOMMON, EnchantedEnderPearl.class),
    ENCHANTED_DIAMOND_BLOCK(Material.DIAMOND_BLOCK, Rarity.RARE, EnchantedDiamondBlock.class),
    ENCHANTED_EMERALD_BLOCK(Material.EMERALD_BLOCK, Rarity.UNCOMMON, EnchantedEmeraldBlock.class),
    ENCHANTED_GOLD_INGOT(Material.GOLD_INGOT, Rarity.UNCOMMON, EnchantedGold.class),
    ENCHANTED_GOLD_BLOCK(Material.GOLD_BLOCK, Rarity.RARE, EnchantedGoldBlock.class),
    ENCHANTED_JUNGLE_WOOD(Material.JUNGLE_LOG, Rarity.UNCOMMON, EnchantedJungleWood.class),
    ENCHANTED_GUNPOWDER(Material.GUNPOWDER, Rarity.UNCOMMON, EnchantedGunpowder.class),
    ENCHANTED_IRON_INGOT(Material.IRON_INGOT, Rarity.UNCOMMON, EnchantedIron.class),
    ENCHANTED_IRON_BLOCK(Material.IRON_BLOCK, Rarity.RARE, EnchantedIronBlock.class),
    ENCHANTED_LEATHER(Material.LEATHER, Rarity.UNCOMMON, EnchantedLeather.class),
    ENCHANTED_OAK_WOOD(Material.OAK_LOG, Rarity.UNCOMMON, EnchantedOakWood.class),
    ENCHANTED_OBSIDIAN(Material.OBSIDIAN, Rarity.UNCOMMON, EnchantedObsidian.class),
    ENCHANTED_PACKED_ICE(Material.PACKED_ICE, Rarity.UNCOMMON, EnchantedPackedIce.class),
    ENCHANTED_POTATO(Material.POTATO, Rarity.UNCOMMON, EnchantedPotato.class),
    ENCHANTED_PUMPKIN(Material.PUMPKIN, Rarity.UNCOMMON, EnchantedPumpkin.class),
    ENCHANTED_REDSTONE(Material.REDSTONE, Rarity.UNCOMMON, EnchantedRedstone.class),
    ENCHANTED_REDSTONE_BLOCK(Material.REDSTONE_BLOCK, Rarity.RARE, EnchantedRedstoneBlock.class),
    ENCHANTED_ROTTEN_FLESH(Material.ROTTEN_FLESH, Rarity.UNCOMMON, EnchantedRottenFlesh.class),
    ENCHANTED_SPONGE(Material.SPONGE, Rarity.UNCOMMON, EnchantedSponge.class),
    ENCHANTED_SPRUCE_WOOD(Material.SPRUCE_LOG, Rarity.UNCOMMON, EnchantedSpruceWood.class),
    ENCHANTED_STRING(Material.STRING, Rarity.UNCOMMON, EnchantedString.class),
    ENCHANTED_SUGAR(Material.SUGAR, Rarity.UNCOMMON, EnchantedSugar.class),
    ENCHANTED_SNOW_BLOCK(Material.SNOW_BLOCK, Rarity.UNCOMMON, EnchantedSnowBlock.class),
    ENCHANTED_BROWN_MUSHROOM(Material.BROWN_MUSHROOM, Rarity.UNCOMMON, EnchantedBrownMushroom.class),
    ENCHANTED_RED_MUSHROOM(Material.RED_MUSHROOM, Rarity.UNCOMMON, EnchantedRedMushroom.class),
    ENCHANTED_CACTUS_GREEN(Material.GREEN_DYE, Rarity.UNCOMMON, EnchantedCactusGreen.class),
    ENCHANTED_CACTUS(Material.CACTUS, Rarity.RARE, EnchantedCactus.class),
    ENCHANTED_ENDSTONE(Material.END_STONE, Rarity.UNCOMMON, EnchantedEndStone.class),
    ENCHANTED_EYE_OF_ENDER(Material.ENDER_EYE, Rarity.UNCOMMON, EnchantedEyeOfEnder.class),
    ENCHANTED_ICE(Material.ICE, Rarity.UNCOMMON, EnchantedIce.class),
    ENCHANTED_MAGMA_CREAM(Material.MAGMA_CREAM, Rarity.UNCOMMON, EnchantedMagmaCream.class),
    ENCHANTED_BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, Rarity.RARE, EnchantedBrownMushroomBlock.class),
    ENCHANTED_CLAY(Material.CLAY_BALL, Rarity.UNCOMMON, EnchantedClay.class),
    ENCHANTED_CLOWNFISH(Material.TROPICAL_FISH, Rarity.UNCOMMON, EnchantedClownfish.class),
    ENCHANTED_COD(Material.COD, Rarity.UNCOMMON, EnchantedRawFish.class),
    ENCHANTED_COOKED_COD(Material.COOKED_COD, Rarity.RARE, EnchantedCookedFish.class),
    ENCHANTED_MUTTON(Material.MUTTON, Rarity.UNCOMMON, EnchantedMutton.class),
    ENCHANTED_COOKED_MUTTON(Material.COOKED_MUTTON, Rarity.RARE, EnchantedCookedMutton.class),
    ENCHANTED_RAW_SALMON(Material.SALMON, Rarity.UNCOMMON, EnchantedRawSalmon.class),
    ENCHANTED_COOKED_SALMON(Material.COOKED_SALMON, Rarity.UNCOMMON, EnchantedCookedSalmon.class),
    ENCHANTED_COOKIE(Material.COOKIE, Rarity.RARE, EnchantedCookie.class),
    ENCHANTED_DANDELION(Material.DANDELION, Rarity.UNCOMMON, EnchantedDandelion.class),
    ENCHANTED_FEATHER(Material.FEATHER, Rarity.UNCOMMON, EnchantedFeather.class),
    ENCHANTED_SPIDER_EYE(Material.SPIDER_EYE, Rarity.UNCOMMON, EnchantedSpiderEye.class),
    ENCHANTED_FERMENTED_SPIDER_EYE(Material.FERMENTED_SPIDER_EYE, Rarity.UNCOMMON, EnchantedFermentedSpiderEye.class),
    ENCHANTED_FIREWORK_ROCKET(Material.FIREWORK_ROCKET, Rarity.RARE, EnchantedFireworkRocket.class),
    ENCHANTED_FLINT(Material.FLINT, Rarity.UNCOMMON, EnchantedFlint.class),
    ENCHANTED_GHAST_TEAR(Material.GHAST_TEAR, Rarity.UNCOMMON, EnchantedGhastTear.class),
    ENCHANTED_GLISTERING_MELON(Material.GLISTERING_MELON_SLICE, Rarity.RARE, EnchantedGlisteringMelon.class),
    ENCHANTED_GLOWSTONE_DUST(Material.GLOWSTONE_DUST, Rarity.UNCOMMON, EnchantedGlowstoneDust.class),
    ENCHANTED_GLOWSTONE(Material.GLOWSTONE, Rarity.UNCOMMON, EnchantedGlowstone.class),
    ENCHANTED_GOLDEN_CARROT(Material.GOLDEN_CARROT, Rarity.UNCOMMON, EnchantedGoldenCarrot.class),
    ENCHANTED_PORK(Material.PORKCHOP, Rarity.UNCOMMON, EnchantedPork.class),
    ENCHANTED_GRILLED_PORK(Material.COOKED_PORKCHOP, Rarity.RARE, EnchantedGrilledPork.class),
    ENCHANTED_HARD_STONE(Material.STONE, Rarity.UNCOMMON, EnchantedHardstone.class),
    ENCHANTED_HAY_BALE(Material.HAY_BLOCK, Rarity.UNCOMMON, EnchantedHayBale.class),
    ENCHANTED_ANCIENT_CLAW(Material.FLINT, Rarity.EPIC, EnchantedAncientClaw.class),
    ENCHANTED_COAL_BLOCK(Material.COAL_BLOCK, Rarity.RARE, EnchantedCoalBlock.class),
    ENCHANTED_BONE_BLOCK(Material.BONE_BLOCK, Rarity.RARE, EnchantedBoneBlock.class),
    ENCHANTED_BREAD(Material.BREAD, Rarity.COMMON, EnchantedBread.class),
    ENCHANTED_RED_MUSHROOM_BLOCK(Material.RED_MUSHROOM_BLOCK, Rarity.RARE, EnchantedRedMushroomBlock.class),
    ENCHANTED_INK_SAC(Material.INK_SAC, Rarity.UNCOMMON, EnchantedInkSac.class),
    ENCHANTED_LAPIS_LAZULI(Material.LAPIS_LAZULI, Rarity.UNCOMMON, EnchantedLapisLazuli.class),
    ENCHANTED_LAPIS_LAZULI_BLOCK(Material.LAPIS_BLOCK, Rarity.RARE, EnchantedLapisLazuliBlock.class),
    ENCHANTED_LILY_PAD(Material.LILY_PAD, Rarity.UNCOMMON, EnchantedLilyPad.class),
    ENCHANTED_MELON(Material.MELON_SLICE, Rarity.UNCOMMON, EnchantedMelon.class),
    ENCHANTED_MELON_BLOCK(Material.MELON, Rarity.RARE, EnchantedMelonBlock.class),
    ENCHANTED_MITHRIL(Material.PRISMARINE_CRYSTALS, Rarity.RARE, EnchantedMithril.class),
    ENCHANTED_MYCELIUM(Material.MYCELIUM, Rarity.UNCOMMON, EnchantedMycelium.class),
    ENCHANTED_MYCELIUM_CUBE(Material.PLAYER_HEAD, Rarity.RARE, EnchantedMyceliumCube.class),
    ENCHANTED_NETHER_WART(Material.NETHER_WART, Rarity.UNCOMMON, EnchantedNetherWart.class),
    ENCHANTED_NETHERRACK(Material.NETHERRACK, Rarity.UNCOMMON, EnchantedNetherrack.class),
    ENCHANTED_PAPER(Material.PAPER, Rarity.UNCOMMON, EnchantedPaper.class),
    ENCHANTED_POISONOUS_POTATO(Material.POISONOUS_POTATO, Rarity.UNCOMMON, EnchantedPoisonousPotato.class),
    ENCHANTED_POPPY(Material.POPPY, Rarity.UNCOMMON, EnchantedPoppy.class),
    ENCHANTED_PRISMARINE_CRYSTALS(Material.PRISMARINE_CRYSTALS, Rarity.UNCOMMON, EnchantedPrismarineCrystals.class),
    ENCHANTED_PRISMARINE_SHARD(Material.PRISMARINE_SHARD, Rarity.UNCOMMON, EnchantedPrismarineShard.class),
    ENCHANTED_PUFFERFISH(Material.PUFFERFISH, Rarity.UNCOMMON, EnchantedPufferfish.class),
    ENCHANTED_QUARTZ(Material.QUARTZ, Rarity.UNCOMMON, EnchantedQuartz.class),
    ENCHANTED_QUARTZ_BLOCK(Material.QUARTZ_BLOCK, Rarity.RARE, EnchantedQuartzBlock.class),
    ENCHANTED_RABBIT_FOOT(Material.RABBIT_FOOT, Rarity.RARE, EnchantedRabbitFoot.class),
    ENCHANTED_RABBIT_HIDE(Material.RABBIT_HIDE, Rarity.RARE, EnchantedRabbitHide.class),
    ENCHANTED_RAW_BEEF(Material.BEEF, Rarity.UNCOMMON, EnchantedRawBeef.class),
    ENCHANTED_RAW_CHICKEN(Material.CHICKEN, Rarity.UNCOMMON, EnchantedRawChicken.class),
    ENCHANTED_RAW_RABBIT(Material.RABBIT, Rarity.UNCOMMON, EnchantedRawRabbit.class),
    ENCHANTED_SAND(Material.SAND, Rarity.UNCOMMON, EnchantedSand.class),
    ENCHANTED_SEEDS(Material.WHEAT_SEEDS, Rarity.UNCOMMON, EnchantedSeeds.class),
    ENCHANTED_SLIMEBALL(Material.SLIME_BALL, Rarity.UNCOMMON, EnchantedSlimeball.class),
    ENCHANTED_SLIME_BLOCK(Material.SLIME_BLOCK, Rarity.RARE, EnchantedSlimeBlock.class),
    ENCHANTED_SUGAR_CANE(Material.SUGAR_CANE, Rarity.RARE, EnchantedSugarCane.class),
    ENCHANTED_SULPHUR(Material.GLOWSTONE_DUST, Rarity.RARE, EnchantedSulphur.class),
    ENCHANTED_SULPHUR_CUBE(Material.PLAYER_HEAD, Rarity.RARE, EnchantedSulphurCube.class),
    ENCHANTED_TITANIUM(Material.PLAYER_HEAD, Rarity.EPIC, EnchantedTitanium.class),
    ENCHANTED_WET_SPONGE(Material.WET_SPONGE, Rarity.RARE, EnchantedWetSponge.class),
    ENCHANTED_WOOL(Material.WHITE_WOOL, Rarity.UNCOMMON, EnchantedWool.class),
    ENCHANTED_SHARK_FIN(Material.PRISMARINE_SHARD, Rarity.EPIC, EnchantedSharkFin.class),
    ENCHANTED_RED_SAND(Material.RED_SAND, Rarity.UNCOMMON, EnchantedRedSand.class),
    ENCHANTED_RED_SAND_CUBE(Material.PLAYER_HEAD, Rarity.RARE, EnchantedRedSandCube.class),
    ENCHANTED_BONE_MEAL(Material.BONE_MEAL, Rarity.COMMON, EnchantedBoneMeal.class),
    ;

    public final Material material;
    public final Rarity rarity;
    public final Class<? extends CustomSkyBlockItem> clazz;
    public final SkyBlockEnchantment bookType;

    ItemType(Material material, Rarity rarity) {
        this.material = material;
        this.rarity = rarity;
        this.clazz = null;
        this.bookType = null;
    }

    ItemType(Material material, Rarity rarity, Class<? extends CustomSkyBlockItem> clazz) {
        this.material = material;
        this.rarity = rarity;
        this.clazz = clazz;
        this.bookType = null;
    }

    ItemType(Material material, Rarity rarity, SkyBlockEnchantment bookType) {
        this.material = material;
        this.rarity = rarity;
        this.clazz = null;
        this.bookType = bookType;
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

    public static ItemType get(String name) {
        try {
            return ItemType.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static @Nullable ItemType fromMaterial(Material material) {
        return new SkyBlockItem(material).getAttributeHandler().getItemTypeAsType();
    }

    public static boolean isVanillaReplaced(String item) {
        return get(item) != null;
    }
}
