package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerLiam extends NPCVillagerDialogue {

    public VillagerLiam() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fLiam", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(10.5 ,70 ,-41.5);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerMeta.Profession profession() {
                return VillagerMeta.Profession.NONE;
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
                }
            }
        }
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "§e[NPC] Liam§f: Did you know you have a SkyBlock Level?",
                                "§e[NPC] Liam§f: In fact, everyone does! You can see them in the tab list by holding [TAB]!",
                                "§e[NPC] Liam§f: You can level up by playing every aspect of the game!",
                                "§e[NPC] Liam§f: If you're curious, you can view your level, and more information in your SkyBlock Menu!"

                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
