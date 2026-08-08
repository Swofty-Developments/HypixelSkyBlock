package net.swofty.type.skyblockgeneric.experimentation;

import net.minestom.server.item.Material;

public enum SuperPairItem {
    DIAMOND(Material.DIAMOND),
    EMERALD(Material.EMERALD),
    GOLD_INGOT(Material.GOLD_INGOT),
    IRON_INGOT(Material.IRON_INGOT),
    COAL(Material.COAL),
    REDSTONE(Material.REDSTONE),
    LAPIS_LAZULI(Material.LAPIS_LAZULI),
    QUARTZ(Material.QUARTZ);

    private final Material material;

    SuperPairItem(Material material) {
        this.material = material;
    }

    public Material material() {
        return material;
    }
}
