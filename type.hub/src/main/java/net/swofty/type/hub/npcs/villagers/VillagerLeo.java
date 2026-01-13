package net.swofty.type.hub.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class VillagerLeo extends HypixelNPC {
    public VillagerLeo() {
        super(new VillagerConfiguration(){
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"&fLeo", "§e§lCLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-7.5, 70, -75.5, -30f, 0f);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.BUTCHER;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;

        MissionData data = player.getMissionData();
        if (data.isCurrentlyActive("speak_to_villagers")) {
            if (data.getMission("speak_to_villagers").getKey().getCustomData()
                    .values()
                    .stream()
                    .anyMatch(value -> value.toString().contains(getClass().getSimpleName()))) {
                if (System.currentTimeMillis() -
                        (long) data.getMission("speak_to_villagers").getKey().getCustomData().get("last_updated") < 30) {
                    setDialogue(player, "quest-hello").thenRun(() -> {
                        player.openView(new GUIRecipe(ItemType.LEAFLET_CHESTPLATE));
                    });
                    return;
                }
            }
        }
        setDialogue(player, "hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "You can unlock §aLeaflet Armor §fby progressing through your §aOak Log Collection§f.",
                                "There is a §bForest §fwest of the §bVillage §fwhere you can gather Oak Logs.",
                                "To check your Collection progress and rewards, open the §aCollection Menu §fin your §aSkyBlock Menu§f."
                        }).build(),
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Progressing through your Collections unlocks new crafting recipes, brewing recipes, trades, enchantments and more!",
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}