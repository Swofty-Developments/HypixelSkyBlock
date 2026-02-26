package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.emblem.GUIEmblems;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards.GUILevelRewards;
import net.swofty.type.skyblockgeneric.levels.LevelsGuide;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockLevels extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("SkyBlock Leveling", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // Toggle SkyBlock Levels in Chat
        layout.slot(50, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            return ItemStackCreator.getStack("§bSkyBlock Levels in Chat",
                    player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT)
                            ? Material.LIME_DYE : Material.GRAY_DYE,
                    1,
                    "§7View other players' SkyBlock Level",
                    "§7and their selected emblem in their",
                    "§7chat messages.",
                    " ",
                    player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT)
                            ? "§a§lENABLED" : "§c§lDISABLED",
                    " ",
                    "§eClick to toggle!");
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            player.sendMessage(player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT) ?
                    "§cSkyBlock Levels in Chat is now disabled!" :
                    "§aSkyBlock Levels in Chat is now enabled!");
            player.getToggles().set(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT,
                    !player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT));
            c.session(DefaultState.class).render();
        });

        // Level Rewards
        layout.slot(34, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>();
            lore.add("§7Unlock rewards for leveling up");
            lore.add("§7your SkyBlock Level.");
            lore.add(" ");
            lore.addAll(GUILevelRewards.getAsDisplay(GUILevelRewards.getUnlocked(player),
                    GUILevelRewards.getTotalAwards()));
            lore.add(" ");
            lore.add("§eClick to view rewards!");
            return ItemStackCreator.getStack("§aLeveling Rewards", Material.CHEST, 1, lore);
        }, (click, c) -> c.player().openView(new GUILevelRewards()));

        // Your SkyBlock Level Ranking
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockLevelRequirement level = player.getSkyBlockExperience().getLevel();
            int completedChallenges = player.getSkyBlockExperience().getCompletedExperienceCauses().size();
            int totalChallenges = SkyBlockLevelCause.getAmountOfCauses();

            return ItemStackCreator.getStack("§aYour SkyBlock Level Ranking",
                    Material.PAINTING, 1,
                    "§8Classic Mode",
                    " ",
                    "§7Your level: " + level.getColor() + level,
                    "§7You have: §b" + Math.round(player.getSkyBlockExperience().getTotalXP()) + " XP",
                    " ",
                    "§7You have completed §3" + (new java.text.DecimalFormat("##.##").format((double) completedChallenges / totalChallenges * 100)) + "% §7of the total",
                    "§7SkyBlock XP Tasks.");
        });

        // SkyBlock Guide
        layout.slot(25, (s, c) -> ItemStackCreator.getStack("§aSkyBlock Guide", Material.FILLED_MAP, 1,
                        "§7Your §6SkyBlock Guide §7tracks the",
                        "§7progress you have made through",
                        "§7SkyBlock.",
                        " ",
                        "§7Complete tasks within your current",
                        "§7game stage to increase your",
                        "§bSkyBlock Level §7and become a §dMaster",
                        "§7of SkyBlock!",
                        " ",
                        "§eClick to view!"),
                (click, c) -> c.player().openView(new GUILevelsGuide(LevelsGuide.STARTER)));

        // Prefix Emblems
        layout.slot(43, (s, c) -> ItemStackCreator.getStack("§aPrefix Emblems", Material.NAME_TAG, 1,
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
                        "§eClick to view!"),
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
                    lore.add("§8Your Level");
                    lore.add(" ");
                    material = level.isMilestone() ? Material.LIME_STAINED_GLASS : Material.LIME_STAINED_GLASS_PANE;
                } else if (unlockedLevel + 1 == level.asInt()) {
                    lore.add("§8Next Level");
                    lore.add(" ");
                    material = level.isMilestone() ? Material.YELLOW_STAINED_GLASS : Material.YELLOW_STAINED_GLASS_PANE;
                }

                lore.add("§7Rewards:");
                level.getUnlocks().forEach(unlock -> lore.addAll(unlock.getDisplay(p, level.asInt())));
                lore.add(" ");
                if (unlockedLevel == level.asInt()) {
                    lore.add("§a§lUNLOCKED");
                    lore.add(" ");
                }
                lore.add("§eClick to view rewards!");

                return ItemStackCreator.getStack("§7Level " + level.asInt(), material, 1, lore);
            }, (click, c) -> c.player().openView(new GUISkyBlockLevel(level)));
        }

        // Next Milestone Level
        SkyBlockLevelRequirement currentMilestone = player.getSkyBlockExperience().getLevel().getNextMilestoneLevel();
        if (currentMilestone != null) {
            layout.slot(30, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                List<String> lore = new ArrayList<>();
                lore.add("§8Next Milestone Level");
                lore.add(" ");
                lore.add("§7Rewards:");
                currentMilestone.getUnlocks().forEach(unlock -> lore.addAll(unlock.getDisplay(p, currentMilestone.asInt())));
                lore.add(" ");
                lore.add("§7XP Left to Gain: §b" + (currentMilestone.getCumulativeExperience() - p.getSkyBlockExperience().getTotalXP())
                        + " XP §8(" + (int) (p.getSkyBlockExperience().getTotalXP() / currentMilestone.getCumulativeExperience() * 100) + "%)");
                lore.add(" ");
                lore.add("§eClick to view rewards!");

                return ItemStackCreator.getStack("§7Level " + currentMilestone.asInt(), Material.PURPLE_STAINED_GLASS_PANE, 1, lore);
            }, (click, c) -> c.player().openView(new GUISkyBlockLevel(currentMilestone)));
        }
    }
}
