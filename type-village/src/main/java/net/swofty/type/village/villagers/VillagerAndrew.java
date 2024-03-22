package net.swofty.type.village.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;

public class VillagerAndrew extends SkyBlockVillagerNPC {
    public VillagerAndrew() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fAndrew", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(38.5, 68, -46.5, 135f, 0f);
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
        e.player().sendMessage("§e[NPC] Andrew§f: This game is still under heavy development, don't forget to check the §adiscord (discord.gg/atlasmc) §foften for updates!");
    }
}
