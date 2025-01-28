package net.swofty.types.generic.gui.inventory.inventories.sbmenu.questlog;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.data.mongodb.FairySoulDatabase;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.MissionSet;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.mission.SkyBlockProgressMission;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GUIMissionLog extends SkyBlockAbstractInventory {
    private static final String STATE_COMPLETED = "completed";
    private static final int[] MISSION_SLOTS = {
            11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public GUIMissionLog() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Quest Log")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build(), 0, 8);
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build(), 45, 53);

        setupNavigationButtons();
        setupMissionDisplay();
        refreshDisplay();
    }

    private void setupNavigationButtons() {
        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        // Quest log header
        attachItem(GUIItem.builder(4)
                .item(() -> ItemStackCreator.getStack(
                        "§aQuest Log " + (hasState(STATE_COMPLETED) ? "(Completed)" : ""),
                        Material.WRITABLE_BOOK, 1,
                        "§7View your active quests,",
                        "§7progress, and rewards.").build())
                .build());

        // Toggle completed/ongoing button
        attachItem(GUIItem.builder(50)
                .item(() -> {
                    if (hasState(STATE_COMPLETED)) {
                        return ItemStackCreator.getStack("§aOngoing Quests", Material.BOOK, 1,
                                "§7View quests you are currently",
                                "§7working towards.",
                                "§7 ",
                                "§eClick to view!").build();
                    } else {
                        return ItemStackCreator.getStack("§aCompleted Quests", Material.BOOK, 1,
                                "§7Take a peek at the past and",
                                "§7browse quests you've,",
                                "§7already completed.",
                                "§f ",
                                "§7Completed: §a" + owner.getMissionData().getCompletedMissions().size(),
                                "§7 ",
                                "§eClick to view!").build();
                    }
                })
                .onClick((ctx, item) -> {
                    if (hasState(STATE_COMPLETED)) {
                        doAction(new RemoveStateAction(STATE_COMPLETED));
                    } else {
                        doAction(new AddStateAction(STATE_COMPLETED));
                    }
                    refreshDisplay();
                    return true;
                })
                .build());
    }

    private void setupMissionDisplay() {
        // Fairy souls button
        attachItem(GUIItem.builder(10)
                .item(() -> ItemStackCreator.getStackHead("§eFind all Fairy Souls",
                        "b96923ad247310007f6ae5d326d847ad53864cf16c3565a181dc8e6b20be2387", 1,
                        "",
                        "  §c✖ §eFound: " + owner.getFairySoulHandler().getTotalFoundFairySouls() + "/" + FairySoulDatabase.getAllSouls().size(),
                        "",
                        "§7Forever ongoing quest...",
                        "",
                        "§eClick to view details!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIFairySoulsGuide());
                    return true;
                })
                .build());

        // Clear mission slots
        Arrays.stream(MISSION_SLOTS).forEach(slot ->
                attachItem(GUIItem.builder(slot).item(ItemStack.AIR).build()));
    }

    private void refreshDisplay() {
        doAction(new SetTitleAction(Component.text("Quest Log " + (hasState(STATE_COMPLETED) ? "(Completed)" : ""))));

        MissionData missionData = owner.getMissionData();
        List<MissionSet> missions = getMissionsToShow(missionData);

        for (int i = 0; i < missions.size(); i++) {
            MissionSet missionSet = missions.get(i);
            attachItem(createMissionItem(MISSION_SLOTS[i], missionSet, missionData));
        }
    }

    private List<MissionSet> getMissionsToShow(MissionData missionData) {
        List<MissionSet> completedMissions = new ArrayList<>();
        List<MissionSet> activeMissions = new ArrayList<>();

        for (MissionSet set : MissionSet.values()) {
            boolean completedSet = true;
            for (Class<? extends SkyBlockMission> mission : set.getMissions()) {
                if (missionData.getMission(mission) == null || !missionData.getMission(mission).getValue()) {
                    completedSet = false;
                    break;
                }
            }
            if (completedSet) {
                completedMissions.add(set);
            } else {
                activeMissions.add(set);
            }
        }

        return hasState(STATE_COMPLETED) ? completedMissions : activeMissions;
    }

    private GUIItem createMissionItem(int slot, MissionSet missionSet, MissionData missionData) {
        return GUIItem.builder(slot)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of("§7 "));
                    addMissionProgress(lore, missionSet, missionData);
                    addMissionTimes(lore, missionSet, missionData);

                    return ItemStackCreator.enchant(
                            ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(missionSet.name()),
                                    Material.PAPER, 1,
                                    lore)).build();
                })
                .build();
    }

    private void addMissionProgress(List<String> lore, MissionSet missionSet, MissionData missionData) {
        Arrays.stream(missionSet.getMissions()).forEach(mission -> {
            Map.Entry<MissionData.ActiveMission, Boolean> activeMission = missionData.getMission(mission);

            if (activeMission == null) {
                try {
                    lore.add(" §c✗§e " + mission.newInstance().getName() + ".");
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            SkyBlockMission skyBlockMission = MissionData.getMissionClass(activeMission.getKey().getMissionID());
            SkyBlockProgressMission progressMission = missionData.getAsProgressMission(skyBlockMission.getID());

            lore.add(" " + (activeMission.getValue() ? "§a✓§f " : "§c✗§e ") + skyBlockMission.getName()
                    + (progressMission != null ? ". §7(§b" + activeMission.getKey().getMissionProgress()
                    + "§7/§b" + progressMission.getMaxProgress() + ")" : "."));
        });
        lore.add("§7 ");
    }

    private void addMissionTimes(List<String> lore, MissionSet missionSet, MissionData missionData) {
        Map.Entry<MissionData.ActiveMission, Boolean> firstMissionEntry = missionData.getMission(missionSet.getMissions()[0]);
        if (firstMissionEntry != null) {
            MissionData.ActiveMission firstMission = firstMissionEntry.getKey();
            lore.add("§7Started:");
            lore.add("§f  " + SkyBlockCalendar.getMonthName(SkyBlockCalendar.getMonth(firstMission.getMissionStarted()))
                    + " " + StringUtility.ntify(SkyBlockCalendar.getDay(firstMission.getMissionStarted())));
            lore.add("§7  " + SkyBlockCalendar.getDisplay(firstMission.getMissionStarted()));

            if (hasState(STATE_COMPLETED)) {
                lore.add("§7 ");
                lore.add("§7Completed:");
                lore.add("§f  " + SkyBlockCalendar.getMonthName(SkyBlockCalendar.getMonth(firstMission.getMissionEnded()))
                        + " " + StringUtility.ntify(SkyBlockCalendar.getDay(firstMission.getMissionEnded())));
                lore.add("§7  " + SkyBlockCalendar.getDisplay(firstMission.getMissionEnded()));
            }
        } else {
            lore.add("§7Not Yet Started");
        }
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {}
}