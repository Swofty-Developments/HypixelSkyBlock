package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCBillyJoe extends NPCDialogue {

    public NPCBillyJoe() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Billy Joe", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "OfOl4uROq84Rq7ig5Uy+P5N0J66V0Sc6BW+5iwOs659y4LtK7MlFbX917io1Cp8OUIuizXYeEXz1xPsJJUfpokDVISOcuv2a/CsK4hSWIHs4KiFd2GS9EEPYwoibWSBZuKy7c9QXOW1+6MRSXQYGstI4wta+ra4pTnyvkNuhlSKgvhGTejJWh/Rl6xS5dKS/alGg3okdNY+fXJBWUdZ5jBcZaZ4pYdneVRZKk3zdArOfm8xlpvMJecuS07kS8bVRcl+WDydwDEiG2uZu76lBV9uWrKz9N+Z/r9qBUeuhdGJ12hUpR6xeNUKwGN9bdVFjjddbozzkC++J+WwX4MRlUjYSGasThWzUkJrjVr9PKKTkwoaShyIRyuOYXotaAYmnkxbcYiAnKoUk2dgDcIJ0d+A2r1StFXAa72Gx5RZY5ZYOIBCVnyZX3oQygqBxov9l3Xtk/Tw3tRpU901QUWILgxFXrDlgbGHl8SgLsLUXUHvTysno9unQBA8175dFqKTOeWi/gi4mxxfS7A2Ve/DT9E5drCyKVTpBo+PT1rOVHh50qgKNkvHTmJgE6vCOnf8ubmdmzl6Z7pK8uLSgfGMxYSp7XTuEQmdDEFR1mhliKbBmLVFj03jJ8/2bxUNZAWGTjBUO+MN5YcVHJo6CQuk2yPIiEaY1JwOgWotoynV7l5E=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMjExNzExMTcxOCwKICAicHJvZmlsZUlkIiA6ICI3ZGEyYWIzYTkzY2E0OGVlODMwNDhhZmMzYjgwZTY4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHb2xkYXBmZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBmYzRhMDNkYmYwMGZlYWI5YjQyMzU1OTNjMjkwNDkxOTZjN2U3ZjA3NTRkOGU3ZjBiNjYzMmQ3ZTI5NWNmNyIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-78, 70, -70, -115, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        if (isInDialogue(e.player())) return;
        setDialogue(e.player(), "hello");
    }
    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "You can hold as many §aAccessories §fas you want in your inventory. They will always work.",
                                "If you have more then one of the same type of the Accessory in your inventory, §conly one §fwill work."
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
