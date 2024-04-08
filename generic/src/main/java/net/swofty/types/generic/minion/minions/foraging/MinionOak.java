package net.swofty.types.generic.minion.minions.foraging;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionOak extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of( //skins done
                new MinionTier(1, 48, 64,
                        "57e4a30f361204ea9cded3fbff850160731a0081cc452cfe26aed48e97f6364b",
                        Material.WOODEN_AXE, true),
                new MinionTier(2, 48, 192,
                        "bb4eccf762baf18f2d5b5b0c8fa9ca2ce1150f8beb1ce66756a4884c68253d9a",
                        Material.WOODEN_AXE, true),
                new MinionTier(3, 45, 192,
                        "a306123edb86a30535267a12ba6ab13558d93abad973793dba6c82c929dfb430",
                        Material.STONE_AXE, true),
                new MinionTier(4, 45, 384,
                        "c643dd831a5d5e409b22f721bd4a6d1e1109b1b24e1fbafeeb0d2aba8c626ce9",
                        Material.STONE_AXE, true),
                new MinionTier(5, 42, 384,
                        "553cbf53549d02cd342aafa13534617514a363ae74db94834fced3a8dd3801b8",
                        Material.STONE_AXE, true),
                new MinionTier(6, 42, 576,
                        "3497c3ff3cf509495bbf59884f8ecae2148ee391a589d4e20bbcb7872d55373f",
                        Material.IRON_AXE, true),
                new MinionTier(7, 38, 576,
                        "c22238ee3f8a38acb4bd05a68479b9b478967eecd51547631c553733c20f6bd9",
                        Material.IRON_AXE, true),
                new MinionTier(8, 38, 768,
                        "fcf9f335bc5c68cf1bb1590d421e8564b942ed94d3c2b4025c1b30168981214e",
                        Material.IRON_AXE, true),
                new MinionTier(9, 33, 768,
                        "93b2cb6e9ec862139600e83505e6b56e07838abb1b6faf4649db9a7098096d20",
                        Material.GOLDEN_AXE, true),
                new MinionTier(10, 33, 960,
                        "546f4040054a097956bf7e135656ea8f52c53acaebddbddbff8d123231c82e93",
                        Material.GOLDEN_AXE, true),
                new MinionTier(11, 27, 960,
                        "e613f991f92bd0cf700cfee9a1440ff4dfe89999792e1eb9698b406549761180",
                        Material.DIAMOND_AXE, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255,155,0);
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
                new MinionExpectations(-1, Block.OAK_LOG, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.OAK_LOG);
    }
}
