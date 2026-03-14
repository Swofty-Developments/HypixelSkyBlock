package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.emblem.GUIEmblems;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards.GUILevelRewards;
import net.swofty.type.skyblockgeneric.levels.LevelsGuide;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUISkyBlockLevels extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(I18n.string("gui_sbmenu.levels.main.title"), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // Toggle SkyBlock Levels in Chat
        layout.slot(50, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            String status = player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT)
                    ? I18n.string("gui_sbmenu.levels.main.chat_toggle.enabled") : I18n.string("gui_sbmenu.levels.main.chat_toggle.disabled");
            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.main.chat_toggle"),
                    player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT)
                            ? Material.LIME_DYE : Material.GRAY_DYE,
                    1,
                    I18n.lore("gui_sbmenu.levels.main.chat_toggle.lore", Map.of("status", status)));
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            player.sendMessage(player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT) ?
                    I18n.string("gui_sbmenu.levels.main.msg.chat_disabled") :
                    I18n.string("gui_sbmenu.levels.main.msg.chat_enabled"));
            player.getToggles().set(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT,
                    !player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT));
            c.session(DefaultState.class).render();
        });

        // Level Rewards
        layout.slot(34, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            StringBuilder display = new StringBuilder();
            List<String> displayList = GUILevelRewards.getAsDisplay(GUILevelRewards.getUnlocked(player),
                    GUILevelRewards.getTotalAwards());
            for (int j = 0; j < displayList.size(); j++) {
                display.append(displayList.get(j));
                if (j < displayList.size() - 1) display.append("\n");
            }
            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.main.level_rewards"), Material.CHEST, 1,
                    I18n.lore("gui_sbmenu.levels.main.level_rewards.lore", Map.of("display", display.toString())));
        }, (click, c) -> c.player().openView(new GUILevelRewards()));

        // Your SkyBlock Level Ranking
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockLevelRequirement level = player.getSkyBlockExperience().getLevel();
            int completedChallenges = player.getSkyBlockExperience().getCompletedExperienceCauses().size();
            int totalChallenges = SkyBlockLevelCause.getAmountOfCauses();

            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.main.ranking"),
                    Material.PAINTING, 1,
                    I18n.lore("gui_sbmenu.levels.main.ranking.lore", Map.of(
                            "level_display", level.getColor() + level.toString(),
                            "xp", String.valueOf(Math.round(player.getSkyBlockExperience().getTotalXP())),
                            "percent", new java.text.DecimalFormat("##.##").format((double) completedChallenges / totalChallenges * 100))));
        });

        // SkyBlock Guide
        layout.slot(25, (s, c) -> ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.main.guide"), Material.FILLED_MAP, 1,
                        I18n.lore("gui_sbmenu.levels.main.guide.lore")),
                (click, c) -> c.player().openView(new GUILevelsGuide(LevelsGuide.STARTER)));

        // Prefix Emblems
        layout.slot(43, (s, c) -> ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.main.emblems"), Material.NAME_TAG, 1,
                        I18n.lore("gui_sbmenu.levels.main.emblems.lore")),
                (click, c) -> c.player().openView(new GUIEmblems()));

        // Level progression slots
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        SkyBlockLevelRequirement currentLevel = player.getSkyBlockExperience().getLevel();
        List<SkyBlockLevelRequirement> levels = new ArrayList<>();
        levels.add(currentLevel);
        for (int i = 0; i < 5; i++) {
            if (currentLevel.getNextLevel() == null) break;
            levels.add(currentLevel.getNextLevel());
            currentLevel = currentLevel.getNextLevel();
        }

        int unlockedLevel = player.getSkyBlockExperience().getLevel().asInt();
        for (int i = 0; i < 5 && i < levels.size(); i++) {
            SkyBlockLevelRequirement level = levels.get(i);
            if (level == null) break;
            int slot = 19 + i;

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                List<String> lore = new ArrayList<>();
                Material material = level.isMilestone() ? Material.RED_STAINED_GLASS : Material.RED_STAINED_GLASS_PANE;

                if (unlockedLevel == level.asInt()) {
                    lore.add(I18n.string("gui_sbmenu.levels.main.your_level"));
                    lore.add(" ");
                    material = level.isMilestone() ? Material.LIME_STAINED_GLASS : Material.LIME_STAINED_GLASS_PANE;
                } else if (unlockedLevel + 1 == level.asInt()) {
                    lore.add(I18n.string("gui_sbmenu.levels.main.next_level"));
                    lore.add(" ");
                    material = level.isMilestone() ? Material.YELLOW_STAINED_GLASS : Material.YELLOW_STAINED_GLASS_PANE;
                }

                lore.add(I18n.string("gui_sbmenu.levels.main.rewards"));
                level.getUnlocks().forEach(unlock -> lore.addAll(unlock.getDisplay(p, level.asInt())));
                lore.add(" ");
                if (unlockedLevel == level.asInt()) {
                    lore.add(I18n.string("gui_sbmenu.levels.main.unlocked"));
                    lore.add(" ");
                }
                lore.add(I18n.string("gui_sbmenu.levels.main.click_to_view"));

                return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.main.level", Map.of("level", String.valueOf(level.asInt()))), material, 1, lore);
            }, (click, c) -> c.player().openView(new GUISkyBlockLevel(level)));
        }

        // Next Milestone Level
        SkyBlockLevelRequirement currentMilestone = player.getSkyBlockExperience().getLevel().getNextMilestoneLevel();
        if (currentMilestone != null) {
            layout.slot(30, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_sbmenu.levels.main.milestone"));
                lore.add(" ");
                lore.add(I18n.string("gui_sbmenu.levels.main.rewards"));
                currentMilestone.getUnlocks().forEach(unlock -> lore.addAll(unlock.getDisplay(p, currentMilestone.asInt())));
                lore.add(" ");
                lore.add(I18n.string("gui_sbmenu.levels.main.xp_left", Map.of(
                        "xp", String.valueOf((long) (currentMilestone.getCumulativeExperience() - p.getSkyBlockExperience().getTotalXP())),
                        "percent", String.valueOf((int) (p.getSkyBlockExperience().getTotalXP() / currentMilestone.getCumulativeExperience() * 100)))));
                lore.add(" ");
                lore.add(I18n.string("gui_sbmenu.levels.main.click_to_view"));

                return ItemStackCreator.getStack(I18n.string("gui_sbmenu.levels.main.level", Map.of("level", String.valueOf(currentMilestone.asInt()))), Material.PURPLE_STAINED_GLASS_PANE, 1, lore);
            }, (click, c) -> c.player().openView(new GUISkyBlockLevel(currentMilestone)));
        }
    }
}
