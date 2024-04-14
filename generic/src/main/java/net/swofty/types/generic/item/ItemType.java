package net.swofty.types.generic.item;

import lombok.SneakyThrows;
import net.minestom.server.item.Material;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.items.SandboxItem;
import net.swofty.types.generic.item.items.accessories.*;
import net.swofty.types.generic.item.items.accessories.dungeon.*;
import net.swofty.types.generic.item.items.armor.*;
import net.swofty.types.generic.item.items.armor.celeste.*;
import net.swofty.types.generic.item.items.armor.mercenary.*;
import net.swofty.types.generic.item.items.armor.mineroutfit.*;
import net.swofty.types.generic.item.items.armor.rosetta.*;
import net.swofty.types.generic.item.items.armor.squire.*;
import net.swofty.types.generic.item.items.armor.starlight.*;
import net.swofty.types.generic.item.items.backpacks.*;
import net.swofty.types.generic.item.items.combat.*;
import net.swofty.types.generic.item.items.combat.mythological.craftable.DaedalusAxe;
import net.swofty.types.generic.item.items.combat.mythological.craftable.SwordOfRevelations;
import net.swofty.types.generic.item.items.combat.mythological.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.blaze.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.enderman.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.enderman.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.spider.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.spider.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.wolf.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.wolf.drops.*;
import net.swofty.types.generic.item.items.combat.slayer.zombie.craftable.*;
import net.swofty.types.generic.item.items.combat.slayer.zombie.drops.*;
import net.swofty.types.generic.item.items.crimson.*;
import net.swofty.types.generic.item.items.dungeon.misc.AncientRose;
import net.swofty.types.generic.item.items.dungeon.misc.ArchitectsFirstDraft;
import net.swofty.types.generic.item.items.dungeon.misc.KismetFeather;
import net.swofty.types.generic.item.items.dungeon.misc.NecronHandle;
import net.swofty.types.generic.item.items.enchanted.*;
import net.swofty.types.generic.item.items.enchantment.EnchantedBook;
import net.swofty.types.generic.item.items.farming.*;
import net.swofty.types.generic.item.items.farming.vanilla.*;
import net.swofty.types.generic.item.items.fishing.festival.*;
import net.swofty.types.generic.item.items.foraging.*;
import net.swofty.types.generic.item.items.foraging.vanilla.*;
import net.swofty.types.generic.item.items.mining.vanilla.*;
import net.swofty.types.generic.item.items.minion.upgrade.MithrilInfusion;
import net.swofty.types.generic.item.items.minion.upgrade.fuel.BlockofCoal;
import net.swofty.types.generic.item.items.minion.upgrade.upgrade.*;
import net.swofty.types.generic.item.items.minion.upgrade.shipping.BudgetHopper;
import net.swofty.types.generic.item.items.minion.upgrade.shipping.EnchantedHopper;
import net.swofty.types.generic.item.items.minion.upgrade.skin.BeeMinionSkin;
import net.swofty.types.generic.item.items.miscellaneous.*;
import net.swofty.types.generic.item.items.miscellaneous.decorations.Cactus;
import net.swofty.types.generic.item.items.miscellaneous.decorations.Melon;
import net.swofty.types.generic.item.items.pet.petitems.*;
import net.swofty.types.generic.item.items.skill.farming.*;
import net.swofty.types.generic.item.items.skill.foraging.*;
import net.swofty.types.generic.item.items.skill.mining.*;
import net.swofty.types.generic.item.items.spooky.*;
import net.swofty.types.generic.item.items.jerrysworkshop.GlacialFragment;
import net.swofty.types.generic.item.items.jerrysworkshop.GreenGift;
import net.swofty.types.generic.item.items.jerrysworkshop.RedGift;
import net.swofty.types.generic.item.items.jerrysworkshop.WhiteGift;
import net.swofty.types.generic.item.items.mining.crystal.*;
import net.swofty.types.generic.item.items.mining.PioneersPickaxe;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.fine.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect.*;
import net.swofty.types.generic.item.items.mining.crystal.gemstones.rough.*;
import net.swofty.types.generic.item.items.mining.dwarven.*;
import net.swofty.types.generic.item.items.minion.foraging.*;
import net.swofty.types.generic.item.items.minion.mining.*;
import net.swofty.types.generic.item.items.miscellaneous.decorations.*;
import net.swofty.types.generic.item.items.pet.BeePet;
import net.swofty.types.generic.item.items.accessories.zombie.ZombieArtifact;
import net.swofty.types.generic.item.items.accessories.zombie.ZombieRing;
import net.swofty.types.generic.item.items.accessories.zombie.ZombieTalisman;
import net.swofty.types.generic.item.items.vanilla.*;
import net.swofty.types.generic.item.items.vanilla.armor.*;
import net.swofty.types.generic.item.items.vanilla.weapon.*;
import net.swofty.types.generic.item.items.weapon.*;
import net.swofty.types.generic.item.items.vanilla.bow.Arrow;
import net.swofty.types.generic.item.items.vanilla.bow.Bow;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

public enum ItemType {
    SANDBOX_ITEM(Material.BLAZE_POWDER, Rarity.COMMON, SandboxItem.class),
    ENCHANTED_BOOK(Material.ENCHANTED_BOOK, Rarity.UNCOMMON, EnchantedBook.class),
    DIRT(Material.DIRT, Rarity.COMMON),
    SKYBLOCK_MENU(Material.NETHER_STAR, Rarity.COMMON, SkyBlockMenu.class),
    AIR(Material.AIR, Rarity.COMMON),
    MOVE_JERRY(Material.VILLAGER_SPAWN_EGG, Rarity.COMMON, MoveJerry.class),
    BOOSTER_COOKIE(Material.COOKIE, Rarity.LEGENDARY, BoosterCookie.class),
    GOD_POTION(Material.PLAYER_HEAD, Rarity.SPECIAL, GodPotion.class),
    KAT_FLOWER(Material.POPPY, Rarity.SPECIAL, KatFlower.class),

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
    LAVA_TALISMAN(Material.PLAYER_HEAD, Rarity.UNCOMMON, FarmingTalisman.class),
    POTATO_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, FarmingTalisman.class),
    POWER_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, FarmingTalisman.class),
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
    MINING_EXP_BOOST(Material.IRON_PICKAXE, Rarity.COMMON, MiningExpBoostCommon.class),
    FARMING_EXP_BOOST(Material.IRON_HOE, Rarity.COMMON, FarmingExpBoostCommon.class),
    FISHING_EXP_BOOST(Material.SALMON, Rarity.COMMON, FishingExpBoost.class),
    FORAGING_EXP_BOOST(Material.IRON_AXE, Rarity.COMMON, ForagingExpBoost.class),
    COMBAT_EXP_BOOST(Material.IRON_SWORD, Rarity.COMMON, CombatExpBoost.class),
    BIG_TEETH(Material.GHAST_TEAR, Rarity.COMMON, BigTeeth.class),
    IRON_CLAWS(Material.IRON_INGOT, Rarity.COMMON, IronClaws.class),
    HARDENED_SCALES(Material.PRISMARINE_CRYSTALS, Rarity.UNCOMMON, HardenedScales.class),
    SHARPENED_CLAWS(Material.PRISMARINE_SHARD, Rarity.UNCOMMON, SharpenedClaws.class),

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
    FISHING_ROD(Material.FISHING_ROD, Rarity.COMMON),

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
    LEAFLET_HAT(Material.OAK_LEAVES, Rarity.COMMON, LeafletHat.class),
    LEAFLET_TUNIC(Material.LEATHER_CHESTPLATE, Rarity.COMMON, LeafletTunic.class),
    LEAFLET_PANTS(Material.LEATHER_LEGGINGS, Rarity.COMMON, LeafletPants.class),
    LEAFLET_SANDALS(Material.LEATHER_BOOTS, Rarity.COMMON, LeafletSandals.class),
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

    /**
     * Pickaxes
     */
    PIONEERS_PICKAXE(Material.WOODEN_PICKAXE, Rarity.SPECIAL, PioneersPickaxe.class),
    DIAMOND_PICKAXE(Material.DIAMOND_PICKAXE, Rarity.UNCOMMON, DiamondPickaxe.class),
    IRON_PICKAXE(Material.IRON_PICKAXE, Rarity.COMMON, IronPickaxe.class),
    STONE_PICKAXE(Material.STONE_PICKAXE, Rarity.COMMON, StonePickaxe.class),
    WOODEN_PICKAXE(Material.WOODEN_PICKAXE, Rarity.COMMON, WoodenPickaxe.class),
    GOLDEN_PICKAXE(Material.GOLDEN_PICKAXE, Rarity.COMMON, GoldenPickaxe.class),
    ROOKIE_PICKAXE(Material.STONE_PICKAXE, Rarity.COMMON),
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
    FLINT_ARROW(Material.ARROW, Rarity.COMMON, Arrow.class),
    WITHER_BOW(Material.BOW, Rarity.UNCOMMON, WitherBow.class),
    ARTISANAL_SHORTBOW(Material.BOW, Rarity.RARE, ArtisanalShortbow.class),
    BOW(Material.BOW, Rarity.COMMON, Bow.class),

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
     * Vanilla Items
     */
    CRAFTING_TABLE(Material.CRAFTING_TABLE, Rarity.COMMON , CraftingTable.class),
    OAK_LEAVES(Material.OAK_LEAVES, Rarity.COMMON , OakPlanks.class),
    STICK(Material.STICK, Rarity.COMMON, Stick.class),
    ACACIA_WOOD(Material.ACACIA_WOOD, Rarity.COMMON, Acacia.class),
    BAKED_POTATO(Material.BAKED_POTATO, Rarity.COMMON),
    BIRCH_WOOD(Material.BIRCH_WOOD, Rarity.COMMON, Birch.class),
    BLAZE_ROD(Material.BLAZE_ROD, Rarity.COMMON),
    BONE(Material.BONE, Rarity.COMMON),
    BONE_BLOCK(Material.BONE_BLOCK, Rarity.COMMON),
    BOOK(Material.BOOK, Rarity.COMMON),
    BOOKSHELF(Material.BOOKSHELF, Rarity.COMMON),
    BOWL(Material.BOWL, Rarity.COMMON),
    BREAD(Material.BREAD, Rarity.COMMON),
    CARROT(Material.CARROT, Rarity.COMMON, Carrot.class),
    CHARCOAL(Material.CHARCOAL, Rarity.COMMON),
    COAL(Material.COAL, Rarity.COMMON, Coal.class),
    COBBLESTONE(Material.COBBLESTONE, Rarity.COMMON, Cobblestone.class),
    COCOA_BEANS(Material.COCOA_BEANS, Rarity.COMMON, CocoaBean.class),
    DARK_OAK_WOOD(Material.DARK_OAK_WOOD, Rarity.COMMON, DarkOak.class),
    DIAMOND(Material.DIAMOND, Rarity.COMMON, Diamond.class),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, Rarity.COMMON),
    EGG(Material.EGG, Rarity.COMMON),
    EMERALD(Material.EMERALD, Rarity.COMMON, Emerald.class),
    EMERALD_BLOCK(Material.EMERALD_BLOCK, Rarity.COMMON),
    ENDER_PEARL(Material.ENDER_PEARL, Rarity.COMMON),
    FEATHER(Material.FEATHER, Rarity.COMMON, Feather.class),
    FLINT(Material.FLINT, Rarity.COMMON),
    GLOWSTONE_DUST(Material.GLOWSTONE_DUST, Rarity.COMMON, GlowstoneDust.class),
    GOLD_INGOT(Material.GOLD_INGOT, Rarity.COMMON, GoldOre.class),
    GOLD_BLOCK(Material.GOLD_BLOCK, Rarity.COMMON),
    GUNPOWDER(Material.GUNPOWDER, Rarity.COMMON),
    END_STONE(Material.END_STONE, Rarity.COMMON),
    EYE_OF_ENDER(Material.ENDER_EYE, Rarity.COMMON),
    GHAST_TEAR(Material.GHAST_TEAR, Rarity.COMMON),
    ICE(Material.ICE, Rarity.COMMON, Ice.class),
    IRON_INGOT(Material.IRON_INGOT, Rarity.COMMON, IronOre.class),
    GRAVEL(Material.GRAVEL, Rarity.COMMON, Gravel.class),
    LAPIS_LAZULI(Material.LAPIS_LAZULI, Rarity.COMMON, LapisLazuli.class),
    IRON_BLOCK(Material.IRON_BLOCK, Rarity.COMMON),
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
    OAK_PLANKS(Material.OAK_PLANKS, Rarity.COMMON),
    OBSIDIAN(Material.OBSIDIAN, Rarity.COMMON, Obsidian.class),
    PACKED_ICE(Material.PACKED_ICE, Rarity.COMMON),
    VINES(Material.VINE, Rarity.COMMON),
    PAPER(Material.PAPER, Rarity.COMMON),
    POTATO(Material.POTATO, Rarity.COMMON, Potato.class),
    PUMPKIN(Material.PUMPKIN, Rarity.COMMON, Pumpkin.class),
    REDSTONE(Material.REDSTONE, Rarity.COMMON, Redstone.class),
    REDSTONE_BLOCK(Material.REDSTONE_BLOCK, Rarity.COMMON),
    ROTTEN_FLESH(Material.ROTTEN_FLESH, Rarity.COMMON),
    SLIME_BALL(Material.SLIME_BALL, Rarity.COMMON),
    SPONGE(Material.SPONGE, Rarity.COMMON),
    SPRUCE_WOOD(Material.SPRUCE_WOOD, Rarity.COMMON, Spruce.class),
    STRING(Material.STRING, Rarity.COMMON),
    SUGAR_CANE(Material.SUGAR_CANE, Rarity.COMMON, SugarCane.class),
    SUGAR(Material.SUGAR, Rarity.COMMON),
    SNOW(Material.SNOW, Rarity.COMMON, Snow.class),
    SNOW_BLOCK(Material.SNOW_BLOCK, Rarity.COMMON),
    MELON_SLICE(Material.MELON_SLICE, Rarity.COMMON, Melon.class),
    RED_MUSHROOM(Material.RED_MUSHROOM, Rarity.COMMON, Mushroom.class),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, Rarity.COMMON, Mushroom.class),
    MUTTON(Material.MUTTON, Rarity.COMMON, Mutton.class),
    NETHER_WART(Material.NETHER_WART, Rarity.COMMON, NetherWart.class),
    CHICKEN(Material.CHICKEN, Rarity.COMMON, RawChicken.class),
    PORKCHOP(Material.PORKCHOP, Rarity.COMMON, RawPorkchop.class),
    RABBIT(Material.RABBIT, Rarity.COMMON, RawRabbit.class),
    WHEAT_SEEDS(Material.WHEAT_SEEDS, Rarity.COMMON, Seeds.class),
    MYCELIUM(Material.MYCELIUM, Rarity.COMMON),
    RED_SAND(Material.RED_SAND, Rarity.COMMON),
    QUARTZ(Material.QUARTZ, Rarity.COMMON, Quartz.class),
    NETHERRACK(Material.NETHERRACK, Rarity.COMMON, Netherrack.class),
    SAND(Material.SAND, Rarity.COMMON, Sand.class),
    CLAY_BALL(Material.CLAY_BALL, Rarity.COMMON),
    TROPICAL_FISH(Material.TROPICAL_FISH, Rarity.COMMON),
    INK_SAC(Material.INK_SAC, Rarity.COMMON),
    LILY_PAD(Material.LILY_PAD, Rarity.COMMON),
    PRISMARINE_CRYSTALS(Material.PRISMARINE_CRYSTALS, Rarity.COMMON),
    PRISMARINE_SHARD(Material.PRISMARINE_SHARD, Rarity.COMMON),
    PUFFERFISH(Material.PUFFERFISH, Rarity.COMMON),
    COD(Material.COD, Rarity.COMMON),
    SALMON(Material.SALMON, Rarity.COMMON),
    SPIDER_EYE(Material.SPIDER_EYE, Rarity.COMMON),
    CACTUS_GREEN(Material.GREEN_DYE, Rarity.COMMON),
    DANDELION(Material.DANDELION, Rarity.COMMON),
    FERMENTED_SPIDER_EYE(Material.FERMENTED_SPIDER_EYE, Rarity.COMMON),
    GLISTERING_MELON(Material.GLISTERING_MELON_SLICE, Rarity.COMMON),
    GLOWSTONE(Material.GLOWSTONE, Rarity.COMMON),
    HAY_BALE(Material.HAY_BLOCK, Rarity.COMMON),
    POISONOUS_POTATO(Material.POISONOUS_POTATO, Rarity.COMMON, Potato.class),
    POPPY(Material.POPPY, Rarity.COMMON),
    RABBIT_FOOT(Material.RABBIT_FOOT, Rarity.COMMON, RawRabbit.class),
    RABBIT_HIDE(Material.RABBIT_HIDE, Rarity.COMMON, RawRabbit.class),
    BEEF(Material.BEEF, Rarity.COMMON),
    WOOL(Material.WHITE_WOOL, Rarity.COMMON),
    COAL_BLOCK(Material.COAL_BLOCK, Rarity.COMMON, BlockofCoal.class),
    LAPIS_LAZULI_BLOCK(Material.LAPIS_BLOCK, Rarity.COMMON),
    QUARTZ_BLOCK(Material.QUARTZ_BLOCK, Rarity.COMMON),
    BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, Rarity.COMMON),
    RED_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, Rarity.COMMON),
    BLAZE_POWDER(Material.BLAZE_POWDER, Rarity.COMMON),
    GOLDEN_CARROT(Material.GOLDEN_CARROT, Rarity.COMMON),
    WATER_BOTTLE(Material.POTION, Rarity.COMMON),
    CACTUS(Material.CACTUS, Rarity.COMMON, Cactus.class),
    CHEST(Material.CHEST, Rarity.COMMON),
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
    ENCHANTED_DIAMOND_BLOCK(Material.DIAMOND_BLOCK, Rarity.UNCOMMON, EnchantedDiamondBlock.class),
    ENCHANTED_EMERALD_BLOCK(Material.EMERALD_BLOCK, Rarity.UNCOMMON, EnchantedEmeraldBlock.class),
    ENCHANTED_GOLD_INGOT(Material.GOLD_INGOT, Rarity.UNCOMMON, EnchantedGold.class),
    ENCHANTED_GOLD_BLOCK(Material.GOLD_BLOCK, Rarity.RARE, EnchantedGoldBlock.class),
    ENCHANTED_JUNGLE_WOOD(Material.JUNGLE_LOG, Rarity.UNCOMMON, EnchantedJungleWood.class),
    ENCHANTED_GUNPOWDER(Material.GUNPOWDER, Rarity.UNCOMMON, EnchantedGunpowder.class),
    ENCHANTED_IRON_INGOT(Material.IRON_INGOT, Rarity.UNCOMMON, EnchantedIron.class),
    ENCHANTED_IRON_BLOCK(Material.IRON_BLOCK, Rarity.UNCOMMON, EnchantedIronBlock.class),
    ENCHANTED_LEATHER(Material.LEATHER, Rarity.UNCOMMON, EnchantedLeather.class),
    ENCHANTED_OAK_WOOD(Material.OAK_LOG, Rarity.UNCOMMON, EnchantedOakWood.class),
    ENCHANTED_OBSIDIAN(Material.OBSIDIAN, Rarity.UNCOMMON, EnchantedObsidian.class),
    ENCHANTED_PACKED_ICE(Material.PACKED_ICE, Rarity.UNCOMMON, EnchantedPackedIce.class),
    ENCHANTED_POTATO(Material.POTATO, Rarity.UNCOMMON, EnchantedPotato.class),
    ENCHANTED_PUMPKIN(Material.PUMPKIN, Rarity.UNCOMMON, EnchantedPumpkin.class),
    ENCHANTED_REDSTONE(Material.REDSTONE, Rarity.UNCOMMON, EnchantedRedstone.class),
    ENCHANTED_REDSTONE_BLOCK(Material.REDSTONE_BLOCK, Rarity.UNCOMMON, EnchantedRedstoneBlock.class),
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
    ENCHANTED_HAY_BAL(Material.HAY_BLOCK, Rarity.UNCOMMON, EnchantedHayBale.class),
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
    public String getDisplayName() {
        if (clazz == null)
            return StringUtility.toNormalCase(this.name());
        if (clazz.newInstance() instanceof CustomDisplayName name)
            return name.getDisplayName();
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
