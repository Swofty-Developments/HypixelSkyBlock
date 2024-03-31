package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCSecuritySloth extends SkyBlockNPC {

    public NPCSecuritySloth() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Security Sloth", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "";
            }

            @Override
            public String texture() {
                return "";
            }

            @Override
            public Pos position() {
                return new Pos(2.5, 70, -75.5, 0, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it added quickly!");
    }

}
