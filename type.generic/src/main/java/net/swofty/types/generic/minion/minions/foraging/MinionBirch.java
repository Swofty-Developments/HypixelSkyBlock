package net.swofty.types.generic.minion.minions.foraging;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionBirch extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of( //skins done
                new MinionTier(1, 48, 64,
                        "eb74109dbb88178afb7a9874afc682904cedb3df75978a51f7beeb28f924251",
                        Material.WOODEN_AXE, true),
                new MinionTier(2, 48, 192,
                        "6dd53989833505625fa9fc5ce5d4c8a745f25201e58d56cc6f94125c78606a91",
                        Material.WOODEN_AXE, true),
                new MinionTier(3, 45, 192,
                        "6ed87a6d743d9e036b169b03973c5772b611db48f5c6844f1f427ffa702c12ef",
                        Material.STONE_AXE, true),
                new MinionTier(4, 45, 384,
                        "ac49f5616584ddb09b46e2d9eba91228c5c55d81dd557c8bf84f7ead7e74578a",
                        Material.STONE_AXE, true),
                new MinionTier(5, 42, 384,
                        "1a1fb86ed5a7d5bddcee9593eed7142f68b4fb55a8b812d0bfaa765e2162138d",
                        Material.STONE_AXE, true),
                new MinionTier(6, 42, 576,
                        "7b79821acb2d8dd8bc54ac77ee6486d6bd21f5e20c828f84973325d6b3f2eb41",
                        Material.IRON_AXE, true),
                new MinionTier(7, 38, 576,
                        "292863ce28af7319e7181be85be55c43be21d3efba789f4768cffaefd488206f",
                        Material.IRON_AXE, true),
                new MinionTier(8, 38, 768,
                        "8f85e3656474430d5cca86f73c474aa647d78594791fcd5acb8d637f60133164",
                        Material.IRON_AXE, true),
                new MinionTier(9, 33, 768,
                        "5e07676b749e912c6299bdb05904aba8fc6df91eb9494376957fcf0f745be295",
                        Material.GOLDEN_AXE, true),
                new MinionTier(10, 33, 960,
                        "d0d6563ad8a3f57870674b7ed87069401016be21cc43625850197db8d299482d",
                        Material.GOLDEN_AXE, true),
                new MinionTier(11, 27, 960,
                        "c7461229df076f8137a4560b38365ae48430b01070b90221aa5846284c17b876",
                        Material.DIAMOND_AXE, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255,155,0);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(255,255,0);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(255,255,0);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.BIRCH_LOG, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.BIRCH_LOG);
    }
}
