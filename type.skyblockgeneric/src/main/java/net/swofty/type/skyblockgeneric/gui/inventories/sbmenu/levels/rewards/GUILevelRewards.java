package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
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
        return new ViewConfiguration<>(I18n.string("gui_sbmenu.levels.rewards.title"), InventoryType.CHEST_4_ROW);
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
            lore.addAll(I18n.lore("gui_sbmenu.levels.rewards.feature.lore"));
            lore.add(" ");
            lore.add(I18n.string("gui_sbmenu.levels.rewards.next_reward"));

            Map.Entry<Integer, List<CustomLevelAward>> nextAward = CustomLevelAward.getNextReward(experience.getLevel().asInt());
            if (nextAward == null) {
                lore.add(I18n.string("gui_sbmenu.levels.rewards.no_more"));
            } else {
                nextAward.getValue().forEach(award -> lore.add("§7" + award.getDisplay()));
                lore.add(I18n.string("gui_sbmenu.levels.rewards.at_level", Map.of("level", String.valueOf(nextAward.getKey()))));
            }

            lore.add(" ");
            lore.addAll(getAsDisplay(CustomLevelAward.getFromLevel(experience.getLevel().asInt()).size(),
                    CustomLevelAward.getTotalLevelAwards()));
            lore.add(" ");
            lore.add(I18n.string("gui_sbmenu.levels.rewards.click_to_view"));

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.rewards.feature"), Material.NETHER_STAR, 1, lore);
        }, (click, c) -> c.player().openView(new GUILevelFeatureRewards()));

        // Prefix Color Rewards
        layout.slot(12, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>();
            lore.addAll(I18n.lore("gui_sbmenu.levels.rewards.prefix.lore"));
            lore.add(" ");
            lore.add(I18n.string("gui_sbmenu.levels.rewards.next_reward"));

            Map.Entry<SkyBlockLevelRequirement, String> nextPrefix = player.getSkyBlockExperience()
                    .getLevel().getNextPrefixChange();
            if (nextPrefix == null) {
                lore.add(I18n.string("gui_sbmenu.levels.rewards.no_more"));
            } else {
                lore.add(nextPrefix.getValue() + nextPrefix.getKey().getPrefixDisplay());
                lore.add(I18n.string("gui_sbmenu.levels.rewards.at_level", Map.of("level", String.valueOf(nextPrefix.getKey().asInt()))));
            }
            lore.add(" ");
            lore.addAll(getAsDisplay(
                    player.getSkyBlockExperience().getLevel().getPreviousPrefixChanges().size(),
                    SkyBlockLevelRequirement.getAllPrefixChanges().size()
            ));
            lore.add(" ");
            lore.add(I18n.string("gui_sbmenu.levels.rewards.click_to_view"));

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.rewards.prefix"), Material.GRAY_DYE, 1, lore);
        }, (click, c) -> c.player().openView(new GUILevelPrefixRewards()));

        // Emblem Rewards
        layout.slot(13, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>();
            lore.addAll(I18n.lore("gui_sbmenu.levels.rewards.emblem.lore"));
            lore.add(" ");
            lore.add(I18n.string("gui_sbmenu.levels.rewards.next_reward"));

            List<SkyBlockEmblems.SkyBlockEmblem> levelEmblems = SkyBlockEmblems.getEmblemsWithLevelCause();
            SkyBlockEmblems.SkyBlockEmblem nextEmblem = null;
            for (SkyBlockEmblems.SkyBlockEmblem emblem : levelEmblems) {
                if (player.getSkyBlockExperience().hasExperienceFor(emblem.cause())) continue;
                nextEmblem = emblem;
                break;
            }

            if (nextEmblem == null) {
                lore.add(I18n.string("gui_sbmenu.levels.rewards.no_more"));
            } else {
                lore.add("§f" + nextEmblem.displayName() + " " + nextEmblem.emblem());
                lore.add(I18n.string("gui_sbmenu.levels.rewards.at_level", Map.of("level", String.valueOf(((LevelCause) nextEmblem.cause()).getLevel()))));
            }

            lore.add(" ");
            lore.addAll(getAsDisplay(
                    player.getSkyBlockExperience().getOfType(LevelCause.class).size(),
                    levelEmblems.size()
            ));
            lore.add(" ");
            lore.add(I18n.string("gui_sbmenu.levels.rewards.click_to_view"));

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.rewards.emblem"), Material.NAME_TAG, 1, lore);
        }, (click, c) -> c.player().openView(new GUILevelEmblemRewards()));

        // Statistic Rewards
        layout.slot(14, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockLevelRequirement nextLevel = player.getSkyBlockExperience().getLevel().getNextLevel();

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.rewards.statistic"), Material.DIAMOND_HELMET, 1,
                    I18n.lore("gui_sbmenu.levels.rewards.statistic.lore", Map.of("next_level", nextLevel == null ? "§cMAX" : String.valueOf(nextLevel.asInt()))));
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
        toReturn.add(I18n.string("gui_sbmenu.levels.rewards.unlocked", Map.of("percent", unlockedPercentage)));

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
