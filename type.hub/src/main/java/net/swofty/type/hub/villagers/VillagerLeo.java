package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.type.generic.entity.villager.NPCVillagerParameters;
import net.swofty.type.generic.gui.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.type.generic.mission.MissionData;

import java.util.stream.Stream;

public class VillagerLeo extends NPCVillagerDialogue {
    public VillagerLeo() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fLeo", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-7.5, 70, -75.5, -30f, 0f);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.BUTCHER;
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
                        new GUIRecipe(ItemType.LEAFLET_CHESTPLATE, null).open(e.player());
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
                                "&e[NPC] Leo&f: You can unlock §aLeaflet Armor §fby progressing through your §aOak Log Collection§f.",
                                "&e[NPC] Leo&f: There is a §bForest §fwest of the §bVillage §fwhere you can gather Oak Logs.",
                                "&e[NPC] Leo&f: To check your Collection progress and rewards, open the §aCollection Menu §fin your §aSkyBlock Menu§f."
                        }).build(),
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§e[NPC] Leo§f: Progressing through your Collections unlocks new crafting recipes, brewing recipes, trades, enchantments and more!",
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}