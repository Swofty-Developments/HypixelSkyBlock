package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionLapis extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 29, 64,
                        "3654a0f8a85d131ac9e633776837d931f0a952dcf174c28972788fef343917ed",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 29, 192,
                        "9f3a29fe392f3b15b641f18668f888cbaca5a9065d22f76e149fc60c0459f1f2",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 27, 192,
                        "8bb69d622dd5ff8ad423a379dd309046551388ae9e157fd643cefd79cbf3efe6",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 27, 384,
                        "37167d3d36785567737aa225b324504c30bf4b451f92bd8c9c081dd7f53458d0",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 25, 384,
                        "f777471870afa67f5d17c6534739f3eef8ff4d95d660ae80f41e28b1165cc0c2",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 25, 576,
                        "1d8a853354472f41dde5a584c160799484788a042487a2053aac55ad6a9780a4",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 23, 576,
                        "6fa5bbebcbd18d58091dece4576153c6dff34cb16323c9b634db031eee14116b",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 23, 768,
                        "1f43f65fd40d5300603dfd790922b4020fed0f87261a1fbaf7c88fcedd38813f=",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 21, 768,
                        "36a33c76348ea038943642db763ca2bab278a005bb6735a23dc1c801dded6cde",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 21, 960,
                        "56a745e9e375e2f7661f2db6339e9c94b009b4f15a56174ac3b2fd7ef66b4b2a",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 18, 960,
                        "7e05846c2b0668022342acdc82e62ffecad75b8be7fe52f533948457fe16791a",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 16, 960,
                        "edc594af5a8391c59001582b274864cf0701505609ece652c2a356de4617c28e",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(38, 97, 156); // Lapis minion's boot color
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(38, 97, 156); // Lapis minion's leggings color
    }

    @Override
    public Color getChestplateColour() {
        return new Color(38, 97, 156); // Lapis minion's chestplate color
    }


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.LAPIS_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.LAPIS_ORE);
    }
}
