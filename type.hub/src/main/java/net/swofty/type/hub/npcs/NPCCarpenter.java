package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.MissionGiveWoolToCarpenter;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCCarpenter extends HypixelNPC {

    public NPCCarpenter() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§fCarpenter", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "b7Z5Yu3WTa01ELIE8FfLvVIu4kyeKFscKRhtjbKlaIUJyaM1hcClDCmgjXcEUmn/47L57jMBYJAspmBhu1402XQkm+Au+6pUBNp1j9uSTAcec5IcpxqkRgOkXMx8HAoH5WTvoLFYwpC8Ff/PSK835WAKHYW4K3o13QcJrlAKenMHBcwXewNy6CNHoXbJwmCzoPTWDjWyE1f2DfKff5hp1WZUFwODOXKMJEFY1+DzWKAwvQSRse+O/89HdB4CMRKbfQnfDYu8m/VRx+86JopCu9PZZk5J5vGdfhrGxChY7H9Lp9I8QADgjU3rdAfbE7AXj95vU6MfyEyOuAxnvTXPHa+vFwTj//1eTJ0+vxfPibg6gqW5FktR8daSjxoOvLqGST1FDn/O3hC3rBm1If2Y9KJgGzDiw+7Loc+oNMprtPriajydwJlcKBrhKAQINDrw3v1xKhphuqSq/+cjoz4OAGPxSqlJ1rN6OvU7HluATXbPhRvJS8PeRaBSLYMqnfqH7o1M2gt5f5D1Wjf9WMVMjqVMBRGjWDcoEvTECW9HHdb8w7vG0uoH3yzdFvHu96Zs4ADDS5eIYXTUjglZDICS06vGm++5cVRq2z2lHmllFsP/KIZY93JhWdfjDm4cuASohQmN+TLznX8C1PInlB4kYnQKyoJiiRss0EoqGZUyMxc=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzU4MDg3MDAwNiwKICAicHJvZmlsZUlkIiA6ICI0ZTMwZjUwZTdiYWU0M2YzYWZkMmE3NDUyY2ViZTI5YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJfdG9tYXRvel8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M4ZDJhNWFmOTc1YzUzY2YwODFiNTMxNTY2MTQ4YmFlMzY2MTQxMzE0YjM1YWE1NzI2ZTk3Y2M5N2VmMTE4YiIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(15.5, 72, -21.5, 90, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;
        MissionData data = player.getMissionData();

        if (!data.isCurrentlyActive(MissionGiveWoolToCarpenter.class) && !data.hasCompleted(MissionGiveWoolToCarpenter.class)) {
            setDialogue(player, "initial-hello").thenRun(() -> {
                data.startMission(MissionGiveWoolToCarpenter.class);
            });
            return;
        }
        if (!data.hasCompleted(MissionGiveWoolToCarpenter.class) && data.isCurrentlyActive(MissionGiveWoolToCarpenter.class)) {
            if (player.getAmountInInventory(ItemType.WHITE_WOOL) >= 64) {
                player.takeItem(ItemType.WHITE_WOOL, 64);
                setDialogue(player, "completed-quest").thenRun(() -> {
                    data.endMission(MissionGiveWoolToCarpenter.class);
                });
            } else {
                player.sendMessage("Come back with a stack of White Wool!");
            }
            return;
        }
        if (data.hasCompleted(MissionGiveWoolToCarpenter.class)) {
            setDialogue(player, "spoke-again");
        }
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "Hi, " + player.getUsername() + "! Welcome to the §aFurniture Shop§f.",
                                "Sales are too good right now, I can't keep up with the demand!",
                                "Could you bring a §astack of White Wool§f to help replenish my stock?",
                                "Sheep over in The Barn drop wool, but you can also purchase it from the §dWool Weaver§f.",
                                "She lives in a house not far from here - it's over by the water fountain."
                        }).build(),
                DialogueSet.builder()
                        .key("completed-quest").lines(new String[]{
                                "Wow, thanks so much for the help!",
                                "Carpentry is my passion, I always love to teach others.",
                                "Here's the recipe for the §aCarpentry Table§f. You can place it in your world and craft furniture that you've unlocked!",
                                "You can now gain Carpentry XP by crafting items. Leveling your §aCarpentry Skill§f unlocks new furniture recipes!",
                                "Some furniture is available exclusively in the Furniture Shop downstairs. Check it out!"
                        }).build(),
                DialogueSet.builder()
                        .key("spoke-again").lines(new String[]{
                                "Check out the Furniture Shop downstairs!",
                                "The Furniture Shop is downstairs. Purchase cool furniture down there!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
