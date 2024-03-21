package net.swofty.type.village.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;

public class VillagerStella extends SkyBlockVillagerNPC {
    public VillagerStella() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fStella", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(17.5,70, -99.5);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerMeta.Profession profession() {
                return VillagerMeta.Profession.LIBRARIAN;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        e.player().sendMessage("§e[NPC] Stella§f: At any time you can create a Co-op with your friends!");
        e.player().sendMessage("§e[NPC] Stella§f: Simply go in your §aSkyBlock Menu §fwhere you can find the §aProfile Menu§f.");
        e.player().sendMessage("§e[NPC] Stella§f: This is where you can create, delete or switch SkyBlock Profiles.");
        e.player().sendMessage("§e[NPC] Stella§f: Enter §b/coop §ffollowed by the name of all the friends you want to invite");
        e.player().sendMessage("§e[NPC] Stella§f: All your friends have to be online to accept!");

    }
}
