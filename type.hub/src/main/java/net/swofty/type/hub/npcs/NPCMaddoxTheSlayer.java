package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCMaddoxTheSlayer extends HypixelNPC {

    public NPCMaddoxTheSlayer() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Maddox The Slayer", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "DnggeaoC7Ik+Kdw10vBW5QdzUNCMuPN12+mfK9p5/gT1tMBS7KOZcnWXx/mQTAAWlOmkLomCTa1z79YynQZDbikrZ3L/2/V8vM3uySA/DWtBpDp0nZZYWccIuZ01rKoduSj1iUJMmYlzOfBEYm/Fi0mrHLezShmv+zR2cUDT3zuHfOr2Y19rS29axh242C28ADd96sNTSSKIBUr5QO25LM/EQOW5dKge3xbVHk/RmSdHE0aaHVNnB5wecM7kEvBLJQNP3k6ydtlFTgcJiRB5qzPeqeXbnVyMVd2sSGbvLPR2gWEeu1CA5i5Duf/syeutmH490+lT9NEZL76qGic3/I9e7KQoT+uAe7wqI74AbCmQPxRddPJHVjE6WBcKpvrARgOiBv9TLmWe+FCvp+d7v+3ZvLG6BmPOXB8c1a4Ovmx5UN/e1KJiOOismPyWGEh2Zyv/jveZNYw859AlD+D/p4ZatwF+ZNicK5xuhE1X/g0UJWmPiax9Quvpa5eLUvo+V/80L8NtGd/vUJggGHJ/3gnVE64haZqcO2iK6lZubOFS3gPp2OdcU/YNRvSbXwxHIDkq4L0nOHHObkT7WUV+PsNSTuVlnrekGu7/XTPb4TDXOhJUwjEbdiOHhTbzDfJC/UuzPqtpmFyYHRV2ucLg3M9bFKoHj2A6sRFXgmp5oHM=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY1MTE5MTAzMDIyNywKICAicHJvZmlsZUlkIiA6ICI4N2RiMmNjNWY4Y2I0MjI4YTU0OGRiMzJlM2Y0NmFmNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJZVG1hdGlhczEzbG9sIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzczOGM2ODdkZDg4ODdjNTA5ZDU1ODY1ZWJiNjJhYjYwODdlZjc0M2U5NDI1Y2Q1ZGM2YjRjN2I5M2I3NDdmZjIiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-74, 65, -50, 180, 0);
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
