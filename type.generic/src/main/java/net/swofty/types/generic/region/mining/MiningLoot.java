package net.swofty.types.generic.region.mining;

import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemDropChanger;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.utility.MaterialQuantifiableRandom;

public record MiningLoot(String identifier, MaterialQuantifiableRandom material) {
    public static MiningLoot defaultLoot() {
        return new MiningLoot("default", null);
    }

    public static MiningLoot custom(Material mat, int bounds1, int bounds2) {
        return new MiningLoot("custom", new MaterialQuantifiableRandom(new SkyBlockItem(mat), bounds1, bounds2));
    }

    public static MiningLoot custom(Material mat, int singleBounds) {
        return custom(mat, singleBounds, singleBounds);
    }

    public static MiningLoot custom(Material mat) {
        return custom(mat, 1);
    }

    public static MiningLoot none() {
        return new MiningLoot("none", null);
    }
}
