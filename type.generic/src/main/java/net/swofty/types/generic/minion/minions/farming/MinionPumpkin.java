package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMinePumpkinOrMelonAction;

import java.util.List;

public class MinionPumpkin extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 32, 64,
                        "f3fb663e843a7da787e290f23c8af2f97f7b6f572fa59a0d4d02186db6eaabb7",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 32, 192,
                        "95bcb44bbeaec7c903d4f37273ff6a20bd40f240dfbefc4aaf25cb4b0a25f3c4",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 30, 192,
                        "6832fd793f38e20265cfef3d979289493b951e0b5fb53511984bf500b6ad64ca",
                        Material.STONE_HOE, true),
                new MinionTier(4, 30, 384,
                        "6656b05400537c47b3e986697e5af027ed36adf7c80d9edaec6b48cb1af9f99b",
                        Material.STONE_HOE, true),
                new MinionTier(5, 27, 384,
                        "16685cf51ab0e08e842a822ca416225df3c583b32110bf4d778ad69f3f604b43",
                        Material.STONE_HOE, true),
                new MinionTier(6, 27, 576,
                        "ee1807903ca846a732ea46e9490b752a2803f75017a3008808c48437cfd8827f",
                        Material.IRON_HOE, true),
                new MinionTier(7, 24, 576,
                        "cda682c874c482e9e659e37fe3e8399c5f3c4f6237f0656071af5ffaf418ea9a",
                        Material.IRON_HOE, true),
                new MinionTier(8, 24, 768,
                        "bb72233f28cb814aadb63f0688033e7317907cf43f015097e493a578a3f50222",
                        Material.IRON_HOE, true),
                new MinionTier(9, 20, 768,
                        "2cc1a47302e055e561b06daede35f84a04829bc899af03d5603b78e55269c402",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 20, 960,
                        "7e246ead094174d265eb03222417dd4ed1d1a6a5ad33d77ed2578ab55eed3a37",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 16, 960,
                        "4c6a48f079ef70d84df10332bb0f2bf038d8e0e82ac36734823fb4b4a50705e4",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 12, 960,
                        "73b4ac77a32521c637033e7b5df8100e24817cf84acbaa2d43aa16103f8d1bf9",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(252,174,47);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(252,174,47);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(252,174,47);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public MinionAction getAction() {
        throw new RuntimeException("Not implemented yet");
    }
}
