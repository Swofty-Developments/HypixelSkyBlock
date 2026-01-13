package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.type.skyblockgeneric.levels.CustomLevelAward;
import net.swofty.type.skyblockgeneric.levels.SkyBlockEmblems;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import net.swofty.type.skyblockgeneric.levels.causes.LevelCause;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUILevelRewards extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Leveling Rewards", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);
        Components.back(layout, 30, ctx);

        // Feature Rewards
        layout.slot(11, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();
            List<String> lore = new ArrayList<>();
            lore.add("§7Specific game features such as the");
            lore.add("§7Bazaar or Community Shop.");
            lore.add(" ");
            lore.add("§7Next Reward:");

            Map.Entry<Integer, List<CustomLevelAward>> nextAward = CustomLevelAward.getNextReward(experience.getLevel().asInt());
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

            return ItemStackCreator.getStack("§aFeature Rewards", Material.NETHER_STAR, 1, lore);
        }, (click, c) -> c.player().openView(new GUILevelFeatureRewards()));

        // Prefix Color Rewards
        layout.slot(12, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
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

            return ItemStackCreator.getStack("§aPrefix Color Rewards", Material.GRAY_DYE, 1, lore);
        }, (click, c) -> c.player().openView(new GUILevelPrefixRewards()));

        // Emblem Rewards
        layout.slot(13, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
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

            return ItemStackCreator.getStack("§aEmblem Rewards", Material.NAME_TAG, 1, lore);
        }, (click, c) -> c.player().openView(new GUILevelEmblemRewards()));

        // Statistic Rewards
        layout.slot(14, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockLevelRequirement nextLevel = player.getSkyBlockExperience().getLevel().getNextLevel();

            return ItemStackCreator.getStack("§aStatistic Rewards", Material.DIAMOND_HELMET, 1,
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
                    "§8+§a1 §cStrength");
        });
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
}
