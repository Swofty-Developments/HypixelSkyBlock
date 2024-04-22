package net.swofty.type.farmingislands.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.barn.MissionCraftWheatMinion;
import net.swofty.types.generic.mission.missions.barn.MissionTalkToFarmHand;
import net.swofty.types.generic.mission.missions.barn.MissionTalkToFarmhandAgain;

import java.util.List;

public class VillagerFarmHand extends NPCVillagerDialogue {
    public VillagerFarmHand() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"Farmhand", "§e§lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(144.5, 73, -240.5, -35f, 0f);
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
    public NPCVillagerDialogue.DialogueSet[] getDialogueSets() {
        return List.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "§e[NPC] Farmhand§f: Welcome to the Barn!",
                                "§e[NPC] Farmhand§f: Feel free to harvest any crops on the island, I'm taking a break.",
                                "§e[NPC] Farmhand§f: Did you craft a §aWhat Minion §fyet? Here's the recipe!"
                        }).build(),
                DialogueSet.builder()
                        .key("spoke-again").lines(new String[]{
                                "§e[NPC] Farmhand§f: Nice! I have a Tier X Wheat Minion of my own, maybe one day you can be like me!",
                                "§e[NPC] Farmhand§f: For now here's some §eEnchanted Bread §fto get you started.",
                                "§e[NPC] Farmhand§f: Enchanted Bread can be used in Farming Minion as fuel! It makes them temporarily faster."
                        }).build()
        ).stream().toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        if (isInDialogue(e.player())) return;
        MissionData data = e.player().getMissionData();

        if (data.isCurrentlyActive(MissionTalkToFarmHand.class)) {
            setDialogue(e.player(), "initial-hello").thenRun(() -> {
                data.endMission(MissionTalkToFarmHand.class);
            });
            return;
        }
        if (!data.hasCompleted(MissionCraftWheatMinion.class)) {
            data.endMission(MissionCraftWheatMinion.class);
            return;
        }
        if (!data.hasCompleted(MissionTalkToFarmhandAgain.class)) {
            setDialogue(e.player(), "spoke-again").thenRun(() -> {
                data.endMission(MissionTalkToFarmhandAgain.class);
            });
            return;
        }
    }
}
