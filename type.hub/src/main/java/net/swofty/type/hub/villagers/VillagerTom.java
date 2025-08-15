package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.type.generic.entity.villager.NPCVillagerParameters;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

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
            public VillagerProfession profession() {
                return VillagerProfession.NONE;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;

        MissionData data = player.getMissionData();
        if (data.isCurrentlyActive("speak_to_villagers")) {
            if (data.getMission("speak_to_villagers").getKey().getCustomData()
                    .values()
                    .stream()
                    .anyMatch(value -> value.toString().contains(getID()))) {
                if (System.currentTimeMillis() -
                        (long) data.getMission("speak_to_villagers").getKey().getCustomData().get("last_updated") < 30) {
                    setDialogue(player, "quest-hello").thenRun(() -> {
                        new GUIRecipe(ItemType.PROMISING_AXE, null).open(player);
                    });
                }
            }
        }

        setDialogue(player,"hello");
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
