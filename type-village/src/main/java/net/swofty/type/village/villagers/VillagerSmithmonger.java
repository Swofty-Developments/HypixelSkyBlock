package net.swofty.type.village.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;

public class VillagerSmithmonger extends SkyBlockVillagerNPC {
    public VillagerSmithmonger() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§fSmithmonger", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-32, 69, -135, 180, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerMeta.Profession profession() {
                return VillagerMeta.Profession.WEAPONSMITH;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it added quickly!");
    }
}
