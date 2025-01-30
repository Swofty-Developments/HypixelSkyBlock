package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.emblem.GUIEmblems;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.rewards.GUILevelRewards;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockLevels extends SkyBlockAbstractInventory {

    public GUISkyBlockLevels() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("SkyBlock Leveling")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        setupNavigationButtons();
        setupLevelInformationDisplay();
        setupFeatureButtons();
        setupLevelProgress();
        setupMilestoneLevelDisplay();
    }

    private void setupNavigationButtons() {
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());
    }

    private void setupLevelInformationDisplay() {
        attachItem(GUIItem.builder(4)
                .item(() -> {
                    SkyBlockLevelRequirement level = owner.getSkyBlockExperience().getLevel();
                    int completedChallenges = owner.getSkyBlockExperience().getCompletedExperienceCauses().size();
                    int totalChallenges = SkyBlockLevelCause.getAmountOfCauses();

                    return ItemStackCreator.getStack("§aYour SkyBlock Level Ranking",
                            Material.PAINTING, 1,
                            "§8Classic Mode",
                            " ",
                            "§7Your level: " + level.getColor() + level,
                            "§7You have: §b" + Math.round(owner.getSkyBlockExperience().getTotalXP()) + " XP",
                            " ",
                            "§7You have completed §3" + (new DecimalFormat("##.##").format((double) completedChallenges / totalChallenges * 100)) + "% §7of the total",
                            "§7SkyBlock XP Tasks.").build();
                })
                .build());

        // Toggle SkyBlock Levels in Chat
        attachItem(GUIItem.builder(50)
                .item(() -> {
                    boolean enabled = owner.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT);
                    return ItemStackCreator.getStack("§bSkyBlock Levels in Chat",
                            enabled ? Material.LIME_DYE : Material.GRAY_DYE,
                            1,
                            "§7View other players' SkyBlock Level",
                            "§7and their selected emblem in their",
                            "§7chat messages.",
                            " ",
                            enabled ? "§a§lENABLED" : "§c§lDISABLED",
                            " ",
                            "§eClick to toggle!").build();
                })
                .onClick((ctx, item) -> {
                    boolean current = ctx.player().getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT);
                    ctx.player().sendMessage(current ?
                            "§cSkyBlock Levels in Chat is now disabled!" :
                            "§aSkyBlock Levels in Chat is now enabled!");
                    ctx.player().getToggles().set(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT, !current);
                    return true;
                })
                .build());
    }

    private void setupFeatureButtons() {
        // Level Rewards
        attachItem(GUIItem.builder(34)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Unlock rewards for leveling up");
                    lore.add("§7your SkyBlock Level.");
                    lore.add(" ");
                    lore.addAll(GUILevelRewards.getAsDisplay(GUILevelRewards.getUnlocked(owner),
                            GUILevelRewards.getTotalAwards()));
                    lore.add(" ");
                    lore.add("§eClick to view rewards!");

                    return ItemStackCreator.getStack("§aLeveling Rewards", Material.CHEST, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUILevelRewards());
                    return true;
                })
                .build());

        // SkyBlock Guide
        attachItem(GUIItem.builder(25)
                .item(ItemStackCreator.getStack("§aSkyBlock Guide", Material.FILLED_MAP, 1,
                        "§7Your §6SkyBlock Guide §7tracks the",
                        "§7progress you have made through",
                        "§7SkyBlock.",
                        " ",
                        "§7Complete tasks within your current",
                        "§7game stage to increase your",
                        "§bSkyBlock Level §7and become a §dMaster",
                        "§7of SkyBlock!",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUILevelsGuide());
                    return true;
                })
                .build());

        // Emblems
        attachItem(GUIItem.builder(43)
                .item(ItemStackCreator.getStack("§aPrefix Emblems", Material.NAME_TAG, 1,
                        "§7Add some spice by having an emblem",
                        "§7next to your name in chat and in tab!",
                        " ",
                        "§7Emblems are unlocked through various",
                        "§7activities such as leveling up",
                        "§7or completing achievements!",
                        " ",
                        "§7Emblems also show important data",
                        "§7associated with them in chat!",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIEmblems());
                    return true;
                })
                .build());
    }

    private void setupLevelProgress() {
        SkyBlockLevelRequirement currentLevel = owner.getSkyBlockExperience().getLevel();
        List<SkyBlockLevelRequirement> levels = new ArrayList<>();
        levels.add(currentLevel);
        for (int i = 0; i < 5; i++) {
            if (currentLevel.getNextLevel() == null) break;
            levels.add(currentLevel.getNextLevel());
            currentLevel = currentLevel.getNextLevel();
        }

        int unlockedLevel = owner.getSkyBlockExperience().getLevel().asInt();
        for (int i = 0; i < 5; i++) {
            if (i >= levels.size() || levels.get(i) == null) break;

            SkyBlockLevelRequirement level = levels.get(i);
            attachLevelProgressItem(19 + i, level, unlockedLevel);
        }
    }

    private void attachLevelProgressItem(int slot, SkyBlockLevelRequirement level, int unlockedLevel) {
        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    Material material = level.isMilestone() ? Material.RED_STAINED_GLASS : Material.RED_STAINED_GLASS_PANE;

                    if (unlockedLevel == level.asInt()) {
                        lore.add("§8Your Level");
                        lore.add(" ");
                        material = level.isMilestone() ? Material.LIME_STAINED_GLASS : Material.LIME_STAINED_GLASS_PANE;
                    } else if (unlockedLevel + 1 == level.asInt()) {
                        lore.add("§8Next Level");
                        lore.add(" ");
                        material = level.isMilestone() ? Material.YELLOW_STAINED_GLASS : Material.YELLOW_STAINED_GLASS_PANE;
                    }

                    lore.add("§7Rewards:");
                    level.getUnlocks().forEach(unlock -> lore.addAll(unlock.getDisplay(owner, level.asInt())));
                    lore.add(" ");
                    if (unlockedLevel == level.asInt()) {
                        lore.add("§a§lUNLOCKED");
                        lore.add(" ");
                    }
                    lore.add("§eClick to view rewards!");

                    return ItemStackCreator.getStack("§7Level " + level.asInt(), material, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockLevel(level));
                    return true;
                })
                .build());
    }

    private void setupMilestoneLevelDisplay() {
        SkyBlockLevelRequirement currentLevelMilestone = owner.getSkyBlockExperience().getLevel().getNextMilestoneLevel();
        if (currentLevelMilestone == null) return;

        attachItem(GUIItem.builder(30)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8Next Milestone Level");
                    lore.add(" ");
                    lore.add("§7Rewards:");
                    currentLevelMilestone.getUnlocks().forEach(unlock ->
                            lore.addAll(unlock.getDisplay(owner, currentLevelMilestone.asInt())));
                    lore.add(" ");
                    lore.add("§7XP Left to Gain: §b" + (currentLevelMilestone.getCumulativeExperience() - owner.getSkyBlockExperience().getTotalXP())
                            + " XP §8(" + (int) (owner.getSkyBlockExperience().getTotalXP() / currentLevelMilestone.getCumulativeExperience() * 100) + "%)");
                    lore.add(" ");
                    lore.add("§eClick to view rewards!");

                    return ItemStackCreator.getStack("§7Level " + currentLevelMilestone.asInt(),
                            Material.PURPLE_STAINED_GLASS_PANE,
                            1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockLevel(currentLevelMilestone));
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}