package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionObsidian extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 45, 64,
                        "320c29ab966637cb9aecc34ee76d5a0130461e0c4fdb08cdaf80939fa1209102",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 45, 192,
                        "58348315724fb1409142dda1cab2e45be34ead373d4a1ecdae6cb4143cd2bd25",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 42, 192,
                        "c5c30c4800b25625ab51d4569437ad7f3e5f6465b51575512388b4c96ecbac90",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 42, 384,
                        "1f417418f6df6efc7515ef31f4db570353d36ee87d46c6f87a7f9678b1f3ac57",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 39, 384,
                        "44d4ae42f0d6e82c7ebf9877303f9a84c96ce1978a8ac33681143f4b55a447ce",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 39, 576,
                        "7c124351bd2da2312d261574fb578594c18720ac9c9d9edfdb57754b7340bd27",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 35, 576,
                        "db80b743fa6a8537c495ba7786ebefb3325e6013dc87d8c144ab902bbdb20f86",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 35, 768,
                        "745c8fc5ccb0bdbc19278c7e91ad6ac33d44f11fae46e1bfbfd1737ec1e420d4",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 30, 768,
                        "15a45b66c8e21b515ea25abf47c9c27d995fe79b128844a0c8bf7777f3badee5",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 30, 960,
                        "1731be266b727b49ad135b4ea7b94843f7b322f873888da9fe037edea2984324",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 24, 960,
                        "4d36910bcbb3fc0b7dedaae85ff052967ad74f3f4c2fb6f7dd2bed5bcfd0992b",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 21, 960,
                        "d80c2415787ef06c5aee6df1d60b91283cbf02dc22c48a7cd28e652b45f21d32",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(59, 49, 86);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(59, 49, 86);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(59, 49, 86);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.OBSIDIAN, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.OBSIDIAN);
    }
}
