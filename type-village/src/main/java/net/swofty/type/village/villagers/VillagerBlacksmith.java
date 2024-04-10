package net.swofty.type.village.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.gui.inventory.inventories.GUIReforge;

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
                                "§e[NPC] Blacksmith§f: I'm the town Blacksmith! I can §areforge §fitems for you, for a price.",
                                "§e[NPC] Blacksmith§f: Reforging usually costs Coins, but since I'm feeling friendly I can reforge your first item for Coal x10.",
                                "§e[NPC] Blacksmith§f: Go into the Mine to collect Coal, then come back to learn how to reforge items!"
                        }).build()
        ).stream().toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
