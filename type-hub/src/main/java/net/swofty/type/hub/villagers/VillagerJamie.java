package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.type.hub.gui.GUIJamie;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerJamie extends NPCVillagerDialogue {
    public VillagerJamie() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fJamie", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-36, 68, -38);
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
                    setDialogue(e.player(), "quest-hello").thenRun(() -> {
                        new GUIJamie().open(e.player());
                    });
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
                                "§e[NPC] Jamie§f: You might have noticed that you have a Mana bar!",
                                "§e[NPC] Jamie§f: Some items have mysterious properties, called Abilities.",
                                "§e[NPC] Jamie§f: Abilities use your Mana as a resource. Here, take this Rogue Sword. I don't need it!"
                        }).build(),
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§e[NPC] Jamie§f: You might have noticed that you have a Mana bar!",
                                "§e[NPC] Jamie§f: Some items have mysterious properties, called Abilities.",
                                "§e[NPC] Jamie§f: Abilities use your Mana as a resource."
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
