package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCBartender extends SkyBlockNPC {

    public NPCBartender() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Bartender", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "XV4wRQHNf+t8UNPxyCQWe9OTKABW2H2q8dKQJD6opc/UpjN8Ho5BZkqeCbCJ0Zdkq6YVzyQTctxOAVx99gi7FCUmtT02Z5lujim8zSemuzAN5ndYvHOBAjOJL51sbftnuGoCPBklmEAJ4uWWl+77mHe2GfXZkHTrBw0yvw777u2vtA8QJwoq2eh/8OPUFWSRtJVeW9kIggwfjJbVYjP7w1im5DKklvL7Tw71TuRx+1VebWhpD3lOTtfq1Vo6ri+LOs4o36Ix/Ec2xnmjeV2BF0CK6gkIbzaMcF4efFHxonmW2GRXL+E/tIpvAm4sY5JR5z/jV4Mp6qEN0CaU5WR8DSdkwLMTrRGuzRoZUjvZL2B6kZ7yaVmpOo7PeNVAr8hPRjAB489qJVLDawfpVCNt4jgQMMDBJUPk8F4DJPBaUMFZXNM4BO9B6DH6xVHNsaZhOZZu5tKKyXBg0yuT7FA1OgaFcye8z+JSIDHNd3kxcR02idHDmI1pDL6da2pdPoAmz19I5Ao7rI9kXPMdJUPmY7aIEd4j6RXXXnJ3UqUcKnDQqf3ElwSbZXayTo/Wn6P9KFa2aTjR/gfvIjf7+Jn9vyVGtbFG8x+xg1oSZR4RE2rmOhHQKEETaXakqbRMWUt1EHWm4c/HpxpxuRSNvFAwkvdrV4mt8VrCo0x+A/Z/3cQ=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYwODIxODk0MDY5NiwKICAicHJvZmlsZUlkIiA6ICI3MzgyZGRmYmU0ODU0NTVjODI1ZjkwMGY4OGZkMzJmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJJb3lhbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYzkyNTgwODcwNzRlYjEzMTRkN2EwZGQzM2QxZmNiNWNlYmYzZjZmMWE0ZThkMjM1NzIxYmMyNTliNGU0OTZhIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position() {
                return new Pos(-83.5, 71, -47.5, 0, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it done quickly!");
    }

}
