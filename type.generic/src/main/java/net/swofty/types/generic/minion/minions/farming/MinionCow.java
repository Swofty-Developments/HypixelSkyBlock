package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.mob.mobs.MobCow;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionCow extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 128,
                        "c2fd8976e1b64aebfd38afbe62aa1429914253df3417ace1f589e5cf45fbd717",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 26, 256,
                        "e4273e1870f9fc54358f7193b7fa3f27fb7bac1d68c9941f63f3c588337b70",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 24, 256,
                        "9c12694906b281c988312cf0575d93274c178a0449b71eff047de1eeb01e3b64",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 24, 384,
                        "e7b32af9f116a425c7394d23dd851f3bff53f05ec413fb2fce3839533d925a86",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 22, 384,
                        "7b412e13e1eba6d84336aee778115f183b88cbbe546b83ea64c5b6295145355a",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 22, 576,
                        "a63ba85ccc57534108199cb2034826d9853e40df3a8edaf6452326b73748e22a",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 20, 576,
                        "fd9cb1a9c54e00d1030a101c961f1f516c145b719f4ec8e7d4ab3c9759ae10f3",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 20, 768,
                        "9e28cd7376398c57887bc326c14c04c9c5796f613d7de9565d5e66c5b12c4d41",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 17, 768,
                        "cfa251097580c0d8d26e93e446f28469ae7b5f1208e559626683b4a5ecf5e0e2",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 17, 960,
                        "3e3f56f3924106eb91414a8859e76b0962395dffaeb91ebda538332fd9774cea",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 13, 960,
                        "cbe1ed84b41681fff45a60cb57b884e6bf4ecc23df2aa6cb112f74d3cb52e315",
                        Material.DIAMOND_SWORD, true),
                new MinionTier(12, 10, 960,
                        "c28e7f8242d3127654d15eadceed3fd0d237a99e4c2671a094e1d601fb1d9e1a",
                        Material.DIAMOND_SWORD, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(89,56,7);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(198,191,180);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(89,56,7);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(() -> new MobCow(EntityType.COW));
    }
}
