package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerJack extends NPCVillagerDialogue {
    public VillagerJack() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fJack", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-0.5, 70, -54.5, 0f, 0f);
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
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "§e[NPC] Jack§f: Increasing your Foraging Skill Level will permanently boost your §cStrength",
                                "§e[NPC] Jack§f: Increasing your Enchanting and Alchemy Skill Levels will permanently boost your §aIntelligence."
                        }).build(),
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "§e[NPC] Jack§f: Your §aSkyBlock Profile §fin your §aSkyBlock Menu §fshows details about your current stats!",
                                "§e[NPC] Jack§f: There are 7 stats in total, including §cHealth§f,§c Strength§f, and §aDefense§f.",
                                "§e[NPC] Jack§f: Equipped armor, weapons, and accessories in your inventory all improve your stats."
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
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
}
