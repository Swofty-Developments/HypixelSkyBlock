package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerDuke extends NPCVillagerDialogue {
    public VillagerDuke() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fDuke", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-11.5, 70, -95.5, -40f, 0f);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerMeta.Profession profession() {
                return VillagerMeta.Profession.BUTCHER;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        if (isInDialogue(e.player())) return;

        MissionData data = e.player().getMissionData();
        if (data.isCurrentlyActive("speak_to_villagers")) {
            if (data.getMission("speak_to_villagers").getKey().getCustomData()
                    .values()
                    .stream()
                    .anyMatch(value -> value.toString().contains(getID()))) {
                if (System.currentTimeMillis() -
                        (long) data.getMission("speak_to_villagers").getKey().getCustomData().get("last_updated") < 30) {
                    setDialogue(e.player(), "quest-hello");
                    return;
                }
            }
        }
        setDialogue(e.player(), "initial-hello");
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "§e[NPC] Duke§f: I found a few Fairly Souls during my travels, they are usually pretty hard to find!",
                                "§e[NPC] Duke§f: I would not venture South of the §bVillage§f, it seems like this place was abandoned."
                        }).build(),
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "§e[NPC] Duke§f: Are you new here? As you can see there is alot to explore!",
                                "§e[NPC] Duke§f: My advice is to start by visiting the §bFarm §for the §bCoal Mine§f, both North of here.",
                                "§e[NPC] Duke§f: If you do need some wood, the best place to get some is West of the §bVillage§f!"
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
