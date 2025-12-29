package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCMort extends HypixelNPC {

    public NPCMort() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Mort", "§e§lGATE KEEPER"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-88.5, 55, -128.5, -90, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                        .clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }

}
