package net.swofty.gui.inventory.inventories;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.calendar.SkyBlockCalendar;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.mission.MissionData;
import net.swofty.mission.MissionSet;
import net.swofty.mission.SkyBlockMission;
import net.swofty.mission.SkyBlockProgressMission;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.StringUtility;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUIMissionLog extends SkyBlockInventoryGUI {
    private static final int[] MISSION_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public GUIMissionLog() {
        super("Quest Log", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));
        display(false);
    }

    public void display(boolean completed) {
        getInventory().setTitle(Component.text("Quest Log " + (completed ? "(Completed)" : "")));
        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 4;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aQuest Log " + (completed ? "(Completed)" : ""), Material.WRITABLE_BOOK, (short) 0, 1, "§7View your active quests, progress,", "§7and rewards.");
            }
        });
        if (completed) {
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    display(false);
                }

                @Override
                public int getSlot() {
                    return 50;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aOngoing Quests", Material.BOOK, (short) 0, 1,
                            "§7View quests you are currently",
                            "§7working towards.",
                            "§7 ",
                            "§eClick to view!");
                }
            });
        } else {
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    display(true);
                }

                @Override
                public int getSlot() {
                    return 50;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aCompleted Quests", Material.BOOK, (short) 0, 1,
                            "§7Take a peek at the past and",
                            "§7browse quests you've,",
                            "§7already completed.",
                            "§f ",
                            "§7Completed: §a" + player.getMissionData().getCompletedMissions().size(),
                            "§7 ",
                            "§eClick to view!");
                }
            });
        }

        MissionData missionData = getPlayer().getMissionData();

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

        Arrays.stream(MISSION_SLOTS).forEach(slot -> {
            set(slot, ItemStack.builder(Material.AIR));
        });

        List<MissionSet> toShow = completed ? completedMissions : activeMissions;

        for (int i = 0; i < toShow.size(); i++) {
            MissionSet missionSet = toShow.get(i);
            int finalI = i;
            set(new GUIItem() {
                @Override
                public int getSlot() {
                    return MISSION_SLOTS[finalI];
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    List<String> lore = new ArrayList<>(List.of("§7 "));

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
                    Map.Entry<MissionData.ActiveMission, Boolean> firstMissionInSetEntry = missionData.getMission(missionSet.getMissions()[0]);
                    if (firstMissionInSetEntry != null) {
                        MissionData.ActiveMission firstMissionInSet = firstMissionInSetEntry.getKey();

                        lore.add("§7Started:");
                        lore.add("§f  " + SkyBlockCalendar.getMonthName(
                                SkyBlockCalendar.getMonth(firstMissionInSet.getMissionStarted()))
                                + " " + StringUtility.ntify(SkyBlockCalendar.getDay(firstMissionInSet.getMissionStarted())));
                        lore.add("§7  " + SkyBlockCalendar.getDisplay(firstMissionInSet.getMissionStarted()));
                        if (completed) {
                            lore.add("§7 ");
                            lore.add("§7Completed:");
                            lore.add("§f  " + SkyBlockCalendar.getMonthName(
                                    SkyBlockCalendar.getMonth(firstMissionInSet.getMissionEnded()))
                                    + " " + StringUtility.ntify(SkyBlockCalendar.getDay(firstMissionInSet.getMissionEnded())));
                            lore.add("§7  " + SkyBlockCalendar.getDisplay(firstMissionInSet.getMissionEnded()));
                        }
                    } else {
                        lore.add("§7Not Yet Started");
                    }

                    return ItemStackCreator.enchant(
                            ItemStackCreator.getStack("§a" + StringUtility.toNormalCase(missionSet.name()),
                                    Material.PAPER, (short) 0, 1,
                                    lore));
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
