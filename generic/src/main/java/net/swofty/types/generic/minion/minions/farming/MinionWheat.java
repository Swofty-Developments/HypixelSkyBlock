package net.swofty.types.generic.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionWheat extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 15, 64,
                        "425b8d2ea965c780652d29c26b1572686fd74f6fe6403b5a3800959feb2ad935",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 15, 192,
                        "f262e1dad0b220f41581dbe272963fff60be55a85c0d587e460a447b33f797c6",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 13, 192,
                        "b84f042872bfc4cc1381caab90a7bbe2c053cca1dae4238a861ac3f4139d7464",
                        Material.STONE_HOE, true),
                new MinionTier(4, 13, 384,
                        "8c87968d19102ed75d95a04389f3759667cc48a2ecacee8b404f7c1681626748",
                        Material.STONE_HOE, true),
                new MinionTier(5, 11, 384,
                        "c5ebd621512c22d013aab7f443862a2d81856ce037afe80fcd6841d0d539136b",
                        Material.STONE_HOE, true),
                new MinionTier(6, 11, 576,
                        "f4757020d157443e591b28c4661064d9a6a44dafe177c9bc133300b176fc40e",
                        Material.IRON_HOE, true),
                new MinionTier(7, 10, 576,
                        "2d2f9afcfada866a2918335509b5401d5c56d6902658090ec4ced91fea6bf53a",
                        Material.IRON_HOE, true),
                new MinionTier(8, 10, 768,
                        "1292ecec3b09fbbdffc07fbe7e17fa10b1ff82a6956744e3fa35c7eb75124a98",
                        Material.IRON_HOE, true),
                new MinionTier(9, 9, 768,
                        "29f5b3c25dd013c4b1630746b7f6ee88c73c0bacf22a970a2331818c225a0620",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 9, 960,
                        "46bb54a946802ebce2f00760639b2bf484faed76cbb284eb2efaa5796d771e6",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 8, 960,
                        "641ffadeaa22c8d97a72036cbd5d934ca454032a81957052e85f3f95b79d3169",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 7, 960,
                        "dd3ca06a86ce32da480403c9b5e0f77b8cfa26dac68c30224ddc2d056fffea7",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255,255,0);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(255,255,0);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(255,255,0);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(1, Block.WHEAT, Block.DIRT)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.WHEAT);
    }
}
