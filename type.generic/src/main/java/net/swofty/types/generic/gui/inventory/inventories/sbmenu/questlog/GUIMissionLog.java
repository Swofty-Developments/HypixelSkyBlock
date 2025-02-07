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

import java.util.*;

public class GUIMissionLog extends SkyBlockAbstractInventory {
    private static final String STATE_SHOWING_COMPLETED = "showing_completed";
    private static final String STATE_SHOWING_ONGOING = "showing_ongoing";

    private static final int[] MISSION_SLOTS = {
            11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final Map<Integer, Integer> slotToIndexMap;

    public GUIMissionLog() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Quest Log")));

        // Pre-compute slot to index mapping
        slotToIndexMap = new HashMap<>();
        for (int i = 0; i < MISSION_SLOTS.length; i++) {
            slotToIndexMap.put(MISSION_SLOTS[i], i);
        }
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Set initial state
        doAction(new AddStateAction(STATE_SHOWING_ONGOING));

        // Fill background
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        setupNavigationButtons(player);
        setupMissionSlots(player);
    }

    private void setupNavigationButtons(SkyBlockPlayer player) {
        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return false;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return false;
                })
                .build());

        // Header item
        attachItem(GUIItem.builder(4)
                .item(() -> ItemStackCreator.getStack("§aQuest Log" +
                                (hasState(STATE_SHOWING_COMPLETED) ? " (Completed)" : ""),
                        Material.WRITABLE_BOOK, 1,
                        "§7View your " + (hasState(STATE_SHOWING_COMPLETED) ? "completed" : "active") + " quests,",
                        "§7progress, and rewards.").build())
                .build());

        // Toggle view button
        attachItem(GUIItem.builder(50)
                .item(() -> ItemStackCreator.getStack(
                        hasState(STATE_SHOWING_COMPLETED) ? "§aOngoing Quests" : "§aCompleted Quests",
                        Material.BOOK, 1,
                        hasState(STATE_SHOWING_COMPLETED) ?
                                new String[]{"§7View quests you are currently",
                                        "§7working towards.",
                                        "§7 ",
                                        "§eClick to view!"} :
                                new String[]{"§7Take a peek at the past and",
                                        "§7browse quests you've",
                                        "§7already completed.",
                                        "§f ",
                                        "§7Completed: §a" + player.getMissionData().getCompletedMissions().size(),
                                        "§7 ",
                                        "§eClick to view!"}).build())
                .onClick((ctx, item) -> {
                    if (hasState(STATE_SHOWING_COMPLETED)) {
                        doAction(new RemoveStateAction(STATE_SHOWING_COMPLETED));
                        doAction(new AddStateAction(STATE_SHOWING_ONGOING));
                        doAction(new SetTitleAction(Component.text("Quest Log")));
                    } else {
                        doAction(new RemoveStateAction(STATE_SHOWING_ONGOING));
                        doAction(new AddStateAction(STATE_SHOWING_COMPLETED));
                        doAction(new SetTitleAction(Component.text("Quest Log (Completed)")));
                    }
                    return false;
                })
                .build());

        // Fairy souls button
        attachItem(GUIItem.builder(10)
                .item(() -> ItemStackCreator.getStackHead("§eFind all Fairy Souls",
                        "b96923ad247310007f6ae5d326d847ad53864cf16c3565a181dc8e6b20be2387", 1,
                        "",
                        "  §c✖ §eFound: " + player.getFairySoulHandler().getTotalFoundFairySouls() + "/" + FairySoulDatabase.getAllSouls().size(),
                        "",
                        "§7Forever ongoing quest...",
                        "",
                        "§eClick to view details!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIFairySoulsGuide());
                    return false;
                })
                .build());
    }

    private void setupMissionSlots(SkyBlockPlayer player) {
        for (int slot : MISSION_SLOTS) {
            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        int index = slotToIndexMap.get(slot);

                        // Get the relevant mission list based on state
                        List<MissionSet> missions = getMissionsToShow(player.getMissionData(),
                                hasState(STATE_SHOWING_COMPLETED));

                        // Check if there's a mission for this slot
                        if (index >= missions.size()) {
                            return ItemStack.AIR;
                        }

                        return createMissionDisplay(missions.get(index), player);
                    })
                    .build());
        }
    }

    private synchronized List<MissionSet> getMissionsToShow(MissionData missionData, boolean completed) {
        List<MissionSet> missionsToShow = new ArrayList<>();

        for (MissionSet set : MissionSet.values()) {
            boolean isCompleted = true;
            for (Class<? extends SkyBlockMission> mission : set.getMissions()) {
                Map.Entry<MissionData.ActiveMission, Boolean> missionEntry = missionData.getMission(mission);
                if (missionEntry == null || !missionEntry.getValue()) {
                    isCompleted = false;
                    break;
                }
            }

            if (isCompleted == completed) {
                missionsToShow.add(set);
            }
        }

        return missionsToShow;
    }

    private ItemStack createMissionDisplay(MissionSet missionSet, SkyBlockPlayer player) {
        List<String> lore = new ArrayList<>();
        lore.add("§7 ");

        MissionData missionData = player.getMissionData();

        for (Class<? extends SkyBlockMission> mission : missionSet.getMissions()) {
            Map.Entry<MissionData.ActiveMission, Boolean> missionEntry = missionData.getMission(mission);

            if (missionEntry == null) {
                try {
                    lore.add(" §c✗§e " + mission.newInstance().getName() + ".");
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            SkyBlockMission skyBlockMission = MissionData.getMissionClass(missionEntry.getKey().getMissionID());
            SkyBlockProgressMission progressMission = missionData.getAsProgressMission(skyBlockMission.getID());

            lore.add(" " + (missionEntry.getValue() ? "§a✓§f " : "§c✗§e ") + skyBlockMission.getName()
                    + (progressMission != null ? ". §7(§b" + missionEntry.getKey().getMissionProgress()
                    + "§7/§b" + progressMission.getMaxProgress() + ")" : "."));
        }

        lore.add("§7 ");

        Map.Entry<MissionData.ActiveMission, Boolean> firstMission = missionData.getMission(missionSet.getMissions()[0]);
        if (firstMission != null) {
            MissionData.ActiveMission mission = firstMission.getKey();
            lore.add("§7Started:");
            lore.add("§f  " + SkyBlockCalendar.getMonthName(SkyBlockCalendar.getMonth(mission.getMissionStarted()))
                    + " " + StringUtility.ntify(SkyBlockCalendar.getDay(mission.getMissionStarted())));
            lore.add("§7  " + SkyBlockCalendar.getDisplay(mission.getMissionStarted()));

            if (hasState(STATE_SHOWING_COMPLETED)) {
                lore.add("§7 ");
                lore.add("§7Completed:");
                lore.add("§f  " + SkyBlockCalendar.getMonthName(SkyBlockCalendar.getMonth(mission.getMissionEnded()))
                        + " " + StringUtility.ntify(SkyBlockCalendar.getDay(mission.getMissionEnded())));
                lore.add("§7  " + SkyBlockCalendar.getDisplay(mission.getMissionEnded()));
            }
        } else {
            lore.add("§7Not Yet Started");
        }

        return ItemStackCreator.enchant(
                ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(missionSet.name()),
                        Material.PAPER, 1, lore)).build();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}