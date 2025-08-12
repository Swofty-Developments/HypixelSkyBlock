package net.swofty.type.skyblockgeneric.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.generic.entity.mob.mobs.minionMobs.MobMinionRabbit;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;
import net.swofty.type.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionRabbit extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 192,
                        "ef59c052d339bb6305cad370fd8c52f58269a957dfaf433a255597d95e68a373",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 320,
                        "95beb50764cd6b3bd9cdad5c8788762dde5b8aca1cd47b9ebdeaf4ab62046022",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 320,
                        "4caf38c59c689f162a1fedba239a6e44fd6c65c103038c91c0d32e5713a0694c",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 448,
                        "a5253184a1665ef0e1ab9da27dcfff2bdbde45836e5b26fc860cee9c2eccf741",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 448,
                        "cd465a0e504286b0dcea425e599e8296c442138cefcea98c76cd966fe53d0639",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "1fe6e315e28258a79ec55c5a12f2ec58fe3fa3b735517779eaa888db219f305b",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "c110ae6f601c71a6a779a2943a33546dc08adaac4fdfd54cfc4a98aa90ca12fb",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "e553b809b5164816aa61d5e39f8998d59fac4a35ff01c54d8a16b16627b06403",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 17, 768,
                        "26e6ecd9f7dfd5ee99a7964e0e404953a29907acca4d6b165aa2ef9807119fe0",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 17, 960,
                        "3ccfa391def65b86e90f1938c98f1dc5874e9cc94e3eefce91ba40a202de4e69",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 13, 960,
                        "7f3fdd04826405dec5c17d0f688e874e7ba9bfbdead28b7ed5a0463335629697",
                        Material.DIAMOND_SWORD, true),
                new MinionTier(12, 10, 960,
                        "94797e92904c64e23124f17da3fcb312d4da6654610f21f61528d7a87503897c",
                        Material.DIAMOND_SWORD, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(242,252,199);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(242,252,199);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(242,252,199);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(MobMinionRabbit::new);
    }
}
