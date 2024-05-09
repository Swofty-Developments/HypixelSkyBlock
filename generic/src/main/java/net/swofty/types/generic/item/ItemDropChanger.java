package net.swofty.types.generic.item;

import lombok.Getter;
import net.minestom.server.item.Material;

import java.util.function.Supplier;

@Getter
public enum ItemDropChanger {
    STONE(Material.STONE, () -> new SkyBlockItem(ItemType.COBBLESTONE)),
    COBBLESTONE(Material.COBBLESTONE, () -> new SkyBlockItem(ItemType.COBBLESTONE)),
    OAK_WOOD(Material.OAK_WOOD, () -> new SkyBlockItem(ItemType.OAK_LOG)),
    COAL_ORE(Material.COAL_ORE, () -> new SkyBlockItem(ItemType.COAL)),
    REDSTONE_ORE(Material.REDSTONE_ORE, () -> new SkyBlockItem(ItemType.REDSTONE)),
    DIAMOND_ORE(Material.DIAMOND_ORE, () -> new SkyBlockItem(ItemType.DIAMOND)),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, () -> new SkyBlockItem(ItemType.PUMPKIN)),
    MELON(Material.MELON, () -> new SkyBlockItem(ItemType.MELON_SLICE)),
    GRASS_BLOCK(Material.GRASS_BLOCK, () -> new SkyBlockItem(ItemType.DIRT)),
    OAK_LEAVES(Material.OAK_LEAVES, () -> {
        if (Math.random() < 0.05) {
            return new SkyBlockItem(ItemType.APPLE);
        } else {
            int amount = (int) (Math.random() * 3) + 1;
            return new SkyBlockItem(ItemType.STICK, amount);
        }
    }),
    ;

    private final Material material;
    private final Supplier<SkyBlockItem> itemSupplier;

    ItemDropChanger(Material material, Supplier<SkyBlockItem> itemSupplier) {
        this.material = material;
        this.itemSupplier = itemSupplier;
    }

    public static ItemDropChanger get(Material material) {
        for (ItemDropChanger changer : values()) {
            if (changer.material == material) {
                return changer;
            }
        }
        return null;
    }
}
