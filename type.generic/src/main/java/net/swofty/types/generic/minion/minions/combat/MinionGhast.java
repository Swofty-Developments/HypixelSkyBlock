package net.swofty.types.generic.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionGhast extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 50, 64,
                        "2478547d122ec83a818b46f3b13c5230429559e40c7d144d4ec225f92c1494b3",
                        Material.BOW, true),
                new MinionTier(2, 50, 192,
                        "cd35bd7c4dd1792eeb85ee0a54645cd4e466c8b7b35d71dde4a4d51dfbbdb13f",
                        Material.BOW, true),
                new MinionTier(3, 47, 192,
                        "e1fb348c7c14e174b19d14c8c77d282f1abe4c792519b376cd0622a777b68200",
                        Material.BOW, true),
                new MinionTier(4, 47, 384,
                        "1b0c2e0852f7369ea7d3fe04eb17eff41bb35a1a8a034834369e1e624c79c03",
                        Material.BOW, true),
                new MinionTier(5, 44, 384,
                        "a3c5c52a4c945825e4c959c5cb6aa607a0e3a1bffd5cb6a0577e172d0f356a2b",
                        Material.BOW, true),
                new MinionTier(6, 44, 576,
                        "ef97eff2721dc201b23373afc3111eda22a325c08de6a14f03dcfcb98d3c9507",
                        Material.BOW, true),
                new MinionTier(7, 41, 576,
                        "5836df340405415ad7d8b84bbe0e806d0cfed990796c3ade38934169a48ebd25",
                        Material.BOW, true),
                new MinionTier(8, 41, 768,
                        "7f2e537ca12c9d8bd0ec6bd56ac8bdae86521e960b5852b0bbb31b2cc83dfc7e",
                        Material.BOW, true),
                new MinionTier(9, 38, 768,
                        "af4d8d82f4d86569c70d265f5cf62e46ee8dc0a5a6d97ef1901c793d0b127545",
                        Material.BOW, true),
                new MinionTier(10, 38, 960,
                        "4013d128e7116812388b789ec641d31d48bf10aa862f7d63d2b4fc0a03d147a2",
                        Material.BOW, true),
                new MinionTier(11, 32, 960,
                        "5840896c78884ebb35103b31ffd7276c941ea862b8b6b0e0810a66b4ed66cbc2",
                        Material.BOW, true),
                new MinionTier(12, 30, 960,
                        "e34cefdd0239b6d37fd74318e283181c4819f05c56352e6216c2d047c74f7007",
                        Material.BOW, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(198,242,232);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(198,242,232);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(198,242,232);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(1, Block.GRASS_BLOCK)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(EntityType.GHAST);
    }
}
