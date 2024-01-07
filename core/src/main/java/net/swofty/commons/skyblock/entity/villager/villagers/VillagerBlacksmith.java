package net.swofty.commons.skyblock.entity.villager.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.commons.skyblock.gui.inventory.inventories.GUIReforge;
import net.swofty.commons.skyblock.entity.villager.NPCVillagerDialogue;
import net.swofty.commons.skyblock.entity.villager.NPCVillagerParameters;

import java.util.Collections;

public class VillagerBlacksmith extends NPCVillagerDialogue {
    public VillagerBlacksmith() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"Blacksmith", "§e§lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-27.5, 69, -125.5, -35f, 0f);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerMeta.Profession profession() {
                return VillagerMeta.Profession.WEAPONSMITH;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        if (isInDialogue(e.player())) return;

        if (e.player().isSneaking()) {
            setDialogue(e.player(), "initial-hello").thenRun(() -> {
                e.player().sendMessage("&cGlad u finished that");
            });
        } else {
            new GUIReforge().open(e.player());
        }
    }

    @Override
    public NPCVillagerDialogue.DialogueSet[] getDialogueSets() {
        return Collections.singletonList(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "Hello there, I'm the blacksmith.",
                                "I can repair your tools for a price.",
                                "gergegeg"
                        }).build()
        ).stream().toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
