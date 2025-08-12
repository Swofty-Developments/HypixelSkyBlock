package net.swofty.type.skyblockgeneric.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;

import java.util.List;

public class MinionMagmaCube extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 32, 64,
                        "18c9a7a24da7e3182e4f62fa62762e21e1680962197c7424144ae1d2c42174f7",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 32, 192,
                        "212ff47f5c8b38e96e940b9957958e37d610918df9b664b0c11bd6246799f4af",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 30, 192,
                        "376d0b9eb9e5633d21424f6eaade8bd4124b9c91f3fa1f6be512fe0b51d6a013",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 30, 384,
                        "69890974664089d1d08a34d5febead4bb34508f902aa624e0be02b61d0178b7f",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 28, 384,
                        "5a74333ed5c54aef95aead60c21e541131d797d3f0d7a647915d7a03bbe4a5fe",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 28, 576,
                        "5de0153aa18d34939b7d297c110e7a207779908cee070e3278a3d4dc9e97b122",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 25, 576,
                        "bf77572393b4b420559f17a56cb55f9ec47c3e9958403184699dba27d12f3ef2",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 25, 768,
                        "365c702393988e0312f56c00c6e73c8cf510b89df05ad766a65b36a1f281b604",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 22, 768,
                        "76101f4bb000518bbedc4b1147a920a99f141b8a679f2984fb94741a33eed69f",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 22, 960,
                        "e9e67c3860cc1d36cb4930e0ae0488c64abc4e910b4224dc9160d273c3af0bba",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 18, 960,
                        "6ab2af6b08c3acedd2328e152ef7177f6bbb617dc985dfbfecdc982e04939b04",
                        Material.DIAMOND_SWORD, true),
                new MinionTier(12, 16, 960,
                        "836abe87bbab593c944975526e636c038829dd9f9d1d94db65838c2688b2fd40",
                        Material.DIAMOND_SWORD, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(216,91,2);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(216,91,2);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(216,91,2);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        throw new RuntimeException("Not implemented yet");
    }
}
