package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.skills;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkills;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.GUIBestiary;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.skill.SkillCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUISkillCategory extends StatelessView {
    private static final int[] DISPLAY_SLOTS = {
            9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53
    };

    private final SkillCategories category;
    private final int page;

    public GUISkillCategory(SkillCategories category, int page) {
        this.category = category;
        this.page = page;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(category.toString() + " Skill", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        DatapointSkills.PlayerSkills skills = player.getSkills();
        int level = skills.getCurrentLevel(category);
        Integer nextLevel = skills.getNextLevel(category);

        // Bestiary button for combat
        if (category == SkillCategories.COMBAT && player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_BRAMASS_BEASTSLAYER)) {
            layout.slot(39, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                ArrayList<String> lore = new ArrayList<>();
                p.getBestiaryData().getTotalDisplay(lore);
                lore.add("");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack("§3Bestiary", Material.WRITTEN_BOOK, 1, lore);
            }, (click, c) -> c.push(new GUIBestiary()));
        }

        // Skill info
        layout.slot(0, (s, c) -> {
            SkyBlockPlayer p = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>(category.asCategory().getDescription());
            lore.add(" ");

            Integer next = p.getSkills().getNextLevel(category);
            if (next == null) {
                lore.add("§cMAX LEVEL REACHED");
            } else {
                p.getSkills().getDisplay(lore, category, category.asCategory().getReward(next).requirement(),
                        "§7Progress to Level " + StringUtility.getAsRomanNumeral(next) + ": ");
            }

            lore.add(" ");
            lore.add("§8Increase your " + category + " Level to");
            lore.add("§8unlock Perks, statistic bonuses, and");
            lore.add("§8more!");

            return ItemStackCreator.getStack("§a" + category + " Skill",
                    category.asCategory().getDisplayIcon(), 1, lore);
        });

        List<SkillCategory.SkillReward> rewards = List.of(category.asCategory().getRewards());

        // Next page button
        if (rewards.size() > (page + 1) * DISPLAY_SLOTS.length) {
            layout.slot(50, (s, c) -> ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1,
                            "§7Click to view the next page of rewards."),
                    (click, c) -> c.replace(new GUISkillCategory(category, page + 1)));
        }

        // Previous page button
        if (page > 0) {
            layout.slot(48, (s, c) -> ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1,
                            "§7Click to view the previous page of rewards."),
                    (click, c) -> c.replace(new GUISkillCategory(category, page - 1)));
        }

        // Rewards
        int startIndex = page * DISPLAY_SLOTS.length;
        int endIndex = Math.min(rewards.size(), (page + 1) * DISPLAY_SLOTS.length);
        List<SkillCategory.SkillReward> pageRewards = rewards.subList(startIndex, endIndex);

        for (int i = 0; i < pageRewards.size() && i < DISPLAY_SLOTS.length; i++) {
            SkillCategory.SkillReward reward = pageRewards.get(i);
            int slot = DISPLAY_SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                int currentLevel = p.getSkills().getCurrentLevel(category);
                List<String> lore = new ArrayList<>();
                reward.getDisplay(lore);

                Material icon = Material.RED_STAINED_GLASS_PANE;
                String colour = "§c";

                if (currentLevel >= reward.level()) {
                    icon = Material.LIME_STAINED_GLASS_PANE;
                    colour = "§a";
                    lore.add(" ");
                    lore.add("§a§lUNLOCKED");
                } else if ((currentLevel + 1) == reward.level()) {
                    icon = Material.YELLOW_STAINED_GLASS_PANE;
                    colour = "§e";
                    lore.add(" ");
                    p.getSkills().getDisplay(lore, category, reward.requirement(), "§7Progress: ");
                }

                return ItemStackCreator.getStack(colour + category + " Level " + StringUtility.getAsRomanNumeral(reward.level()),
                        icon, 1, lore);
            });
        }
    }
}
