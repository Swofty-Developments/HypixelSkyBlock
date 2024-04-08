package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionQuartz extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 22, 64,
                        "6e72be1f791d92395c4cf01335d078de5af43d9c6fcbc3a38753dc56942f4a46",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 22, 192,
                        "1e16e0b2616ee6da20ea53492a96257b0aba565df23bfdbbd9d9010b2a6eece9",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 21, 192,
                        "ed8cb0280411a12a66e9ab5bec0b6815aee4170b3a99c7812688f7a38df90f3e",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 21, 384,
                        "a41a43ac301b79925e62de426be22ce0c5bfdb59dec1107f0e22f725dfc03834",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 19, 384,
                        "460e38e34d6c0c7d75528d43190713327e00eb8126d6d0218c9c9ddc5463473e",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 19, 576,
                        "8a1cb5539b499518607a542fc6a948f2b191c743fb74d988a43be508483ea627",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 17, 576,
                        "a86cf99bf17ab09181314ca713ef0deca4ae34cea7b4cd6045c308cefc9fb658",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 17, 768,
                        "fad40884125b86e089ba4910e21e87862f34bc6702a06c2cb099beed4cba3eb7",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 14, 768,
                        "ba1ee3a968b677ed831de2e1d7b66a0ff46482a16fa093b565d3d26ec3111026",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 14, 960,
                        "fe5c1ff6b4d2237f65017ea56be758856b0ca662bcf331196e4f047c10e9b18",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 11, 960,
                        "ba18006267c91854761507ea63ef0750a7b306336f3e362da7aad7c72cc68603",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 10, 960,
                        "cb2903b51e16da7d778b7d0352c3ff08a21245bc476b73cdb82417ba4ff8a139",
                        Material.DIAMOND_PICKAXE, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255, 255, 255); // Quartz minion's boot color (White)
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(255, 255, 255); // Quartz minion's leggings color (White)
    }

    @Override
    public Color getChestplateColour() {
        return new Color(255, 255, 255); // Quartz minion's chestplate color (White)
    }
//hm


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.NETHER_QUARTZ_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.NETHER_QUARTZ_ORE);
    }
}
