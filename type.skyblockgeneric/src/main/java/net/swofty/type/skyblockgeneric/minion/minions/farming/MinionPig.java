package net.swofty.type.skyblockgeneric.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.item.Material;
import net.swofty.type.generic.entity.mob.mobs.minionMobs.MobMinionPig;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;
import net.swofty.type.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionPig extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "a9bb5f0c56408c73cfa412345c8fc51f75b6c7311ae60e7099c4781c48760562",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 192,
                        "13d136654297e744ccb3ba71bb85bd7653267db4b9b940b621be587d52a51310",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 192,
                        "d0215bbadccec19fc11b04d10958eedea0cb2957479d60d092fcb7339e0d3a3d",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 384,
                        "8a591979d1f27c834b837482ff077dd6ae60603af1d42efd54fd0fe423f473b2",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 384,
                        "c6dcf14cfaee6c9a5aef79f7cfe7f0a05f6d1d51c0ae9f93e44945a99d7b67e9",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "d3054be358caefe2b9c049159144dbd94de0bdefab4fa07472d8d8f3b22a1edc",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "73c5582b39fc6c08d4adc8c27bd7b9fc1340073ace1d5571276f57bfc852d864",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "6be861ec200f4741fb5a202c31b94c345417b7b85bc3e5dd595fdedf387a5559",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 17, 768,
                        "caa9f8b050d5f71bb638398af11fe0c6f523251b4d8ff262979248933e2ac7b1",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 17, 960,
                        "4a466ca591bfe16022be2a6f8aeb2c6321913fd6ad5cb9f40f5e0058521b0d3a",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 13, 960,
                        "9281a6db6bec7d3d5f05f3bbec4eca94ba2073863b0ec2fa853c0c8f28c97629",
                        Material.DIAMOND_SWORD, true),
                new MinionTier(12, 10, 960,
                        "f474577cd67de5f86679370a0d95a00482a9b04e13533783b7ccc427be562696",
                        Material.DIAMOND_SWORD, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(249,114,222);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(249,114,222);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(249,114,222);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(MobMinionPig::new);
    }
}
