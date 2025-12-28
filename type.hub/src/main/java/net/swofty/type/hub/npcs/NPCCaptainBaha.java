package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.stream.Stream;

public class NPCCaptainBaha extends HypixelNPC {

    public NPCCaptainBaha() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§6Captain Baha", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "A9Wh529RWV2HEMvVnPzQEPfvT7p8m2GU8IB5FowBVYRash8GUSC6OvO88v5eBXAsCJvAauOnCFkp0DrxNTHUTS6E8rcGpo5ieHTr+QYglXIlA8S+rgA5eGODgI3LEtOZucHJ6H64a23Bu41lNMpN2c+LzQbisqC9WBnfVBxYo6qrzgh5JBGsRDIg2h3UKmTnNgJPuhN2cwRDDlHG8/k+xES5ZqyEFvdjGn6O5HHL6xyMkCukjZN0E8s03NkpkKxZXEm1M/Eg8EWtwGqZIa3DHNmxchYok4mDPMst8iRy4pGRlJN+VBCmGLIV7pq4QZlGzuXWplrX/PAOb+B36Rg67SHvmIk23tpnu+7uvB3rw9NedWY1+xLp8W4gPKpynOobSCbKiJ6bX0mCQfURVh2svFT5nG/VnKCL0TE8CUiTxOuxJR8QWwWRI4BMRMJQfQxy0mofvPnR5g1XUnHzvGWr4m44dmooqyCgB4W9iysADAEgc9CVtizjroopAJLXCtfsxwIuioHaZsBKQU1NpvpH55bPqf//RI9FyJJwOXTgX7fbF49z0eAgjnRAyF9VE9VYI2hFZwa3BIFnvdGxlZhE63QPB+nmKQMT0WzTz15lm77lxvvpQsurkm2gKr6FlL9+SokbTUuQmisyzS84s2EocpRscgc9JF1Dv/NjK7T+3GU=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTc0MDQxOTU1NzI0NiwKICAicHJvZmlsZUlkIiA6ICIzY2I3YTA3YWY3ZjM0ZWZiYTlkNGI4ODQ3NDM4Mzc0ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBUkJVWklLMTIwMTMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzcxYzQ0YWQ0MDdjMDIxNzM3YWQ3ODkwN2I1MDY4ZDdiY2MwYzY1OGIyYTJmYmFiZjAxNzA2NTYzYmQ5NDQ3ZCIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(179.500, 69.000, 55.500, 105, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        HypixelPlayer player = event.player();
        if (isInDialogue(player)) return;

        if(!player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_GAVIN)) {
            setDialogue(player, "first-interaction").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_GAVIN, true);
            });
            return;
        }

        setDialogue(player, "idle-" + (int)(Math.random() * 2 + 1));
        // TODO: new GUIFishingShip().open(player);
    }

    @Override
    protected DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "I was about to set sail, but this §6Ship §fis missing its §cengine§f!",
                                "Maybe §3Fisherman §fGerald knows where it is?",
                        }).build(),
                DialogueSet.builder()
                        .key("rust-ship-engine").lines(new String[]{
                                "Ahoy, is that the §cRusty Ship Engine§f?"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-yes").lines(new String[]{
                                "Excellent! I'm the captain of this §6Ship§f, which means I oversee everything from repairs to navigation.",
                                "Apply that §cRusty Ship Engine §fin the §6Engine §fslot by clicking the engine in your inventory!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
