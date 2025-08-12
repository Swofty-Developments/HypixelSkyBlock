package net.swofty.type.skyblockgeneric.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.minion.SkyBlockMinion;
import net.swofty.type.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionIce extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 14, 64,
                        "e500064321b12972f8e5750793ec1c823da4627535e9d12feaee78394b86dabe",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 14, 192,
                        "de333a96dc994277adedb2c79d37605e45442bc97ff8c9138b62e90231008d08",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 12, 192,
                        "c2846bd72a4b9ac548f6b69f21004f4d9a0f2a1aee66044fb9388ca06ecb0b0d",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 12, 384,
                        "79579614fdaa24d6b2136a164c23e7ef082d3dee751c2e37e096d48bef028272",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 10, 384,
                        "60bcda03d6b3b91170818dd5d91fc718e6084ca06a2fa1e841bd1db2cb0859f4",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 10, 576,
                        "38bdef08b0cd6378e9a7b9c4438f7324c65d2c2afdfb699ef14305b668b44700",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 9, 576,
                        "93a0b0c2794dda82986934e95fb5a08e30a174ef6120b70c58f573683088e27e",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 9, 768,
                        "d381912c9337a459a28f66e2a3edcdacbddc296dd69b3c820942ba1f4969d936",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 8, 768,
                        "21cb422b9e633e0700692ae573c5f63a838ebc771a209a5e0cc3cba4c56f746f",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 8, 960,
                        "6406ca9dcd26cc148e05917ae1524066824a4f59f5865c47214ba8771e9b924b",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 7, 960,
                        "5ef40b76cca1e4bcd2cbda5bc61bc982a519a2df5170662ea889bf0d95aa2c1b",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 6, 960,
                        "c0bc28604e6b9ffe9dda08372b777a9bd5492d6641168fa453463ea2b215c7fb",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(144, 249, 244);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(144, 249, 244);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(144, 249, 244);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.ICE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.ICE);
    }
}
