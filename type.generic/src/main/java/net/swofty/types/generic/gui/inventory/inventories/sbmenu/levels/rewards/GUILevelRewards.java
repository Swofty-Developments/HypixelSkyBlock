package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.rewards;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.GUISkyBlockLevels;
import net.swofty.types.generic.levels.CustomLevelAward;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.levels.causes.LevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUILevelRewards extends SkyBlockAbstractInventory {

    public GUILevelRewards() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Leveling Rewards")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Close button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(30)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + new GUISkyBlockLevels().getTitle()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockLevels());
                    return true;
                })
                .build());

        setupFeatureRewardsItem(player);
        setupPrefixRewardsItem(player);
        setupEmblemRewardsItem(player);
        setupStatisticRewardsItem(player);
    }

    private void setupFeatureRewardsItem(SkyBlockPlayer player) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();

        attachItem(GUIItem.builder(11)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Specific game features such as the");
                    lore.add("§7Bazaar or Community Shop.");
                    lore.add(" ");
                    lore.add("§7Next Reward:");

                    Map.Entry<Integer, List<CustomLevelAward>> nextAward = CustomLevelAward.getNextReward(
                            experience.getLevel().asInt()
                    );
                    if (nextAward == null) {
                        lore.add("§cNo more rewards!");
                    } else {
                        nextAward.getValue().forEach(award -> lore.add("§7" + award.getDisplay()));
                        lore.add("§8at Level " + nextAward.getKey());
                    }

                    lore.add(" ");
                    lore.addAll(getAsDisplay(CustomLevelAward.getFromLevel(experience.getLevel().asInt()).size(),
                            CustomLevelAward.getTotalLevelAwards()));
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStack("§aFeature Rewards", Material.NETHER_STAR, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUILevelFeatureRewards());
                    return true;
                })
                .build());
    }

    private void setupPrefixRewardsItem(SkyBlockPlayer player) {
        attachItem(GUIItem.builder(12)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7New colors for your level prefix");
                    lore.add("§7shown in TAB and in chat!");
                    lore.add(" ");
                    lore.add("§7Next Reward:");

                    Map.Entry<SkyBlockLevelRequirement, String> nextPrefix = player.getSkyBlockExperience()
                            .getLevel().getNextPrefixChange();
                    if (nextPrefix == null) {
                        lore.add("§cNo more rewards!");
                    } else {
                        lore.add(nextPrefix.getValue() + nextPrefix.getKey().getPrefixDisplay());
                        lore.add("§8at Level " + nextPrefix.getKey().asInt());
                    }
                    lore.add(" ");
                    lore.addAll(getAsDisplay(
                            player.getSkyBlockExperience().getLevel().getPreviousPrefixChanges().size(),
                            SkyBlockLevelRequirement.getAllPrefixChanges().size()
                    ));
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStack("§aPrefix Color Rewards", Material.GRAY_DYE, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUILevelPrefixRewards());
                    return true;
                })
                .build());
    }

    private void setupEmblemRewardsItem(SkyBlockPlayer player) {
        attachItem(GUIItem.builder(13)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Emblems to show next to your name");
                    lore.add("§7that signify special achievements.");
                    lore.add(" ");
                    lore.add("§7Next Reward:");

                    List<SkyBlockEmblems.SkyBlockEmblem> levelEmblems = SkyBlockEmblems.getEmblemsWithLevelCause();
                    SkyBlockEmblems.SkyBlockEmblem nextEmblem = null;
                    for (SkyBlockEmblems.SkyBlockEmblem emblem : levelEmblems) {
                        if (player.getSkyBlockExperience().hasExperienceFor(emblem.cause())) continue;
                        nextEmblem = emblem;
                        break;
                    }

                    if (nextEmblem == null) {
                        lore.add("§cNo more rewards!");
                    } else {
                        lore.add("§f" + nextEmblem.displayName() + " " + nextEmblem.emblem());
                        lore.add("§8at Level " + ((LevelCause) nextEmblem.cause()).getLevel());
                    }

                    lore.add(" ");
                    lore.addAll(getAsDisplay(
                            player.getSkyBlockExperience().getOfType(LevelCause.class).size(),
                            levelEmblems.size()
                    ));
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStack("§aEmblem Rewards", Material.NAME_TAG, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUILevelEmblemRewards());
                    return true;
                })
                .build());
    }

    private void setupStatisticRewardsItem(SkyBlockPlayer player) {
        attachItem(GUIItem.builder(14)
                .item(() -> {
                    SkyBlockLevelRequirement nextLevel = player.getSkyBlockExperience().getLevel().getNextLevel();

                    return ItemStackCreator.getStack("§aStatistic Rewards",
                            Material.DIAMOND_HELMET, 1,
                            "§7Statistic bonuses that will power you",
                            "§7up as you level up.",
                            " ",
                            "§7Next Reward:",
                            "§8+§a5 §cHealth",
                            "§8at Level " + (nextLevel == null ? "§cMAX" : nextLevel.asInt()),
                            " ",
                            "§7For every level:",
                            "§8+§a5 §cHealth",
                            " ",
                            "§7For every 5 levels:",
                            "§8+§a1 §cStrength").build();
                })
                .build());
    }

    public static int getTotalAwards() {
        int amountToReturn = 0;
        amountToReturn += CustomLevelAward.getTotalLevelAwards();
        amountToReturn += SkyBlockLevelRequirement.getAllPrefixChanges().size();
        amountToReturn += SkyBlockEmblems.getEmblemsWithLevelCause().size();
        return amountToReturn;
    }

    public static int getUnlocked(SkyBlockPlayer player) {
        int amountToReturn = 0;
        amountToReturn += CustomLevelAward.getFromLevel(player.getSkyBlockExperience().getLevel().asInt()).size();
        amountToReturn += player.getSkyBlockExperience().getLevel().getPreviousPrefixChanges().size();
        amountToReturn += player.getSkyBlockExperience().getOfType(LevelCause.class).size();
        return amountToReturn;
    }

    public static List<String> getAsDisplay(int unlocked, int total) {
        List<String> toReturn = new ArrayList<>();

        String unlockedPercentage = String.format("%.2f", (unlocked / (double) total) * 100);
        toReturn.add("§7Rewards Unlocked: §3" + unlockedPercentage + "%");

        String baseLoadingBar = "─────────────────";
        int maxBarLength = baseLoadingBar.length();
        int completedLength = (int) ((unlocked / (double) total) * maxBarLength);

        String completedLoadingBar = "§b§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
        int formattingCodeLength = 4;
        String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                completedLoadingBar.length() - formattingCodeLength,
                maxBarLength
        ));

        toReturn.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + unlocked + "§6/§e" + total);
        return toReturn;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}