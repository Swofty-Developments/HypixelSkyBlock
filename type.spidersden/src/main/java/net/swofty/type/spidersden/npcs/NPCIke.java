package net.swofty.type.spidersden.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ChatColor;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCIke extends HypixelNPC {
    public NPCIke() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{
                        ChatColor.GOLD + "Ike",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "CLICK"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "d4g+ZRuYzKehekkNskqtDdQZnqF3vPFhUD37hUSEkwgWytaY2YCRSKM/1ZiBFh5Ufv7ILgQdBRLaumFju88v7RnkWMzEjfWJQnipTNQlYKFpUb8M7ucYMy6Vl+H5kFd0PBqkWdn8btz7BOqbPXPjwHYJjZgQ9+w28M1p+GQXd3TWm1NCqtUS+ufmNmu4yB9guf59ZQYx806UVT0z8KcelMFGj+RXljGfEh8Wo4mlVb237aofoc+htkh8R66G/GJiLJCNIO/YchB2iqDE7VcLR/UPkrJmS/m7alntdpJA03GdBfkyIi+deYQXmqQ+Kl7Dic2QhmfxJcte94vPbVwpMuVdTlEnTpm/CzAqStp9AGJfMm934zL8g2FvVvaFtOUUlSpnULpETuaozcCBxlrcX0uuEOKDVEStTAk4hVjeg+5aGwg2yT1AGkistUnfEly3WeVp2cHmMOBkKi8y8QlV4QprnTOpl9uehiMWP5iSdf+MFVidj2FRamKaZ8h7uuu+hOCk12Ga17VyKw2AT+CaRKlxDBpZnGV1rP/rIMDA/lLgD38W6HQnxkyiPzLMmKb2mslye70I7xQS9s5JYWsARPbp+aw5E6kKvUIonW6Z+gWabkYIs6zCod9aLBZcs7/Pwx3xRtJGj0dsbDlGlxXBV2iV2ikAaRFEdO6+dqA7RVo=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTc0NTg0NTcyNjY3NywKICAicHJvZmlsZUlkIiA6ICI2YWVmMjM3Y2RhNDY0Y2QzYTdiZDcxYjg3YzFlMDEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLYWlqdW5va2kiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTAxMzAyNzFjODE2ZTM2YWIxN2Y4ZWY1ZWYwNjMwNGM4Y2JkYTk1MjMwNzg4Mjg1NDEyYmY1MzQ2NmIwNzAzNCIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-260.5, 94.5, -284.5, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {

    }
}
