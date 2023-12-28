package net.swofty.entity.villager.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.entity.villager.NPCVillagerDialogue;
import net.swofty.entity.villager.NPCVillagerParameters;
import net.swofty.entity.villager.SkyBlockVillagerNPC;

import java.util.Arrays;
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
                return new Pos(-6.5, 70, -89.5, -40f, 0f);
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

        setDialogue(e.player(), "initial-hello");
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "§e[NPC] Duke§f: I found a few Fairly Souls during my travels, they are usually pretty hard to find!",
                                "§e[NPC] Duke§f: I would not venture South of the §bVillage§f, it seems like this place was abandoned."
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
