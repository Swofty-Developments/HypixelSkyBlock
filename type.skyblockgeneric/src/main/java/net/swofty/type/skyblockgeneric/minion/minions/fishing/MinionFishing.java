package net.swofty.type.skyblockgeneric.minion.minions.fishing;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;
import net.swofty.type.generic.minion.actions.MinionFishingAction;

import java.util.List;

public class MinionFishing extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 78, 640,
                        "53ea0fd89524db3d7a3544904933830b4fc8899ef60c113d948bb3c4fe7aabb1",
                        Material.FISHING_ROD, true),
                new MinionTier(2, 75, 640,
                        "8798c0d7b65bfa5f56b084c1f51767a4276ad9f2c60bcb284dc6eccb7281e2ab",
                        Material.FISHING_ROD, true),
                new MinionTier(3, 72, 640,
                        "c8cefef2d7268a5170dd86fd782d6fee06f063b0a223e86378dde3d766c19929",
                        Material.FISHING_ROD, true),
                new MinionTier(4, 72, 704,
                        "cdfb98800f7d01c56744fa116d2275090a337334f6f884230522f8ea3964c9e0",
                        Material.FISHING_ROD, true),
                new MinionTier(5, 68, 704,
                        "5eb079ce77840f08fb170aad0a89827695d92a6ccca5977f48c43fe931fd22f7",
                        Material.FISHING_ROD, true),
                new MinionTier(6, 68, 768,
                        "db557d80642ccd12c417a9190c8d24b9df2e797eb79b9b63e55c4b0716584222",
                        Material.FISHING_ROD, true),
                new MinionTier(7, 62.5f, 768,
                        "db557d80642ccd12c417a9190c8d24b9df2e797eb79b9b63e55c4b0716584222",
                        Material.FISHING_ROD, true),
                new MinionTier(8, 62.5f, 832,
                        "a5ee01b414c8e7fb1f55d8143d63b9dfed0c0428f7de043b721424c4a84eded3",
                        Material.FISHING_ROD, true),
                new MinionTier(9, 53, 832,
                        "204b03b60b99d675da18c4238d3031b6139c3763dcb59ba09129e6b3367d9f59",
                        Material.FISHING_ROD, true),
                new MinionTier(10, 53, 896,
                        "593aa3e4eaa3911456d25aab27ce63908fe7a57d880a55884498c3c6a67549b0",
                        Material.FISHING_ROD, true),
                new MinionTier(11, 35, 960,
                        "46efc2d1ebb53ed1242081f22614a7e3ac983b9f6159814e6bcbc73ce7e3132a",
                        Material.FISHING_ROD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(39,227,244);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(39,227,244);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(39,227,244);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.WATER, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionFishingAction();
    }
}
