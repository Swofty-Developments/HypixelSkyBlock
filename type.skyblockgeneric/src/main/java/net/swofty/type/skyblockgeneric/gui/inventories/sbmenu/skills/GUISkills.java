package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.skills;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.skill.SkillCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Locale;

public class GUISkills extends StatelessView {
    private static final int[] DISPLAY_SLOTS = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 32, 33, 34
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.skills.main.title", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        layout.slot(4, (s, c) -> {
            Locale l = c.player().getLocale();
            return ItemStackCreator.getStack(I18n.string("gui_sbmenu.skills.main.info", l), Material.DIAMOND_SWORD, 1,
                I18n.lore("gui_sbmenu.skills.main.info.lore", l));
        });

        SkillCategories[] allCategories = SkillCategories.values();
        for (int i = 0; i < DISPLAY_SLOTS.length && i < allCategories.length; i++) {
            SkillCategories category = allCategories[i];
            SkillCategory skillCategory = category.asCategory();
            int slot = DISPLAY_SLOTS[i];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                Locale l = player.getLocale();
                ArrayList<String> lore = new ArrayList<>();

                if (category == SkillCategories.CARPENTRY && !player.getMissionData().hasCompleted("give_wool_to_carpenter")) {
                    lore.addAll(I18n.lore("gui_sbmenu.skills.main.carpentry_locked.lore", l));
                } else {
                    lore.addAll(skillCategory.getDescription());
                    lore.add(" ");

                    Integer nextLevel = player.getSkills().getNextLevel(category);

                    if (nextLevel != null) {
                        player.getSkills().getDisplay(lore, category, skillCategory.getRewards()[nextLevel - 1].requirement(),
                                "§7Progress to Level " + StringUtility.getAsRomanNumeral(nextLevel) + ": ");
                        lore.add(" ");

                        SkillCategory.SkillReward[] rewards = skillCategory.getRewards();
                        SkillCategory.SkillReward reward = rewards[nextLevel - 1];

                        reward.getDisplay(lore);
                    } else {
                        lore.add(I18n.string("gui_sbmenu.skills.main.max_level", l));
                    }

                    lore.add(" ");
                    lore.add(I18n.string("gui_sbmenu.skills.main.click_to_view", l));
                }

                return ItemStackCreator.getStack(
                        "§a" + skillCategory.getName() + " " +
                                StringUtility.getAsRomanNumeral(player.getSkills().getCurrentLevel(category)),
                        skillCategory.getDisplayIcon(), 1, lore);
            }, (click, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                if (category == SkillCategories.CARPENTRY && !player.getMissionData().hasCompleted("give_wool_to_carpenter")) return;
                c.push(new GUISkillCategory(category, 0));
            });
        }
    }
}
