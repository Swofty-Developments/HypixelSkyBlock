package net.swofty.types.generic.item;

import net.minestom.server.item.Material;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.items.armor.LeafletHat;
import net.swofty.types.generic.item.items.armor.LeafletPants;
import net.swofty.types.generic.item.items.armor.LeafletSandals;
import net.swofty.types.generic.item.items.armor.LeafletTunic;
import net.swofty.types.generic.item.items.backpacks.*;
import net.swofty.types.generic.item.items.enchanted.*;
import net.swofty.types.generic.item.items.enchantment.EnchantedBook;
import net.swofty.types.generic.item.items.farming.RookieHoe;
import net.swofty.types.generic.item.items.farming.WheatCrystal;
import net.swofty.types.generic.item.items.mining.PioneersPickaxe;
import net.swofty.types.generic.item.items.mining.crystalHallows.HardStone;
import net.swofty.types.generic.item.items.mining.crystalHallows.gemstones.*;
import net.swofty.types.generic.item.items.mining.dwarvenMines.Mithril;
import net.swofty.types.generic.item.items.mining.dwarvenMines.Titanium;
import net.swofty.types.generic.item.items.mining.vanilla.DiamondPickaxe;
import net.swofty.types.generic.item.items.mining.vanilla.IronPickaxe;
import net.swofty.types.generic.item.items.mining.vanilla.StonePickaxe;
import net.swofty.types.generic.item.items.mining.vanilla.WoodenPickaxe;
import net.swofty.types.generic.item.items.minion.*;
import net.swofty.types.generic.item.items.miscellaneous.MoveJerry;
import net.swofty.types.generic.item.items.miscellaneous.SkyBlockMenu;
import net.swofty.types.generic.item.items.talismans.SpeedTalisman;
import net.swofty.types.generic.item.items.talismans.ZombieTalisman;
import net.swofty.types.generic.item.items.vanilla.Coal;
import net.swofty.types.generic.item.items.vanilla.Cobblestone;
import net.swofty.types.generic.item.items.vanilla.Stick;
import net.swofty.types.generic.item.items.vanilla.Wheat;
import net.swofty.types.generic.item.items.weapon.Hyperion;
import net.swofty.types.generic.item.items.weapon.vanilla.DiamondSword;
import net.swofty.types.generic.item.items.weapon.vanilla.IronSword;
import net.swofty.types.generic.item.items.weapon.vanilla.StoneSword;
import net.swofty.types.generic.item.items.weapon.vanilla.WoodenSword;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.Nullable;

public enum ItemType {
    ENCHANTED_BOOK(Material.ENCHANTED_BOOK, Rarity.UNCOMMON, EnchantedBook.class),
    DIRT(Material.DIRT, Rarity.EPIC),
    SKYBLOCK_MENU(Material.NETHER_STAR, Rarity.COMMON, SkyBlockMenu.class),
    AIR(Material.AIR, Rarity.COMMON),
    MOVE_JERRY(Material.VILLAGER_SPAWN_EGG, Rarity.COMMON, MoveJerry.class),
    ANCIENT_CLAW(Material.FLINT, Rarity.RARE),
    /**
     * Talismans
     */
    ZOMBIE_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, ZombieTalisman.class),
    SPEED_TALISMAN(Material.PLAYER_HEAD, Rarity.COMMON, SpeedTalisman.class),

    /**
     * Minions
     */
    COBBLESTONE_MINION(Material.PLAYER_HEAD, Rarity.RARE, CobblestoneMinion.class),
    COAL_MINION(Material.PLAYER_HEAD, Rarity.RARE, CoalMinion.class),
    SNOW_MINION(Material.PLAYER_HEAD, Rarity.RARE, SnowMinion.class),

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
     * Farming Props
     */
    ROOKIE_HOE(Material.STONE_HOE, Rarity.COMMON, RookieHoe.class),
    WOODEN_HOE(Material.WOODEN_HOE, Rarity.COMMON),
    STONE_HOE(Material.STONE_HOE, Rarity.COMMON),
    IRON_HOE(Material.IRON_HOE, Rarity.COMMON),
    DIAMOND_HOE(Material.DIAMOND_HOE, Rarity.UNCOMMON),
    NETHERITE_HOE(Material.NETHERITE_HOE, Rarity.RARE),
    WHEAT(Material.WHEAT, Rarity.COMMON, Wheat.class),
    CACTUS(Material.CACTUS, Rarity.COMMON),
    WHEAT_CRYSTAL(Material.PLAYER_HEAD, Rarity.SPECIAL, WheatCrystal.class),

    /**
     * Fishing Props
     */
    MAGMAFISH(Material.PLAYER_HEAD, Rarity.RARE),
    SHARK_FIN(Material.PRISMARINE_SHARD, Rarity.RARE),

    /**
     * Combat Props
     */
    CHILI_PEPPER(Material.PLAYER_HEAD, Rarity.UNCOMMON),

    /**
     * Armor Sets
     */
    LEAFLET_HAT(Material.OAK_LEAVES, Rarity.COMMON, LeafletHat.class),
    LEAFLET_TUNIC(Material.LEATHER_CHESTPLATE, Rarity.COMMON, LeafletTunic.class),
    LEAFLET_PANTS(Material.LEATHER_LEGGINGS, Rarity.COMMON, LeafletPants.class),
    LEAFLET_SANDALS(Material.LEATHER_BOOTS, Rarity.COMMON, LeafletSandals.class),

    /**
     * Pickaxes
     */
    PIONEERS_PICKAXE(Material.WOODEN_PICKAXE, Rarity.SPECIAL, PioneersPickaxe.class),
    DIAMOND_PICKAXE(Material.DIAMOND_PICKAXE, Rarity.UNCOMMON, DiamondPickaxe.class),
    IRON_PICKAXE(Material.IRON_PICKAXE, Rarity.COMMON, IronPickaxe.class),
    STONE_PICKAXE(Material.STONE_PICKAXE, Rarity.COMMON, StonePickaxe.class),
    WOODEN_PICKAXE(Material.WOODEN_PICKAXE, Rarity.COMMON, WoodenPickaxe.class),

    /**
     * Swords
     */
    HYPERION(Material.IRON_SWORD, Rarity.LEGENDARY, Hyperion.class),
    DIAMOND_SWORD(Material.DIAMOND_SWORD, Rarity.UNCOMMON, DiamondSword.class),
    IRON_SWORD(Material.IRON_SWORD, Rarity.COMMON, IronSword.class),
    STONE_SWORD(Material.STONE_SWORD, Rarity.COMMON, StoneSword.class),
    WOODEN_SWORD(Material.WOODEN_SWORD, Rarity.COMMON, WoodenSword.class),

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
    ROUGH_OPAL_GEM(Material.PLAYER_HEAD, Rarity.COMMON, RoughOpal.class),
    FLAWED_AMBER_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedAmber.class),
    FLAWED_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedTopaz.class),
    FLAWED_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedSapphire.class),
    FLAWED_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedAmethyst.class),
    FLAWED_JASPER_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedJasper.class),
    FLAWED_RUBY_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedRuby.class),
    FLAWED_JADE_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedJade.class),
    FLAWED_OPAL_GEM(Material.PLAYER_HEAD, Rarity.UNCOMMON, FlawedOpal.class),
    FINE_AMBER_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineAmber.class),
    FINE_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineTopaz.class),
    FINE_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineSapphire.class),
    FINE_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineAmethyst.class),
    FINE_JASPER_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineJasper.class),
    FINE_RUBY_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineRuby.class),
    FINE_JADE_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineJade.class),
    FINE_OPAL_GEM(Material.PLAYER_HEAD, Rarity.RARE, FineOpal.class),
    FLAWLESS_AMBER_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessAmber.class),
    FLAWLESS_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessTopaz.class),
    FLAWLESS_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessSapphire.class),
    FLAWLESS_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessAmethyst.class),
    FLAWLESS_JASPER_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessJasper.class),
    FLAWLESS_RUBY_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessRuby.class),
    FLAWLESS_JADE_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessJade.class),
    FLAWLESS_OPAL_GEM(Material.PLAYER_HEAD, Rarity.EPIC, FlawlessOpal.class),
    PERFECT_AMBER_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectAmber.class),
    PERFECT_TOPAZ_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectTopaz.class),
    PERFECT_SAPPHIRE_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectSapphire.class),
    PERFECT_AMETHYST_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectAmethyst.class),
    PERFECT_JASPER_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectJasper.class),
    PERFECT_RUBY_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectRuby.class),
    PERFECT_JADE_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectJade.class),
    PERFECT_OPAL_GEM(Material.PLAYER_HEAD, Rarity.LEGENDARY, PerfectOpal.class),
    HARD_STONE(Material.STONE, Rarity.COMMON, HardStone.class),
    MITHRIL(Material.PRISMARINE_CRYSTALS, Rarity.COMMON, Mithril.class),
    TITANIUM(Material.PLAYER_HEAD, Rarity.RARE, Titanium.class),
    SULPHUR(Material.GLOWSTONE_DUST, Rarity.UNCOMMON),

    /**
     * Vanilla Items
     */
    CRAFTING_TABLE(Material.CRAFTING_TABLE, Rarity.COMMON),
    OAK_LEAVES(Material.OAK_LEAVES, Rarity.COMMON),
    STICK(Material.STICK, Rarity.COMMON, Stick.class),
    ACACIA_WOOD(Material.ACACIA_WOOD, Rarity.COMMON),
    BAKED_POTATO(Material.BAKED_POTATO, Rarity.COMMON),
    BIRCH_WOOD(Material.BIRCH_WOOD, Rarity.COMMON),
    BLAZE_ROD(Material.BLAZE_ROD, Rarity.COMMON),
    BONE(Material.BONE, Rarity.COMMON),
    BONE_BLOCK(Material.BONE_BLOCK, Rarity.COMMON),
    BOOK(Material.BOOK, Rarity.COMMON),
    BOOKSHELF(Material.BOOKSHELF, Rarity.COMMON),
    BOWL(Material.BOWL, Rarity.COMMON),
    BREAD(Material.BREAD, Rarity.COMMON),
    CARROT(Material.CARROT, Rarity.COMMON),
    CHARCOAL(Material.CHARCOAL, Rarity.COMMON),
    COAL(Material.COAL, Rarity.COMMON, Coal.class),
    COBBLESTONE(Material.COBBLESTONE, Rarity.COMMON, Cobblestone.class),
    COCOA_BEANS(Material.COCOA_BEANS, Rarity.COMMON),
    DARK_OAK_WOOD(Material.DARK_OAK_WOOD, Rarity.COMMON),
    DIAMOND(Material.DIAMOND, Rarity.COMMON),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, Rarity.COMMON),
    EGG(Material.EGG, Rarity.COMMON),
    EMERALD(Material.EMERALD, Rarity.COMMON),
    EMERALD_BLOCK(Material.EMERALD_BLOCK, Rarity.COMMON),
    ENDER_PEARL(Material.ENDER_PEARL, Rarity.COMMON),
    FEATHER(Material.FEATHER, Rarity.COMMON),
    FLINT(Material.FLINT, Rarity.COMMON),
    GLOWSTONE_DUST(Material.GLOWSTONE_DUST, Rarity.COMMON),
    GOLD_INGOT(Material.GOLD_INGOT, Rarity.COMMON),
    GOLD_BLOCK(Material.GOLD_BLOCK, Rarity.COMMON),
    GUNPOWDER(Material.GUNPOWDER, Rarity.COMMON),
    END_STONE(Material.END_STONE, Rarity.COMMON),
    EYE_OF_ENDER(Material.ENDER_EYE, Rarity.COMMON),
    GHAST_TEAR(Material.GHAST_TEAR, Rarity.COMMON),
    ICE(Material.ICE, Rarity.COMMON),
    IRON_INGOT(Material.IRON_INGOT, Rarity.COMMON),
    GRAVEL(Material.GRAVEL, Rarity.COMMON),
    LAPIS_LAZULI(Material.LAPIS_LAZULI, Rarity.COMMON),
    IRON_BLOCK(Material.IRON_BLOCK, Rarity.COMMON),
    JUNGLE_WOOD(Material.JUNGLE_WOOD, Rarity.COMMON),
    LEATHER(Material.LEATHER, Rarity.COMMON),
    MAGMA_CREAM(Material.MAGMA_CREAM, Rarity.COMMON),
    OAK_WOOD(Material.OAK_WOOD, Rarity.COMMON),
    OAK_LOG(Material.OAK_LOG, Rarity.COMMON),
    SPRUCE_LOG(Material.SPRUCE_LOG, Rarity.COMMON),
    ACAICA_LOG(Material.ACACIA_LOG, Rarity.COMMON),
    BIRCH_LOG(Material.BIRCH_LOG, Rarity.COMMON),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, Rarity.COMMON),
    JUNGLE_LOG(Material.JUNGLE_LOG, Rarity.COMMON),
    OAK_PLANKS(Material.OAK_PLANKS, Rarity.COMMON),
    OBSIDIAN(Material.OBSIDIAN, Rarity.COMMON),
    PACKED_ICE(Material.PACKED_ICE, Rarity.COMMON),
    PAPER(Material.PAPER, Rarity.COMMON),
    POTATO(Material.POTATO, Rarity.COMMON),
    PUMPKIN(Material.PUMPKIN, Rarity.COMMON),
    REDSTONE(Material.REDSTONE, Rarity.COMMON),
    REDSTONE_BLOCK(Material.REDSTONE_BLOCK, Rarity.COMMON),
    ROTTEN_FLESH(Material.ROTTEN_FLESH, Rarity.COMMON),
    SLIME_BALL(Material.SLIME_BALL, Rarity.COMMON),
    SPONGE(Material.SPONGE, Rarity.COMMON),
    SPRUCE_WOOD(Material.SPRUCE_WOOD, Rarity.COMMON),
    STRING(Material.STRING, Rarity.COMMON),
    SUGAR_CANE(Material.SUGAR_CANE, Rarity.COMMON),
    SUGAR(Material.SUGAR, Rarity.COMMON),
    SNOW(Material.SNOW, Rarity.COMMON),
    SNOW_BLOCK(Material.SNOW_BLOCK, Rarity.COMMON),
    MELON_SLICE(Material.MELON_SLICE, Rarity.COMMON),
    RED_MUSHROOM(Material.RED_MUSHROOM, Rarity.COMMON),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, Rarity.COMMON),
    MUTTON(Material.MUTTON, Rarity.COMMON),
    NETHER_WART(Material.NETHER_WART, Rarity.COMMON),
    CHICKEN(Material.CHICKEN, Rarity.COMMON),
    PORKCHOP(Material.PORKCHOP, Rarity.COMMON),
    RABBIT(Material.RABBIT, Rarity.COMMON),
    WHEAT_SEEDS(Material.WHEAT_SEEDS, Rarity.COMMON),
    MYCELIUM(Material.MYCELIUM, Rarity.COMMON),
    RED_SAND(Material.RED_SAND, Rarity.COMMON),
    QUARTZ(Material.QUARTZ, Rarity.COMMON),
    NETHERRACK(Material.NETHERRACK, Rarity.COMMON),
    SAND(Material.SAND, Rarity.COMMON),
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
    HAY_BAL(Material.HAY_BLOCK, Rarity.COMMON),
    POISONOUS_POTATO(Material.POISONOUS_POTATO, Rarity.COMMON),
    POPPY(Material.POPPY, Rarity.COMMON),
    RABBIT_FOOT(Material.RABBIT_FOOT, Rarity.COMMON),
    RABBIT_HIDE(Material.RABBIT_HIDE, Rarity.COMMON),
    BEEF(Material.BEEF, Rarity.COMMON),
    WOOL(Material.WHITE_WOOL, Rarity.COMMON),
    COAL_BLOCK(Material.COAL_BLOCK, Rarity.COMMON),
    LAPIS_LAZULI_BLOCK(Material.LAPIS_BLOCK, Rarity.COMMON),
    QUARTZ_BLOCK(Material.QUARTZ_BLOCK, Rarity.COMMON),
    BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, Rarity.COMMON),
    RED_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, Rarity.COMMON),
    BLAZE_POWDER(Material.BLAZE_POWDER, Rarity.COMMON),
    GOLDEN_CARROT(Material.GOLDEN_CARROT, Rarity.COMMON),

    /**
     * Enchanted Items
     */
    ENCHANTED_ACACIA_WOOD(Material.ACACIA_WOOD, Rarity.UNCOMMON, EnchantedAcaciaWood.class),
    ENCHANTED_BAKED_POTATO(Material.BAKED_POTATO, Rarity.UNCOMMON, EnchantedBakedPotato.class),
    ENCHANTED_BIRCH_WOOD(Material.BIRCH_WOOD, Rarity.UNCOMMON, EnchantedBirchWood.class),
    ENCHANTED_BLAZE_ROD(Material.BLAZE_ROD, Rarity.UNCOMMON, EnchantedBlazeRod.class),
    ENCHANTED_BLAZE_POWDER(Material.BLAZE_POWDER, Rarity.UNCOMMON, EnchantedBlazePowder.class),
    ENCHANTED_BONE(Material.BONE, Rarity.UNCOMMON, EnchantedBone.class),
    ENCHANTED_CARROT(Material.CARROT, Rarity.UNCOMMON, EnchantedCarrot.class),
    ENCHANTED_CHARCOAL(Material.CHARCOAL, Rarity.UNCOMMON, EnchantedCharcoal.class),
    ENCHANTED_COAL(Material.COAL, Rarity.UNCOMMON, EnchantedCoal.class),
    ENCHANTED_COBBLESTONE(Material.COBBLESTONE, Rarity.UNCOMMON, EnchantedCobblestone.class),
    ENCHANTED_COCOA_BEANS(Material.COCOA_BEANS, Rarity.UNCOMMON, EnchantedCocoaBeans.class),
    ENCHANTED_DARK_OAK_WOOD(Material.DARK_OAK_WOOD, Rarity.UNCOMMON, EnchantedDarkOakWood.class),
    ENCHANTED_DIAMOND(Material.DIAMOND, Rarity.UNCOMMON, EnchantedDiamond.class),
    ENCHANTED_EGG(Material.EGG, Rarity.UNCOMMON, EnchantedEgg.class),
    ENCHANTED_EMERALD(Material.EMERALD, Rarity.UNCOMMON, EnchantedEmerald.class),
    ENCHANTED_ENDER_PEARL(Material.ENDER_PEARL, Rarity.UNCOMMON, EnchantedEnderPearl.class),
    ENCHANTED_DIAMOND_BLOCK(Material.DIAMOND_BLOCK, Rarity.UNCOMMON, EnchantedDiamondBlock.class),
    ENCHANTED_EMERALD_BLOCK(Material.EMERALD_BLOCK, Rarity.UNCOMMON, EnchantedEmeraldBlock.class),
    ENCHANTED_GOLD_INGOT(Material.GOLD_INGOT, Rarity.UNCOMMON, EnchantedGold.class),
    ENCHANTED_GOLD_BLOCK(Material.GOLD_BLOCK, Rarity.UNCOMMON, EnchantedGoldBlock.class),
    ENCHANTED_JUNGLE_WOOD(Material.JUNGLE_WOOD, Rarity.UNCOMMON, EnchantedJungleWood.class),
    ENCHANTED_GUNPOWDER(Material.GUNPOWDER, Rarity.UNCOMMON, EnchantedGunpowder.class),
    ENCHANTED_IRON_INGOT(Material.IRON_INGOT, Rarity.UNCOMMON, EnchantedIron.class),
    ENCHANTED_IRON_BLOCK(Material.IRON_BLOCK, Rarity.UNCOMMON, EnchantedIronBlock.class),
    ENCHANTED_LEATHER(Material.LEATHER, Rarity.UNCOMMON, EnchantedLeather.class),
    ENCHANTED_OAK_WOOD(Material.OAK_WOOD, Rarity.UNCOMMON, EnchantedOakWood.class),
    ENCHANTED_OBSIDIAN(Material.OBSIDIAN, Rarity.UNCOMMON, EnchantedObsidian.class),
    ENCHANTED_PACKED_ICE(Material.PACKED_ICE, Rarity.UNCOMMON, EnchantedPackedIce.class),
    ENCHANTED_POTATO(Material.POTATO, Rarity.UNCOMMON, EnchantedPotato.class),
    ENCHANTED_PUMPKIN(Material.PUMPKIN, Rarity.UNCOMMON, EnchantedPumpkin.class),
    ENCHANTED_REDSTONE(Material.REDSTONE, Rarity.UNCOMMON, EnchantedRedstone.class),
    ENCHANTED_REDSTONE_BLOCK(Material.REDSTONE_BLOCK, Rarity.UNCOMMON, EnchantedRedstoneBlock.class),
    ENCHANTED_ROTTEN_FLESH(Material.ROTTEN_FLESH, Rarity.UNCOMMON, EnchantedRottenFlesh.class),
    ENCHANTED_SPONGE(Material.SPONGE, Rarity.UNCOMMON, EnchantedSponge.class),
    ENCHANTED_SPRUCE_WOOD(Material.SPRUCE_WOOD, Rarity.UNCOMMON, EnchantedSpruceWood.class),
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
    ENCHANTED_HAY_BAL(Material.HAY_BLOCK, Rarity.UNCOMMON, EnchantedHayBal.class),
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
    ENCHANTED_MYCELIUM_CUBE(Material.PLAYER_HEAD, Rarity.RARE, EnchantedMycelium.class),
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

    public String getDisplayName() {
        return StringUtility.toNormalCase(this.name());
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
