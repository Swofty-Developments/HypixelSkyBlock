package net.swofty.types.generic.minion.minions.foraging;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionAcacia extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of( //skins done
                new MinionTier(1, 48, 64,
                        "42183eaf5b133b838db13d145247e389ab4b4f33c67846363792dc3d82b524c0",
                        Material.WOODEN_AXE, true),
                new MinionTier(2, 48, 192,
                        "9609bcfecad73c84dd957673439a7f56426269fc569b6e8405d1a1c05ced8557",
                        Material.WOODEN_AXE, true),
                new MinionTier(3, 45, 192,
                        "85c6492e5b0e3315fbdfd2314ee98073abdcdcbec36b4915b40e0943a95d726",
                        Material.STONE_AXE, true),
                new MinionTier(4, 45, 384,
                        "4afae3d06cb1510d931c3b213854549614983e6d8e2440ce76b683960aab69f6",
                        Material.STONE_AXE, true),
                new MinionTier(5, 42, 384,
                        "f06b64b7743a20fc36f2aaa0908d64346540af97e38d8263acf5b53e4e4a16fe",
                        Material.STONE_AXE, true),
                new MinionTier(6, 42, 576,
                        "836bc401455a23aed7f83b6ae46f2bcd52809a153bb5888b04a7dca3a702f531",
                        Material.IRON_AXE, true),
                new MinionTier(7, 38, 576,
                        "572b1b70882093a9d19c96e9dd7db8bd51aa117f5b5bbbc27e3bafb9e1c1167",
                        Material.IRON_AXE, true),
                new MinionTier(8, 38, 768,
                        "10a919b3efd2521fc823b2da1246568d5e83dc1f6908ac128d19cde5d326d469",
                        Material.IRON_AXE, true),
                new MinionTier(9, 33, 768,
                        "2f0b33a2ab3e165a193d33e148f61384d01ed45d9edabbf1e55a3016ccd991f5",
                        Material.GOLDEN_AXE, true),
                new MinionTier(10, 33, 960,
                        "9b4826120105ca75f208c3b97225245033e156a61fb53ecebc3fa6e1baaba919",
                        Material.GOLDEN_AXE, true),
                new MinionTier(11, 27, 960,
                        "4f6e34656f238ed0d6823fc31cb16455f79aa9756884225d6ce4ef681c8240eb",
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
                new MinionExpectations(-1, Block.ACACIA_LOG, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.ACACIA_LOG);
    }
}
