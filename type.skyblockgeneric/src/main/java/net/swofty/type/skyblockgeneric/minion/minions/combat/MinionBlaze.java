package net.swofty.type.skyblockgeneric.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;

import java.util.List;

public class MinionBlaze extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 33, 64,
                        "3208fbd64e97c6e00853d36b3a201e4803cae43dcbd6936a3cece050912e1f20",
                        Material.BOW, true),
                new MinionTier(2, 33, 192,
                        "ffcc301b04b1537f040d53fd88a5c16e9e1fde5ea32cd38758059a531b75cb46",
                        Material.BOW, true),
                new MinionTier(3, 31, 192,
                        "da5e196586d751ba7063bcf58d3dc84121e746288cb3c364b4b6f216a6492a27",
                        Material.BOW, true),
                new MinionTier(4, 31, 384,
                        "6ddae5fcdd5ede764f8fe9397b07893ccf3761496f8e2895625581ce54225b00",
                        Material.BOW, true),
                new MinionTier(5, 28.5f, 384,
                        "f5e3a84c9d6609964b5be8f5f4c96800194677d0f8f43d53a4d2db93dbb66fad",
                        Material.BOW, true),
                new MinionTier(6, 28.5f, 576,
                        "e9d7db90d3118ef56c166418a2232100fb4eb0ab5403548cfa63e985d5e0152c",
                        Material.BOW, true),
                new MinionTier(7, 25, 576,
                        "a9bdeb530d09ee73479db19b357597318eac92ee7855740e46a1b97ae682b27",
                        Material.BOW, true),
                new MinionTier(8, 25, 768,
                        "d7fc92fa962d0944ce46b71bc7dcb73a5f51f9d8a7e2bcccf666f2da05a0152d",
                        Material.BOW, true),
                new MinionTier(9, 21, 768,
                        "a2a246dbcc45be4a936a19b44fcb61725c0fe2372a0ce0676fb08fd54d4d899b",
                        Material.BOW, true),
                new MinionTier(10, 21, 960,
                        "ea357aeaf75a8cfed2b3c1c8f3ccf54f907ae2b64fa871cf201baeef53528e19",
                        Material.BOW, true),
                new MinionTier(11, 16.5f, 960,
                        "e791eb26b39f162f552d539a4d22c4bee8aa9c571d9acf82a012593bb945c360",
                        Material.BOW, true),
                new MinionTier(12, 15, 960,
                        "c64b157a9dffd66a8fe832f8564c2c7822279cdda344dfbef23f84c65ef625d1",
                        Material.BOW, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(244,203,36);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(244,203,36);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(244,203,36);
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
