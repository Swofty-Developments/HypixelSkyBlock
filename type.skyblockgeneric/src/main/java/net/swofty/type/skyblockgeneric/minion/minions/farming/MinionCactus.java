package net.swofty.type.skyblockgeneric.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.minion.actions.MinionMineAction;

import java.util.List;

public class MinionCactus extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 27, 64,
                        "ef93ec6e67a6cd272c9a9684b67df62584cb084a265eee3cde141d20e70d7d72",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 27, 192,
                        "d133a6e56ac05d1cfa027a564c7392b04c2cffda3e57c70b06ed5ae1d73ca6fe",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 25, 192,
                        "1b35a27732a2cb5ee36a52653b2e7d98bdd9d3d799499035c9f918344570c9e8",
                        Material.STONE_HOE, true),
                new MinionTier(4, 25, 384,
                        "6569e2f7104a423362844fdd645c3a9d2b8f8c1b8979d208ec20487ac2a5c783",
                        Material.STONE_HOE, true),
                new MinionTier(5, 23, 384,
                        "c4fe9efb395689a9254fea06d929c3c408a5b314084399b386c009ca83a062a9",
                        Material.STONE_HOE, true),
                new MinionTier(6, 23, 576,
                        "ac8a964da5cc050812171dce6e9937191e4e7b65b7eb5f27e1846f868c023f58",
                        Material.IRON_HOE, true),
                new MinionTier(7, 21, 576,
                        "2c1b5f3b3ffb6a8983f5110d4fd347df6086205bcedd3e065cbdf9ee47f957fe",
                        Material.IRON_HOE, true),
                new MinionTier(8, 21, 768,
                        "2ddea64e86688c84d9edae63cf94765b9a5fac004e4babb3bfff081b30198327",
                        Material.IRON_HOE, true),
                new MinionTier(9, 18, 768,
                        "5a03ed5566128ca6d7911e2e1614450e28372e2b0513327689e183168edc5711",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 18, 960,
                        "f4272b51f991a088d3aae579b93d253efc2e6d9657f0299191e3a18ee89a22c0",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 15, 960,
                        "b1cabca262d9f98ccabf5546c033614f664173c6f206e626ca8b316d7962f8c8",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 12, 960,
                        "1259053579eb83072fe4d5a530b7a6a318a6e00355257205c1324166cba14ee0",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(89,242,104);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(89,242,104);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(89,242,104);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.CACTUS);
    }
}
