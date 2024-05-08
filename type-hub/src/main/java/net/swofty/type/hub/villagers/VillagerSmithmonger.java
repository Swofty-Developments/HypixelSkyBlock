package net.swofty.type.hub.villagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
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
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }
}
