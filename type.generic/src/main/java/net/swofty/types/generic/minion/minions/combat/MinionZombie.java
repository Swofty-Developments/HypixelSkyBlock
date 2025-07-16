package net.swofty.types.generic.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.mob.mobs.minionMobs.MobMinionZombie;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionZombie extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "196063a884d3901c41f35b69a8c9f401c61ac9f6330f964f80c35352c3e8bfb0",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 192,
                        "c01613ba2e99ee8326b5ceae77efb1e9afa6ae541f38b4ed63e79ecb01e725f0",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 192,
                        "d6fdd8d54bc3a109b7e06baaf1b0ac97fb22989aa93069b63cca817ff7fd7463",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 384,
                        "bfbec1bd0fe3b71b9da9d7666fd6bbde341b4c481e8563fddf61f4ee52f7cd1b",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 384,
                        "67a1945b52761443d1a7de233a4e4aea40c9abad92ae9ac35e385478971956ae",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "a8c3ab42d327fa01271f9f19958c77e0dee9fde57415f873783737a1e83f4e86",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "5058f08910b39c30644f33fd71f81a412f6e05fe7c703a87fd4f3d5e4b2b6509",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "e40b20aba5b3c279dee42b39d8e03de25cbead3421655f0cf1bea43ed0b4272e",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 17, 768,
                        "fcbf17681e579f00d65f978c0b50915aaf2d5f609da7d9ab156cb6f092b88840",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 17, 960,
                        "6b4a9dc6d0fdbd1bad3613dcb3ab5c54c5ea5e0b498ee35b2fd30951cc2e9fcd",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 13, 960,
                        "6699ff5ce9a0f5032340596f6b2dd6ac7028fc7cc5b943d4c1fc2d3749fedcd6",
                        Material.DIAMOND_SWORD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(32,178,122);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(32,178,122);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(32,178,122);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(2)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(MobMinionZombie::new);
    }
}
