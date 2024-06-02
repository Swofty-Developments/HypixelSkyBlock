package net.swofty.types.generic.minion.minions.foraging;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionDarkOak extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of( //skins done
                new MinionTier(1, 48, 64,
                        "5ecdc8d6b2b7e081ed9c36609052c91879b89730b9953adbc987e25bf16c5581",
                        Material.WOODEN_AXE, true),
                new MinionTier(2, 48, 192,
                        "b25860cc1423ab010cf17697b288fdd3f5cb725ea9ab3e88a499dc1938104b02",
                        Material.WOODEN_AXE, true),
                new MinionTier(3, 45, 192,
                        "2ecb65fceae74d76106b02eaa31bd80cc26b3f88d32372b645658d337352b42",
                        Material.STONE_AXE, true),
                new MinionTier(4, 45, 384,
                        "2ecb65fceae74d76106b02eaa31bd80cc26b3f88d32372b645658d337352b42",
                        Material.STONE_AXE, true),
                new MinionTier(5, 42, 384,
                        "cf0969d586970c7ed5fef0c44d2899cfc97780488a36d725d55a6569dd02fa3c",
                        Material.STONE_AXE, true),
                new MinionTier(6, 42, 576,
                        "299b2d8c62b17108023c57e2bc40873446e1b96f11674a2bb2a27f915cf9d519",
                        Material.IRON_AXE, true),
                new MinionTier(7, 38, 576,
                        "3ee074f5bb1680686d0794506c6c26e8f6acf1117b015ad3441aa938c9dcc8d",
                        Material.IRON_AXE, true),
                new MinionTier(8, 38, 768,
                        "fd20485516e15e9c7ade2529848ebee04a9242fea2e2eefa4b336e7bd9177af1",
                        Material.IRON_AXE, true),
                new MinionTier(9, 33, 768,
                        "c0cde69130063d80dcd974d96ac02af355deeb1a5391fa14cbabecb530924ad3",
                        Material.GOLDEN_AXE, true),
                new MinionTier(10, 33, 960,
                        "9fc5b2ee7d07de80538e77d651c9190eeafea9ef3dfe094589f70117c4d4ed07",
                        Material.GOLDEN_AXE, true),
                new MinionTier(11, 27, 960,
                        "23c650b69189a1da2a0a9e9d0a235cb89df0f32ab421ad059e012be59638057f",
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
                new MinionExpectations(-1, Block.DARK_OAK_LOG, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.DARK_OAK_LOG);
    }
}
