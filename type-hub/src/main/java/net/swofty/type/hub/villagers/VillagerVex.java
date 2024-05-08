package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerVex extends NPCVillagerDialogue {
    public VillagerVex() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fVex", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-16.5, 70, -81.5, -60f, 0f);
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
        setDialogue(e.player(), "hello");
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "&e[NPC] Vex&f: You can shift click any player to trade with them!",
                                "&e[NPC] Vex&f: Once both players are ready to trade, click on §aAccept trade§f!",
                                "&e[NPC] Vex&f: Make sure you don't give away all your belongings!"
                        }).build(),
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§e[NPC] Vex§f: You can disable Player Trading in your §bSkyBlock Settings§f!",
                                "§e[NPC] Vex§f: Your settings can be found in the §aSkyBlock Menu§f.",
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
