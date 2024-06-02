package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerTom extends NPCVillagerDialogue {
    public VillagerTom() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fTom", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(28, 69, -57);
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
                        new GUIRecipe(ItemType.PROMISING_AXE, null).open(e.player());
                    });
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
                                "§e[NPC] Tom§f: I will teach you the Promising Axe Recipe to get you started!",
                        }).build(),
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§e[NPC] Tom§f: All SkyBlock recipes can be found by opening the §aRecipe Book §fin your §aSkyBlock Menu",
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
