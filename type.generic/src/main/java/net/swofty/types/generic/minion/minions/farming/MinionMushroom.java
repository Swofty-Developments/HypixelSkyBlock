package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionMushroom extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 30, 128,
                        "4a3b58341d196a9841ef1526b367209cbc9f96767c24f5f587cf413d42b74a93",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 30, 256,
                        "645c0b050d7223cce699f6cdc8649b865349ebc22001c067bf41151d6e5c1060",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 28, 256,
                        "a8e5b335b018b36c2d259711bee83da5b42fcc55ec234514ae2c23b1e98d7e77",
                        Material.STONE_HOE, true),
                new MinionTier(4, 28, 384,
                        "80970ebf76d0aa52a6abb7458a2e3917d967d553def9174a8b83697a10f4e339",
                        Material.STONE_HOE, true),
                new MinionTier(5, 26, 384,
                        "268b2d44457a92988400687d43e1562e0cb2ed1667ef0e62ed033a2881723eb4",
                        Material.STONE_HOE, true),
                new MinionTier(6, 26, 576,
                        "ac8772dfd110ef66d5eb3957046834313adbf035f36352f2426be2802c1a21d8",
                        Material.IRON_HOE, true),
                new MinionTier(7, 23, 576,
                        "9fd3d54f0eb2570ffd254bcbff3b3c076521ed896118d984fb66db154d4a5466",
                        Material.IRON_HOE, true),
                new MinionTier(8, 23, 768,
                        "dd360de8da7f050cedad36e91f595577622a2ae9db32f622b745c47f35dc012e",
                        Material.IRON_HOE, true),
                new MinionTier(9, 20, 768,
                        "e785f2fb94555998b380d19deffe120eb8dbd1191b0927312221e1f4f762a87d",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 20, 960,
                        "9ff2be74b7aad963d3f7bad59d2f9cda1337c3d00af0bcd6e314ba1fe348dfae",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 16, 960,
                        "74e69059cb27b7b7c65b5543db19aa153a2509a0090719e5199f1082acc1b051",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 12, 960,
                        "d940a7296f4e72b15f7eb2afe92b0ede089dcbd5fe786d2726e7794597b95f4b",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(198,21,77);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(198,21,77);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(198,21,77);
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
