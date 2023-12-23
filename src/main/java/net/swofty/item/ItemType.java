package net.swofty.item;

import net.minestom.server.item.Material;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.items.enchantment.EnchantedBook;
import net.swofty.item.items.mining.DiamondPickaxe;
import net.swofty.item.items.mining.PioneersPickaxe;
import net.swofty.item.items.weapon.Hyperion;

public enum ItemType {
    HYPERION(Material.IRON_SWORD, Rarity.LEGENDARY, Hyperion.class),
    ENCHANTED_BOOK(Material.ENCHANTED_BOOK, Rarity.UNCOMMON, EnchantedBook.class),
    DIRT(Material.DIRT, Rarity.EPIC),
    DIAMOND_PICKAXE(Material.DIAMOND_PICKAXE, Rarity.UNCOMMON, DiamondPickaxe.class),
    PIONEERS_PICKAXE(Material.WOODEN_PICKAXE, Rarity.SPECIAL, PioneersPickaxe.class),
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
