package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionCarrot extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 20, 64,
                        "4baea990b45d330998cb0c1f8515c27b24f93bff1df0db056e647f8200d03b9d",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 20, 192,
                        "32a0a1695d50e0a9ced4b91edfd42afd41b4e737aa5d174c74b13963fb022556",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 18, 192,
                        "149dbae380e85f93d4c86f5097ec2ac3dec28389fb528a0d6a719fc6139626a8",
                        Material.STONE_HOE, true),
                new MinionTier(4, 18, 384,
                        "7399c0373eed7e12d5c212e13c51422e21d4ec7fa301f5a5c684f816a2eb2aab",
                        Material.STONE_HOE, true),
                new MinionTier(5, 16, 384,
                        "c56711e5002d0a7003f85cc2f59137da467648332baa630d939684580c5bbddb",
                        Material.STONE_HOE, true),
                new MinionTier(6, 16, 576,
                        "52bc4e06ec80d34fb5c419d743e7ccf313866a98dbd15d53a83db98a7bff8ff5",
                        Material.IRON_HOE, true),
                new MinionTier(7, 14, 576,
                        "c8c0dbbc8cc4bdc5d8483c732a61404ad12ab0ab7c49ff81cba2b709ae547923",
                        Material.IRON_HOE, true),
                new MinionTier(8, 14, 768,
                        "f3e5e690f2f78fd39efb4b0bf212bf68eb02d76936891238c8db2b4940d49313",
                        Material.IRON_HOE, true),
                new MinionTier(9, 12, 768,
                        "42c2ab452b92102b7ba030b81084d000e155beb616a026f95a11c654e96f4e28",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 12, 960,
                        "bdf031730f2f6bd8aaebfbc6e160723d294e03cc9545d98d9bdb84cfdf853266",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 10, 960,
                        "62858c422e0963f1b1da6196e9d47936acea449bea9f90e2dbf32f921f2522e",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 8, 960,
                        "2c459fc8849bd8f38c577907b9983fdbef406285437082bfa4845cd3d75065c9",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(237,194,109);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(237,194,109);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(237,194,109);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(1, Block.CARROTS, Block.DIRT)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.CARROTS);
    }
}
