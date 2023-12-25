package net.swofty.item;

import net.minestom.server.item.Material;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.items.enchantment.EnchantedBook;
import net.swofty.item.items.mining.vanilla.DiamondPickaxe;
import net.swofty.item.items.mining.PioneersPickaxe;
import net.swofty.item.items.mining.vanilla.IronPickaxe;
import net.swofty.item.items.mining.vanilla.StonePickaxe;
import net.swofty.item.items.mining.vanilla.WoodenPickaxe;
import net.swofty.item.items.weapon.Hyperion;
import net.swofty.item.items.weapon.vanilla.DiamondSword;
import net.swofty.item.items.weapon.vanilla.IronSword;
import net.swofty.item.items.weapon.vanilla.StoneSword;
import net.swofty.item.items.weapon.vanilla.WoodenSword;

public enum ItemType {
    ENCHANTED_BOOK(Material.ENCHANTED_BOOK, Rarity.UNCOMMON, EnchantedBook.class),
    DIRT(Material.DIRT, Rarity.EPIC),

    /**
     * Pickaxes
     */
    PIONEERS_PICKAXE(Material.WOODEN_PICKAXE, Rarity.MYTHIC, PioneersPickaxe.class),
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

    public static ItemType get(String s) {
        try {
            return ItemType.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isVanillaReplaced(String item) {
        return get(item) != null;
    }
}
