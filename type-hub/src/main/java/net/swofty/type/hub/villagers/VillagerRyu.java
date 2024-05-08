package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerRyu extends NPCVillagerDialogue {

    public VillagerRyu() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fRyu", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(27, 70, -116);
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
        setDialogue(e.player(),"hello");
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "§e[NPC] Ryu§f: There are §a12 Skills §fin SkyBlock!",
                                "§e[NPC] Ryu§f: Some include Farming, Mining, Foraging, Fishing, and Combat. There are plenty more Skills to discover and level up!",
                                "§e[NPC] Ryu§f: You can learn all about them in the §aSkill Menu§f, located in your §aSkyBlock Menu."
                        }).build(),
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§e[NPC] Ryu§f: Most actions in SkyBlock will reward you Skill EXP.",
                                "§e[NPC] Ryu§f: You get rewarded every time you level up a Skill!",
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
