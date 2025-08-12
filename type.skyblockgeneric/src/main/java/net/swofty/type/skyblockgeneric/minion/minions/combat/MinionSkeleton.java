package net.swofty.type.skyblockgeneric.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;

import java.util.List;

public class MinionSkeleton extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "2fe009c5cfa44c05c88e5df070ae2533bd682a728e0b33bfc93fd92a6e5f3f64",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 192,
                        "3ab6f9c3c911879181dbf2468783348abc671346d5e8c34d118b2b7ece7c47c2",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 192,
                        "ccd9559dc31e4700aaf001e0e2f0bd3517f238af25decd8395f4621404ca4568",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 384,
                        "5b2df127315a583e767c6116f9c6ccdb887dc71fbe36ff30e0c4533db2c8514e",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 384,
                        "1605c73264a27d5c9339b8a55c830d288450345df37329023c13cdc4e4b46ccc",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "b51e887ab5c0966bb4622882e4417037c3eee8a2d0162e2e82bf295f0d1e1db2",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "40ad48abf6ae82b8bad2c8a1f1a0c40dea748c05922b7ff00f705b313329e1f1",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "1a81a52e837daa71fd05c9e4c37a9cad2e722f96779b574127072d30a98af582",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 17, 768,
                        "e0a8fae40ff866e3fb7d9131f50efb8bd870da92cdf11051af48fa394bfa19e2",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 17, 960,
                        "ed666149a1967b13df3341690c4c9a9f409b0f3b4f9ca8725d1969102ad420e0",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 13, 960,
                        "576255c781ebfb719d28f904813f69e20541d697f88bc6d96a6d4aa05b0fbc22",
                        Material.DIAMOND_SWORD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(222,226,225);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(222,226,225);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(222,226,225);
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
