package net.swofty.commons.skyblock.region.mining;

import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.utility.MaterialQuantifiableRandom;

public record MiningLoot(String identifier, MaterialQuantifiableRandom material) {
    public static MiningLoot Default() {
        return new MiningLoot("default", null);
    }

    public static MiningLoot Custom(Material mat, int bounds1, int bounds2) {
        return new MiningLoot("custom", new MaterialQuantifiableRandom(new SkyBlockItem(BlockResults.getResultFor(mat)), bounds1, bounds2));
    }

    public static MiningLoot Custom(Material mat, int singleBounds) {
        return Custom(mat, singleBounds, singleBounds);
    }

    public static MiningLoot Custom(Material mat) {
        return Custom(mat, 1);
    }

    public static MiningLoot None() {
        return new MiningLoot("none", null);
    }

    enum BlockResults {
        COAL_ORE(Material.COAL),
        REDSTONE_ORE(Material.REDSTONE),
        DIAMOND_ORE(Material.DIAMOND),
        PUMPKIN(Material.PUMPKIN),
        CARVED_PUMPKIN(Material.PUMPKIN),
        MELON(Material.MELON_SLICE),
        ;

        private final Material result;

        BlockResults(Material result) {
            this.result = result;
        }

        public static Material getResultFor(Material material) {
            for (BlockResults r : values())
                if (r.name().equalsIgnoreCase(material.name()))
                    return r.result;

            return material;
        }
    }
}
