package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.gui.inventory.inventories.GUIReforge;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.blacksmith.MissionMineCoal;
import net.swofty.types.generic.mission.missions.blacksmith.MissionTalkToBlacksmith;
import net.swofty.types.generic.mission.missions.blacksmith.MissionTalkToBlacksmithAgain;

import java.util.List;

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
        MissionData data = e.player().getMissionData();

        if (data.isCurrentlyActive(MissionTalkToBlacksmith.class)) {
            setDialogue(e.player(), "initial-hello").thenRun(() -> {
                data.endMission(MissionTalkToBlacksmith.class);
            });
            return;
        }
        if (!data.hasCompleted(MissionMineCoal.class)) {
            e.player().sendMessage("§e[NPC] Blacksmith§f: Retrieve 10 coal from the Coal Mines!");
            return;
        }
        if (!data.hasCompleted(MissionTalkToBlacksmithAgain.class)) {
            setDialogue(e.player(), "spoke-again").thenRun(() -> {
                data.endMission(MissionTalkToBlacksmithAgain.class);
            });
            return;
        }

        new GUIReforge().open(e.player());
    }

    @Override
    public NPCVillagerDialogue.DialogueSet[] getDialogueSets() {
        return List.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "§e[NPC] Blacksmith§f: I'm the town Blacksmith! I can §areforge §fitems for you, for a price.",
                                "§e[NPC] Blacksmith§f: Reforging usually costs Coins, but since I'm feeling friendly I can reforge your first item for Coal §8x10.",
                                "§e[NPC] Blacksmith§f: Go into the Mine to collect Coal, then come back to learn how to reforge items!"
                        }).build(),
                DialogueSet.builder()
                        .key("spoke-again").lines(new String[]{
                                "§e[NPC] Blacksmith§f: Ahh, excellent!",
                                "§e[NPC] Blacksmith§f: Reforging items allows you to get the most out of your weapons, armor, and other items by applying stat modifiers to them!",
                                "§e[NPC] Blacksmith§f: To reforge an item, place an item in my inventory. Reforging costs Coins - the more prestigious items cost more to reforge!",
                                "§e[NPC] Blacksmith§f: However, this time I will reforge any item for the low price of Coal §8x10!"
                        }).build()
        ).stream().toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
