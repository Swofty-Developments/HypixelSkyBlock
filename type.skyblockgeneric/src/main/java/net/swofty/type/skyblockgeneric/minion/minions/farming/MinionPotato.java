package net.swofty.type.skyblockgeneric.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;

import java.util.List;

public class MinionPotato extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 20, 64,
                        "7dda35a044cb0374b516015d991a0f65bf7d0fb6566e350496642cf2059ff1d9",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 20, 192,
                        "6ce06fb5d857f1b821b4f6f4481464b2471650733bf7baa3e1f6b41555aab561",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 18, 192,
                        "29e3d309d56d37b51f4a356cba55fec4ac8e174bf2b72a03fb8361a2b41da17d",
                        Material.STONE_HOE, true),
                new MinionTier(4, 18, 384,
                        "72fd7129e7831c043447a8355e78109431e7ca19959ef79dcc7a4c8f0a4ccf77",
                        Material.STONE_HOE, true),
                new MinionTier(5, 16, 384,
                        "2033a6e541525d523fe25da2c68a885ba1c2449362d0b35a68c95b69e8a28c87",
                        Material.STONE_HOE, true),
                new MinionTier(6, 16, 576,
                        "2aea4e3ef5782f4cb6e0d38e8d871221d29197cb186aca0e144922e7cd2e1224",
                        Material.IRON_HOE, true),
                new MinionTier(7, 14, 576,
                        "6a1812e4f58f1ec46c608521fc5f51eb2e653bbc4e43cd9e89dff88e8c777e",
                        Material.IRON_HOE, true),
                new MinionTier(8, 14, 768,
                        "9788f69ebdb3030054feff365e689bb5b10a867f52f9873bc952fc26b54d48ff",
                        Material.IRON_HOE, true),
                new MinionTier(9, 12, 768,
                        "b84792c8674b964f708b880df7d175631b9b6d9b5362353362ca997e727e1189",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 12, 960,
                        "e05c2ab7f41ca1f3f221b949edc7b20b800ed3bbaeb36514eb003887338f960",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 10, 960,
                        "57441fa19d89d5df902c07586c084f1b00c4ca06ca4cc2ec5b6230d1a5199811",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 8, 960,
                        "5cecf2e03258448f46849443ede1b2f7e74429f6ec5a0231e1f29befe4c6ec66",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(242,229,92);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(242,229,92);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(242,229,92);
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
