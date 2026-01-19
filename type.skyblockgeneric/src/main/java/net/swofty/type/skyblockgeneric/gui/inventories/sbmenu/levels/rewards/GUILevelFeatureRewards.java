package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.type.skyblockgeneric.levels.CustomLevelAward;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GUILevelFeatureRewards extends StatelessView {
    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25, 31
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Feature Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // Title item
        layout.slot(4, (s, c) -> {
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
            lore.addAll(GUILevelRewards.getAsDisplay(CustomLevelAward.getFromLevel(experience.getLevel().asInt()).size(),
                    CustomLevelAward.getTotalLevelAwards()));

            return ItemStackCreator.getStack("§aFeature Rewards", Material.NETHER_STAR, 1, lore);
        });

        // Award items
        for (Map.Entry<CustomLevelAward, Integer> entry : CustomLevelAward.getAwards().entrySet()) {
            CustomLevelAward award = entry.getKey();
            Integer level = entry.getValue();
            int slot = SLOTS[award.ordinal()];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                boolean unlocked = player.getSkyBlockExperience().getLevel().asInt() >= level;

                ItemStack.Builder item = award.getItem();
                List<String> lore = new ArrayList<>(Arrays.asList("§8Level " + level, " "));

                if (unlocked) {
                    lore.add("§aYou have unlocked this reward!");
                } else {
                    lore.add("§7Levels left to Unlock: §3" + (level - player.getSkyBlockExperience().getLevel().asInt()));
                }

                return ItemStackCreator.updateLore(item, lore).set(
                        DataComponents.CUSTOM_NAME,
                        Component.text(award.getDisplay()).decoration(TextDecoration.ITALIC, false)
                );
            });
        }
    }
}
