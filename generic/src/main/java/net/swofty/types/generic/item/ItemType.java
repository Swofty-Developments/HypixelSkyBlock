package net.swofty.types.generic.item;

import net.minestom.server.item.Material;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.items.armor.LeafletHat;
import net.swofty.types.generic.item.items.armor.LeafletPants;
import net.swofty.types.generic.item.items.armor.LeafletSandals;
import net.swofty.types.generic.item.items.armor.LeafletTunic;
import net.swofty.types.generic.item.items.enchanted.*;
import net.swofty.types.generic.item.items.enchantment.EnchantedBook;
import net.swofty.types.generic.item.items.farming.RookieHoe;
import net.swofty.types.generic.item.items.mining.PioneersPickaxe;
import net.swofty.types.generic.item.items.mining.vanilla.DiamondPickaxe;
import net.swofty.types.generic.item.items.mining.vanilla.IronPickaxe;
import net.swofty.types.generic.item.items.mining.vanilla.StonePickaxe;
import net.swofty.types.generic.item.items.mining.vanilla.WoodenPickaxe;
import net.swofty.types.generic.item.items.miscellaneous.SkyBlockMenu;
import net.swofty.types.generic.item.items.vanilla.Stick;
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

    /**
     * Farming Props
     */
    ROOKIE_HOE(Material.STONE_HOE, Rarity.COMMON, RookieHoe.class),
    WOODEN_HOE(Material.WOODEN_HOE, Rarity.COMMON),
    STONE_HOE(Material.STONE_HOE, Rarity.COMMON),
    IRON_HOE(Material.IRON_HOE, Rarity.COMMON),
    DIAMOND_HOE(Material.DIAMOND_HOE, Rarity.UNCOMMON),
    NETHERITE_HOE(Material.NETHERITE_HOE, Rarity.RARE),
    WHEAT(Material.WHEAT, Rarity.COMMON),

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
    COAL(Material.COAL, Rarity.COMMON),
    COBBLESTONE(Material.COBBLESTONE, Rarity.COMMON),
    COCOA(Material.COCOA_BEANS, Rarity.COMMON),
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
    IRON_BLOCK(Material.IRON_BLOCK, Rarity.COMMON),
    JUNGLE_WOOD(Material.JUNGLE_WOOD, Rarity.COMMON),
    LEATHER(Material.LEATHER, Rarity.COMMON),
    MAGMA_CREAM(Material.MAGMA_CREAM, Rarity.COMMON),
    OAK_WOOD(Material.OAK_WOOD, Rarity.COMMON),
    OAK_LOG(Material.OAK_LOG, Rarity.COMMON),
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

    /**
     * Enchanted Items
     */
    ENCHANTED_ACACIA_WOOD(Material.ACACIA_WOOD, Rarity.UNCOMMON, EnchantedAcaciaWood.class),
    ENCHANTED_BAKED_POTATO(Material.BAKED_POTATO, Rarity.UNCOMMON, EnchantedBakedPotato.class),
    ENCHANTED_BIRCH_WOOD(Material.BIRCH_WOOD, Rarity.UNCOMMON, EnchantedBirchWood.class),
    ENCHANTED_BLAZE_ROD(Material.BLAZE_ROD, Rarity.UNCOMMON, EnchantedBlazeRod.class),
    ENCHANTED_BONE(Material.BONE, Rarity.UNCOMMON, EnchantedBone.class),
    ENCHANTED_CARROT(Material.CARROT, Rarity.UNCOMMON, EnchantedCarrot.class),
    ENCHANTED_CHARCOAL(Material.CHARCOAL, Rarity.UNCOMMON, EnchantedCharcoal.class),
    ENCHANTED_COAL(Material.COAL, Rarity.UNCOMMON, EnchantedCoal.class),
    ENCHANTED_COBBLESTONE(Material.COBBLESTONE, Rarity.UNCOMMON, EnchantedCobblestone.class),
    ENCHANTED_COCOA(Material.COCOA_BEANS, Rarity.UNCOMMON, EnchantedCocoa.class),
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
