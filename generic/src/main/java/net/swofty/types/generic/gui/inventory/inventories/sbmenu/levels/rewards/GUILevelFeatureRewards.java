package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.levels.CustomLevelAward;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GUILevelFeatureRewards extends SkyBlockInventoryGUI {
    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25,
                        31
    };

    public GUILevelFeatureRewards() {
        super("Feature Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUILevelRewards()));

        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = getPlayer().getSkyBlockExperience();

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
                    nextAward.getValue().forEach(award -> {
                        lore.add("§7" + award.getDisplay());
                    });
                    lore.add("§8at Level " + nextAward.getKey());
                }

                lore.add(" ");
                lore.addAll(GUILevelRewards.getAsDisplay(CustomLevelAward.getFromLevel(experience.getLevel().asInt()).size(),
                        CustomLevelAward.getTotalLevelAwards()));

                return ItemStackCreator.getStack("§aFeature Rewards",
                        Material.NETHER_STAR, 1, lore);
            }
        });

        for (Map.Entry<CustomLevelAward, Integer> entry : CustomLevelAward.getAwards().entrySet()) {
            CustomLevelAward award = entry.getKey();
            Integer level = entry.getValue();
            int slot = SLOTS[award.ordinal()];
            boolean unlocked = experience.getLevel().asInt() >= level;

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    ItemStack.Builder item = award.getItem();
                    List<String> lore = new ArrayList<>(Arrays.asList(
                            "§8Level " + level,
                            " "
                    ));

                    if (unlocked) {
                        lore.add("§aYou have unlocked this reward!");
                    } else {
                        lore.add("§7Levels left to Unlock: §3" + (level - experience.getLevel().asInt()));
                    }

                    return item.lore(lore.stream().map(line -> {
                        return Component.text(line).decoration(TextDecoration.ITALIC, false);
                    }).toList()).displayName(Component.text(award.getDisplay()).decoration(TextDecoration.ITALIC, false));
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
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
