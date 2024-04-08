package net.swofty.types.generic.minion.minions.foraging;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionJungle extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of( //skins done
                new MinionTier(1, 48, 64,
                        "2fe73d981690c1be346a16331819c4e8800859fcdc3e5153718c6ad45861924c",
                        Material.WOODEN_AXE, true),
                new MinionTier(2, 48, 192,
                        "61a133a359788b12655cfb9abd3eb71532d231052f5bb213fd05930d2ee4937",
                        Material.WOODEN_AXE, true),
                new MinionTier(3, 45, 192,
                        "9829fa43121066bc01344745f889c67f8e80a75ba30a38e017d2393e17cfef21",
                        Material.STONE_AXE, true),
                new MinionTier(4, 45, 384,
                        "95ca25a3b4fc31454da307a4e98c09455efaaa9f2c074b066a98300764e2690b",
                        Material.STONE_AXE, true),
                new MinionTier(5, 42, 384,
                        "20d26c2e29b2205c620b8b60fbaa056942d5417b75a2acc7f4c581b0e9bc6d",
                        Material.STONE_AXE, true),
                new MinionTier(6, 42, 576,
                        "b8619464d104822d9937344d11ee5c037169a13b2473f59b24836fca4cf214c5",
                        Material.IRON_AXE, true),
                new MinionTier(7, 38, 576,
                        "d7113a0d8e635447ef7b1908cab69d6fd68c010f1fc08b9db4d2612a35e65646",
                        Material.IRON_AXE, true),
                new MinionTier(8, 38, 768,
                        "24606b1daf8e60363fc8db71ef204262ee800fa7b6496fb2e05f57d0674ef51f",
                        Material.IRON_AXE, true),
                new MinionTier(9, 33, 768,
                        "a4bbeb118757923d36871c835779aa71f8790931f64e64f2942ad3306aee59ad",
                        Material.GOLDEN_AXE, true),
                new MinionTier(10, 33, 960,
                        "3ee34e1469da11fe6c44f2ca90dc9b2861a1e7b98594cb344d86824eeeabcb60",
                        Material.GOLDEN_AXE, true),
                new MinionTier(11, 27, 960,
                        "dbefc4e8d5c73d9a9e3fe5b1009f568c5d3cb071fa869b54d2604cadef474505",
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
                new MinionExpectations(-1, Block.JUNGLE_LOG, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.JUNGLE_LOG);
    }
}
