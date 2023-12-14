package net.swofty.item;

import net.minestom.server.item.Material;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.items.weapon.Hyperion;

public enum ItemType {
    HYPERION(Material.IRON_SWORD, Rarity.LEGENDARY, Hyperion.class),
    DIRT(Material.DIRT, Rarity.EPIC),
    ;

    public final Material material;
    public final Rarity rarity;
    public final Class<? extends CustomSkyBlockItem> clazz;

    ItemType(Material material, Rarity rarity) {
        this.material = material;
        this.rarity = rarity;
        this.clazz = null;
    }

    ItemType(Material material, Rarity rarity, Class<? extends CustomSkyBlockItem> clazz) {
        this.material = material;
        this.rarity = rarity;
        this.clazz = clazz;
    }
}
