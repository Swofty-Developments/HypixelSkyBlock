package net.swofty.types.generic.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;

import java.util.List;

public class MinionCreeper extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 27, 64,
                        "54a92c2f8c1b3774e80492200d0b2218d7b019314a73c9cb5b9f04cfcacec471",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 27, 192,
                        "3fcf99ab9b31c2f0b7a7378c6936b63ac4a78857831729f08cca603925a5873b",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 25, 192,
                        "488b4089a835e276838bba45c79d1146f0c2341971170a6163e6493890fd1b83",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 25, 384,
                        "ac2d5f8dcfc9f35897f8b0a42ff0c19e483bdc745e7e64bf0aaf1054a6e67dd",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 23, 384,
                        "654bde9a26e35094e3438540c225cffa7690c1d4456251da30cc990ff921cc36",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 23, 576,
                        "b5f07bbd87cffad76aeb5337e74726e7fcbd96c2bd0dcb083f5fca5aec2e12a",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 21, 576,
                        "f6f95998dd76a3bd9ffe949e7a4fe993b4baa2e981f49bf7113417f51003b193",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 21, 768,
                        "8c0abba2be5c9a93362a7da3231aeea824c5c590bfaaaec78888f1b3d9d32adc",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 18, 768,
                        "21abd529c1898f6ec7e01d9943419c6358de93e0d6cdd2d90c8d63e7036db60d",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 18, 960,
                        "5699c6b6bc8adfa79e22ae51cc049fab2c7a51b686ca968df222cfa98faf92a",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 14, 960,
                        "70850cccb3dfb7fe4bb0f7a008d5b4c10c08f9e36998f6f44ae8c9bc1b1b8e01",
                        Material.DIAMOND_SWORD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(20,186,81);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(20,186,81);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(20,186,81);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(2)
        );
    }

    @Override
    public MinionAction getAction() {
        throw new RuntimeException("Not implemented yet");
    }
}
