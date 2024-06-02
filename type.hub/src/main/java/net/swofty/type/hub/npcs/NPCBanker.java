package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.gui.inventory.inventories.banker.GUIBanker;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCBanker extends NPCDialogue {
    public NPCBanker() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Banker", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "iLrTZiVANLmw8dSM3A59OLKAWYNkvg+MocfzluRU2xYYKW1682hCHBian6MMf+Ke+oHlihtkc5OSA//oCA8zuyP/FpFni+jzOTBUJ8CA/Kh9zeGBKRpKw2P0ChAwIfsBnqG6PEyo5HGCt4ImqBGgWQ8qLizZqaA60qpKWLt78FPX0ruJ+wg35B+BN4vN1MoXv/WFX2BSBdD+GBk9hOysAYoPCIop2F3d2qBPebI8OCawGXZcUegR/N4lCE5Q9bbowdCqUuzr301fPwKK3eSTD4gMuha8fcfD8o4DI88xV9lhU5WpairEBzG/yF/6wFu40A8+HoOlFLlmDAggGibtShHrkowa2JuclYYyMFsbcQVW3LG1XjdneUBR9Owd6B0JmNYKI2Uuh4vaQi05Oy4QbdqpjoJAr/sP73pj6nc2HdwC3phHuoIh/Cry7Q5sxNyDBsCEzLbwEDxROPcvL3FUbvgaCB+LmZ6dwtga1hTT8gEbXkjrrBvhUk6qvCgERvG93AeuPWZwANMn1lR5klN5aOxlsjGSmeiSwI1BgdlVybV1AAiQdN9JcNNETtFvef31J+wiU1QsWTewmMs/Ltrr5EI4FKWT+tFxDbvl1jcAeCc2NYmINVLDOR4viNfEdg4MWBV+pzGKcgydniUv0Nh4xehWd9ZLml/nZrcmDKy2Wpk=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzODAwMDI0NzY2NSwKICAicHJvZmlsZUlkIiA6ICI2Njg5MDJmYjI1YTY0NDBhODBmM2Y2MjZhYTk0MzBmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJCYW5rZXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmZkODNhY2NhOWFmM2JiYWQ3MDVmNzE0MzU1ZDk0MTA3NDEyY2E0ZWJiZDRjZTkzOTE2MGMxYmUxMGNjZDFhMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U3ZGZlYTE2ZGM4M2M5N2RmMDFhMTJmYWJiZDEyMTYzNTljMGNkMGVhNDJmOTk5OWI2ZTk3YzU4NDk2M2U5ODAiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-24.5, 71, -58.5, 180, 0);
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

        MissionData missionData = e.player().getMissionData();

        if (missionData.isCurrentlyActive("talk_to_banker")) {
            setDialogue(e.player(), "quest-hello").thenRun(() -> {
                missionData.endMission("talk_to_banker");
            });
        } else {
            new GUIBanker().open(e.player());
        }
    }

    @Override
    public DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "Hello there!",
                                "You may want to store your §6Coins §fin a safe place while you are off adventuring.",
                                "Storing them in your §6Bank §fkeeps them safe and allows you to earn interest at the start of every season!"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
