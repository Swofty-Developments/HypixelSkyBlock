package net.swofty.types.generic.item;

import lombok.Getter;
import net.minestom.server.item.Material;

import java.util.function.Supplier;

@Getter
public enum ItemDropChanger {
    STONE(Material.STONE, () -> new SkyBlockItem(ItemTypeLinker.COBBLESTONE)),
    COBBLESTONE(Material.COBBLESTONE, () -> new SkyBlockItem(ItemTypeLinker.COBBLESTONE)),
    OAK_WOOD(Material.OAK_WOOD, () -> new SkyBlockItem(ItemTypeLinker.OAK_LOG)),
    COAL_ORE(Material.COAL_ORE, () -> new SkyBlockItem(ItemTypeLinker.COAL)),
    REDSTONE_ORE(Material.REDSTONE_ORE, () -> new SkyBlockItem(ItemTypeLinker.REDSTONE)),
    DIAMOND_ORE(Material.DIAMOND_ORE, () -> new SkyBlockItem(ItemTypeLinker.DIAMOND)),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, () -> new SkyBlockItem(ItemTypeLinker.PUMPKIN)),
    MELON(Material.MELON, () -> new SkyBlockItem(ItemTypeLinker.MELON_SLICE)),
    GRASS_BLOCK(Material.GRASS_BLOCK, () -> new SkyBlockItem(ItemTypeLinker.DIRT)),
    OAK_LEAVES(Material.OAK_LEAVES, () -> {
        if (Math.random() < 0.05) {
            return new SkyBlockItem(ItemTypeLinker.APPLE);
        } else {
            int amount = (int) (Math.random() * 3) + 1;
            return new SkyBlockItem(ItemTypeLinker.STICK, amount);
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
