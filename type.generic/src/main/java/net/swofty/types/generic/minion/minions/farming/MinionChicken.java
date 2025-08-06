package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.mob.mobs.minionMobs.MobMinionChicken;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionChicken extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 192,
                        "a04b7da13b0a97839846aa5648f5ac6736ba0ca9fbf38cd366916e417153fd7f",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 320,
                        "7ae39f29a0cc4d8ac8277e7a4e6d56b0e42f04267a9f9033fcba109751ebfff5",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 320,
                        "2fdacd78fce2c6c70cd020dd0cf69481582d97796abcda0a282e1f7e1a9ab6f3",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 448,
                        "c968476a306df54c26053b639de69e1473b5b453a4f84cf371f675ba794314da",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 448,
                        "597ca4daa25ad8a48eb0a34a23000971f87fe42319c32375c21dea940ffffd5e",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "7a6ed3e94cc354164f759c448f39cc0ac0ee50feae2e4008e26c890a8387f7e",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "c1c9ed510850622947e215dbd9b018939a7908595c645c8415fc8e4e5ce714d",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "c3812cb86fe22971d0ae58789f18a1d208116cb204329aff7905aa3993b0d0d8",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 18, 768,
                        "9f24c0d1e3aa3c2999a1268fcc0f933591a9910437f082b1d5dc9bed7ee1a753",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 18, 960,
                        "4212ce883dfd2bec43e6cd9b7a7f86be1cca8ebceb33b83e3e70ad873717be18",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 15, 960,
                        "d5c12fd3968d389f6d053b1a7a85dc1cfb90a39385c379e3afee6295aaafcd37",
                        Material.DIAMOND_SWORD, true),
                new MinionTier(12, 12, 960,
                        "fa39abe8d2cb1023a3b1be527d2da51585735d3cea2b71dffafe555af6387b79",
                        Material.DIAMOND_SWORD, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(254,255,252);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(254,255,252);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(254,255,252);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(MobMinionChicken::new);
    }
}
