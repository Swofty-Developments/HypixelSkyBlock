package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionEndstone extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "b339b471723eb35b21f2d87699aea156dc54b491b9516b4f2f27f3e6373ca2c0",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 26, 192,
                        "564962ca65c940b824ee7a9236ccc6b10758465a3320de10f703bf453cc03d1b",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 24, 192,
                        "fe56d6792d0d777080c8636ea7d668767ce1562fee8713e81ef7d3ed65cb93f5",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 24, 384,
                        "eb90a11594df73d7916032e62a11c618cfee278b8f027388693d291a5e45955a",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 22, 384,
                        "1028057e8e64d1b5571481d62e7afb1411dc2ef74733f578294f26bd72890f7a",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 22, 576,
                        "75d7bb694abe9a799510f436666d00877698a301f90638c5af7f7cb07718de6e",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 19, 576,
                        "cdbcda31b38c9fd9dc4e42616278068a6de0b9c701d49100af6ea6d0e3525eed",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 19, 768,
                        "c501ac1dbb2f51f935dab99e6366421a98cd9eac7053c039b53507ed775869af",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 16, 768,
                        "7bec17adb9b84df18eb705c31ecc674535b2f4fff6698b2772351530c2684146",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 16, 960,
                        "4f7961a1c44fd88ea005d427fc0f702b0925e9413e5df4ffc19cea1ef104468",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 13, 960,
                        "77f79d20092e2a689fe8cbec48ea8b028967aad930728129ccc6303616fece20",
                        Material.DIAMOND_PICKAXE, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(214, 214, 140); // Endstone minion's boot color
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(214, 214, 140); // Endstone minion's leggings color
    }

    @Override
    public Color getChestplateColour() {
        return new Color(214, 214, 140); // Endstone minion's chestplate color
    }


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.END_STONE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.END_STONE);
    }
}
