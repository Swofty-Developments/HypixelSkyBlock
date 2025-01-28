package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.levels.CustomLevelAward;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GUILevelFeatureRewards extends SkyBlockAbstractInventory {
    private static final String STATE_FEATURE_UNLOCKED = "feature_unlocked";
    private static final String STATE_FEATURE_LOCKED = "feature_locked";

    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25,
            31
    };

    public GUILevelFeatureRewards() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Feature Rewards")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + new GUILevelRewards().getTitle()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUILevelRewards());
                    return true;
                })
                .build());

        setupInfoItem(player);
        setupFeatureItems(player);
    }

    private void setupInfoItem(SkyBlockPlayer player) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();

        attachItem(GUIItem.builder(4)
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
                    lore.addAll(GUILevelRewards.getAsDisplay(CustomLevelAward.getFromLevel(experience.getLevel().asInt()).size(),
                            CustomLevelAward.getTotalLevelAwards()));

                    return ItemStackCreator.getStack("§aFeature Rewards",
                            Material.NETHER_STAR, 1, lore).build();
                })
                .build());
    }

    private void setupFeatureItems(SkyBlockPlayer player) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();

        for (Map.Entry<CustomLevelAward, Integer> entry : CustomLevelAward.getAwards().entrySet()) {
            CustomLevelAward award = entry.getKey();
            Integer level = entry.getValue();
            int slot = SLOTS[award.ordinal()];
            boolean unlocked = experience.getLevel().asInt() >= level;

            if (unlocked) {
                doAction(new AddStateAction(STATE_FEATURE_UNLOCKED + "_" + award.ordinal()));
            } else {
                doAction(new AddStateAction(STATE_FEATURE_LOCKED + "_" + award.ordinal()));
            }

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
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

                        return ItemStackCreator.updateLore(item, lore)
                                .set(ItemComponent.CUSTOM_NAME,
                                        Component.text(award.getDisplay())
                                                .decoration(TextDecoration.ITALIC, false))
                                .build();
                    })
                    .requireState(unlocked ? STATE_FEATURE_UNLOCKED + "_" + award.ordinal() :
                            STATE_FEATURE_LOCKED + "_" + award.ordinal())
                    .build());
        }
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}