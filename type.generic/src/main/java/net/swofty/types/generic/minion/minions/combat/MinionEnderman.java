package net.swofty.types.generic.minion.minions.combat;

import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionKillMobAction;

import java.util.List;

public class MinionEnderman extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 32, 64,
                        "e460d20ba1e9cd1d4cfd6d5fb0179ff41597ac6d2461bd7ccdb58b20291ec46e",
                        Material.WOODEN_SWORD, true),
                new MinionTier(2, 32, 192,
                        "e38b1bacbce1c6fa1928a89d443868a40a98da7b4507801993b1ab9bb9115458",
                        Material.WOODEN_SWORD, true),
                new MinionTier(3, 30, 192,
                        "2f2e4d0850b0d87c0b6a2d361b630960ff9165a47893c287eddf3eda2caa101b",
                        Material.STONE_SWORD, true),
                new MinionTier(4, 30, 384,
                        "2b37ae94f463c642d7c0caf3da5b95b4b7568c47daad99337ecefdeb25be5d9d",
                        Material.STONE_SWORD, true),
                new MinionTier(5, 28, 384,
                        "9dd3f4532c428d0589bac809463b76e15e6fa31bccd2d5e350aa7d506b792904",
                        Material.STONE_SWORD, true),
                new MinionTier(6, 28, 576,
                        "89f50d3955bec550def51df0e4e143cda3d71314f9a7288dd92e0079605b5363",
                        Material.IRON_SWORD, true),
                new MinionTier(7, 25, 576,
                        "368c2e2d9827cb25bf4add695f668180bb2b52d41342f175bdfeb142f960d712",
                        Material.IRON_SWORD, true),
                new MinionTier(8, 25, 768,
                        "84c91f6c71b6f75b7540134cb4d36b7e3c5ff8f26b6919a7410fe3427663b7dd",
                        Material.IRON_SWORD, true),
                new MinionTier(9, 22, 768,
                        "c70a920c4940a1ffaebcc20b87afaaf0b17ebc4d3b1c34dfd0374a0a583de32d",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(10, 22, 960,
                        "ecaf73a2cd819331d8096caf2f83f65db119692f0600c02d48081ceacf0c864c",
                        Material.GOLDEN_SWORD, true),
                new MinionTier(11, 18, 960,
                        "86906d7f34af69a797ddf5b5a5b1c428f77284451c67e788caf08070e3008ad",
                        Material.DIAMOND_SWORD, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(34,7,45);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(34,7,45);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(34,7,45);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(1, Block.GRASS_BLOCK)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionKillMobAction(EntityType.ENDERMAN);
    }
}
