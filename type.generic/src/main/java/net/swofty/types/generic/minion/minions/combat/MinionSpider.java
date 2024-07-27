package net.swofty.types.generic.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionSpider extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 128,
                        "e77c4c284e10dea038f004d7eb43ac493de69f348d46b5c1f8ef8154ec2afdd0",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 256,
                        "c9a88db53bdf854c29d91b09027904684a6ba638d8007b7ad142a7321b9a212",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 256,
                        "6be5128d61371acc4eabd3590013a5b8bfc678366e61c5363bf41a8b0154efdc",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 384,
                        "4ef774366eef0ae26c9da09f52d101fd3a6181f62c059d579900d33098968058",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 384,
                        "eeb537b6d278623a110b4d31784ae415789972fad78bec236aa36f3a5f43a856",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "dc785f2b1cca983928b0fe8ceb700660365b757e93a42a6651a937df773c70af",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "887abd32a5aae5870821fe0883002cdad26a58f9fee052c7ab9b800ee6e9ac52",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "4781e95aeb0e31be71e64093a084602de754f3e50443d5a4f685aac00d7a662f",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 17, 768,
                        "fe3869503b7fdeaa063351fd3579dbaf6fd3592bd4c30bac1058c9367c6a3823",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 17, 960,
                        "5a4209e45b623b8338bcd184f15d279558c8a9d756d538e1584911780a60697a",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 13, 960,
                        "62d0262788369b6734e65d0240185affc2ead224a07efbcd70e4c7125d2c5330",
                        Material.DIAMOND_SWORD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(137,103,11);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(137,103,11);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(137,103,11);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(1, Block.GRASS_BLOCK)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(EntityType.SPIDER);
    }
}
