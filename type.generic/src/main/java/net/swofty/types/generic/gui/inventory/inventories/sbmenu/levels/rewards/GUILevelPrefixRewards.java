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
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUILevelPrefixRewards extends SkyBlockAbstractInventory {
    private static final String STATE_PREFIX_UNLOCKED = "prefix_unlocked";
    private static final String STATE_PREFIX_LOCKED = "prefix_locked";

    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33
    };

    public GUILevelPrefixRewards() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Prefix Rewards")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
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
        setupPrefixItems(player);
    }

    private void setupInfoItem(SkyBlockPlayer player) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();

        attachItem(GUIItem.builder(4)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7New colors for your level prefix");
                    lore.add("§7shown in TAB and in chat!");
                    lore.add(" ");
                    lore.add("§7Next Reward:");

                    Map.Entry<SkyBlockLevelRequirement, String> nextPrefix = experience.getLevel().getNextPrefixChange();
                    if (nextPrefix == null) {
                        lore.add("§cNo more rewards!");
                    } else {
                        lore.add(nextPrefix.getValue() + nextPrefix.getKey().getPrefixDisplay());
                        lore.add("§8at Level " + nextPrefix.getKey().asInt());
                    }
                    lore.add(" ");
                    lore.addAll(GUILevelRewards.getAsDisplay(
                            player.getSkyBlockExperience().getLevel().getPreviousPrefixChanges().size(),
                            SkyBlockLevelRequirement.getAllPrefixChanges().size()
                    ));

                    return ItemStackCreator.getStack("§aPrefix Color Rewards",
                            Material.GRAY_DYE, 1, lore).build();
                })
                .build());
    }

    private void setupPrefixItems(SkyBlockPlayer player) {
        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();

        int index = 0;
        for (Map.Entry<SkyBlockLevelRequirement, String> entry : SkyBlockLevelRequirement.getAllPrefixChanges().entrySet()) {
            SkyBlockLevelRequirement level = entry.getKey();
            int slot = SLOTS[index];
            boolean unlocked = experience.getLevel().asInt() >= level.asInt();

            if (unlocked) {
                doAction(new AddStateAction(STATE_PREFIX_UNLOCKED + "_" + index));
            } else {
                doAction(new AddStateAction(STATE_PREFIX_LOCKED + "_" + index));
            }

            attachItem(GUIItem.builder(slot)
                    .item(() -> ItemStackCreator.getStack(level.getPrefix() + level.getPrefixDisplay(),
                                    level.getPrefixItem(), 1,
                                    "§8Level " + level.asInt(),
                                    " ",
                                    "§7Preview: " + player.getFullDisplayName(level.getPrefix()),
                                    " ",
                                    (unlocked ? "§aYou have unlocked this reward!" : "§7Levels left to unlock: §3" + (level.asInt() - experience.getLevel().asInt())))
                            .build())
                    .requireState(unlocked ? STATE_PREFIX_UNLOCKED + "_" + index : STATE_PREFIX_LOCKED + "_" + index)
                    .build());

            index++;
        }
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