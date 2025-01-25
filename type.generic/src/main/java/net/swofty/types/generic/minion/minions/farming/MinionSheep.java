package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.mob.mobs.MobSheep;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionSheep extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 24, 128,
                        "fd15d4b8bce708f77f963f1b4e87b1b969fef1766a3e9b67b249c59d5e80e8c5",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 24, 256,
                        "deaee0de135a24a27b8920ddc1c7b58314ffaba3ef3f4cf0d77195936d471c20",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 22, 256,
                        "c33da48269f28698c4548c1dbb8773f8e49888afd93af5f5b420e0f43c39f2eb",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 22, 384,
                        "3bd2c5fe2fe9be577c9034d3abfdbd3e90c697deebf9cd35107786bd4dd0555b",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 20, 384,
                        "f7b64375097693c11215acd72c429d2770e746178aa4014066285974fdacdaa6",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 20, 576,
                        "aea49ac4e8f88bbf0321b55ed264df0d527952ce49c387fdebdedc5d6447376",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 18, 576,
                        "d23301e0358c2c33e55011cec3a848c6cb4f3c8a016968ffc55190ff2d813c85",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 18, 768,
                        "f04de71765e46be9bcdb6c499f954c9bc831563d52776c593c16c99113bcb2d9",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 16, 768,
                        "eea074e9e53cb179da2ebd625de042b70cb0d8cc7280fc101c7cafb9abe07680",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 16, 960,
                        "3f92d454109855656d16061c8947760ce84a9561863481292ce8aa60b981911c",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 12, 960,
                        "6abba939e3a292203108d09da6a867dcf77cef01a5e6e77bcf9cfac5360b0e88",
                        Material.DIAMOND_SWORD, true),
                new MinionTier(12, 9, 960,
                        "349036ca4884a6b29b031b792f5f39f9b9141d95bd4082c31b9ac3f674faa2e4",
                        Material.DIAMOND_SWORD, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(156,252,252);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(156,252,252);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(156,252,252);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(() -> new MobSheep(EntityType.SHEEP));
    }
}
