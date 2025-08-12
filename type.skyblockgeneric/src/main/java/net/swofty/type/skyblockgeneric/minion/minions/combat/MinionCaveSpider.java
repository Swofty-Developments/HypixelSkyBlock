package net.swofty.type.skyblockgeneric.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;

import java.util.List;

public class MinionCaveSpider extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 128,
                        "5d815df973bcd01ee8dfdb3bd74f0b7cb8fef2a70559e4faa5905127bbb4a435",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 256,
                        "677fb9a717ec81d45f89b73f0acd4ee03c9e396601a2de60f2719e4458c7325b",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 256,
                        "7f4912b76e599d12e73e4b03ee51a105999ad1306709fbffcfbaed556a9d7eb0",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 384,
                        "3d90f56d6e1632c00c14d568036aa536073c6a4a7e5759e012bd46d9f3809086",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 384,
                        "c682c74ba44a5221a70f98188e76a4e88e41f633363a54af1d26247423130636",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "b54735acf9c010f2d25d7af70d600d8bc2633729a4fde7b4ac248c211135f3ab",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "729095202ca3cd63556e3549f71c39aae4b6718170de19067d6819be4ddecd6e",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "5c4ec7d3c5084a5c91bdf3fba196a1d12d5bf71049b61b97dd1c5854617a41cf",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 17, 768,
                        "42654f0248464e23cf70811a1b1665cad19aa207857f05452967f860458a4c64",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 17, 960,
                        "4cded81400f3ced561bed776bd44b48e784f7a810ba6cd6340d26f4c00a0c50f",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 13, 960,
                        "36303fc7e2046822ec79a95ce5c7350e58dabd8d776e5c36669f5404581d0459",
                        Material.DIAMOND_SWORD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(89,96,94);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(89,96,94);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(89,96,94);
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
