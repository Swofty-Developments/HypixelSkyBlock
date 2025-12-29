package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCUdium extends HypixelNPC {

    public NPCUdium() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Udium", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "GnM27W60Lt+aV2EqLga8sxV3Fw1Z9H2CaCbu/quQ3FItmdpjiiGuX2qQR/x1Wp4KrGLUdgoWlPX926lT++xt3fuIO9nLiCy/m/06KQ7ijvjtvYA3L4ZNpknN6bRKjmxICvczlpiZXfU+esO2wbnNA3Dq86MV0SIWhUPLUkqFa5e6JX5/4GOkVt/hiA32qx4eFLBHkaX3PUpzKn2YhoCAoHlB6p0bVccq5xi79utz72UfufFvEYVUs6jzKCE0i+hzMTFaAINQeKZ4zvybhEqQJuMt6mWnyRkJgav5DScueRLaR97oc1CcDE9ti4C9qsHDLi0N1n1OXJ2no4R6qwcMwGpoZXTQv/KpQWPcc7N4xY04Wp1J8ydL5yfZ+AlM2upt5pzzIni0yc/pbqRDLnyEyJpmLsMpKKtvF5THoyK7Gcyh87J4Q5g90ztkiP8qMV/h4iRskLNbPWNnLlPwlIZx8uaZZvtqf2JNvO9Tlh6AWRnaZyZ5OBKwokns/nospcQ2e4k3wnwiRx0NuR0vBMxw5QuNaH98A/kr9fbUTvhASV738rQgvGR9mlEE7WCO9G2cG2UvJuiPEWFJIHq+bcnJ8CA0A4aOuMfYibS8/K3Fou2Z1Ho2+50bKhCujUxIterBZMk4/oWPVx67MUT8faaJ7HcaJ7jUMja3DKPSKkotte0=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY1OTM5NzU1NDUxMywKICAicHJvZmlsZUlkIiA6ICI4YmM3MjdlYThjZjA0YWUzYTI4MDVhY2YzNjRjMmQyNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJub3RpbnZlbnRpdmUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVhZWU3ZTUxYTg3NjkzYThmYjdmZTQzMGE4ODdmZmIyN2VlYjk3NDljZWE4MTZkNjUyNDVlMmRmNjU1Y2YzMiIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(44.8, 10.4, 76, 145, 0);
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
