package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerStella extends NPCVillagerDialogue {
    public VillagerStella() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fStella", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(17.5,70, -99.5);
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
                                "§e[NPC] Stella§f: At any time you can create a Co-op with your friends!",
                                "§e[NPC] Stella§f: Simply go in your §aSkyBlock Menu §fwhere you can find the §aProfile Menu§f.",
                                "§e[NPC] Stella§f: This is where you can create, delete or switch SkyBlock Profiles.",
                                "§e[NPC] Stella§f: Enter §b/coop §ffollowed by the name of all the friends you want to invite",
                                "§e[NPC] Stella§f: All your friends have to be online to accept!"
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
