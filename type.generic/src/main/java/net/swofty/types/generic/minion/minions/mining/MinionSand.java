package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionSand extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "ba220e38ba2555e9cdd622c30840752398729b5ca4e4977e1997e41f299b5081",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(2, 26, 192,
                        "2ad5f7b440990a35bd4a9f87a42b715bbd1466346539467680179378164743ba",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(3, 24, 192,
                        "e71a57c784a876ff646fe51334514929b5367d758b8f302d0b7e3efca20a6429",
                        Material.STONE_SHOVEL, true),
                new MinionTier(4, 24, 384,
                        "6cd895d66949e763f775dea81723576044a86eb9b738a3dd59ee50c29368ad3c",
                        Material.STONE_SHOVEL, true),
                new MinionTier(5, 22, 384,
                        "533fb92bdbba98561c5c441c76a6f84f4c185e8aa8d5077d12d14342c4b40b5",
                        Material.STONE_SHOVEL, true),
                new MinionTier(6, 22, 576,
                        "a404c932d49da2d1bf93bc058cb3786e6392b6580fab90d7594745dab5743f2f",
                        Material.IRON_SHOVEL, true),
                new MinionTier(7, 19, 576,
                        "a404c932d49da2d1bf93bc058cb3786e6392b6580fab90d7594745dab5743f2f",
                        Material.IRON_SHOVEL, true),
                new MinionTier(8, 19, 768,
                        "5825aa6514ee7a4e5b11a19b13c39b0294737cbdecb1bf7da828cbebd486c1a0",
                        Material.IRON_SHOVEL, true),
                new MinionTier(9, 16, 768,
                        "ba77237e0277ae89431193d242f88cd524b0aff4023799309536e7638608eb73",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(10, 16, 960,
                        "57b9ceb5de3971e27a56a106debc51e6c8156f4321a0e0d757d143016678f47c",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(11, 13, 960,
                        "778f558bd4158df1a5ff79757055374d137f9cf4bd4476f3731b8cf4204381b6",
                        Material.DIAMOND_SHOVEL, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(244, 164, 96); // Sand minion's boot color (Sandy Brown)
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(244, 164, 96); // Sand minion's leggings color (Sandy Brown)
    }

    @Override
    public Color getChestplateColour() {
        return new Color(244, 164, 96); // Sand minion's chestplate color (Sandy Brown)
    }

    //hm


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.SAND, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.SAND);
    }
}
