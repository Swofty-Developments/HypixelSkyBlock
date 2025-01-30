package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointSackOfSacks;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.SackComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GUISackOfSacks extends SkyBlockAbstractInventory {
    private static final String STATE_SLOT_UNLOCKED = "slot_unlocked_";

    private static final SortedMap<CustomCollectionAward, Integer> SLOTS_PER_UPGRADE = new TreeMap<>(Map.of(
            CustomCollectionAward.SACK_OF_SACKS, 3,
            CustomCollectionAward.SACK_OF_SACKS_UPGRADE_1, 6,
            CustomCollectionAward.SACK_OF_SACKS_UPGRADE_2, 9,
            CustomCollectionAward.SACK_OF_SACKS_UPGRADE_3, 12,
            CustomCollectionAward.SACK_OF_SACKS_UPGRADE_4, 15,
            CustomCollectionAward.SACK_OF_SACKS_UPGRADE_5, 18
    ));

    @Setter
    private int page = 1;
    private int slotToSaveUpTo;

    public GUISackOfSacks() {
        super(InventoryType.CHEST_5_ROW);
        doAction(new SetTitleAction(Component.text("Sack of Sacks")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(40)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(39)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Your Bags").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIYourBags());
                    return true;
                })
                .build());

        int totalSlots = getTotalSlots(player);
        int slotsPerPage = 45;
        slotToSaveUpTo = totalSlots;

        // Set up slot states
        for (int i = 0; i < totalSlots; i++) {
            doAction(new AddStateAction(STATE_SLOT_UNLOCKED + i));
        }

        // Setup sack slots
        for (int i = 0; i < slotsPerPage; i++) {
            final int slot = i;
            SkyBlockItem item = player.getSackOfSacks().getInSlot(i);

            // Unlocked slot
            attachItem(GUIItem.builder(slot)
                    .item(() -> item == null ? ItemStack.AIR :
                            PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build())
                    .requireState(STATE_SLOT_UNLOCKED + slot)
                    .onClick((ctx, clickedItem) -> {
                        if (ctx.clickType() == ClickType.RIGHT_CLICK) {
                            SkyBlockItem sackItem = new SkyBlockItem(clickedItem);
                            if (sackItem.isNA() || sackItem.isAir()) return false;
                            ctx.player().openInventory(new GUISack(sackItem.getAttributeHandler().getPotentialType(), false));
                            return false;
                        }
                        save(ctx.player());
                        return true;
                    })
                    .build());

            // Locked slot
            if (slot >= totalSlots) {
                CustomCollectionAward nextUpgrade = getUpgradeNeededForSlotIndex(slot);
                if (nextUpgrade != null) {
                    attachItem(GUIItem.builder(slot)
                            .item(ItemStackCreator.getStack("§cLocked", Material.RED_STAINED_GLASS_PANE,
                                    1,
                                    "§7You need to unlock the",
                                    "§a" + nextUpgrade.getDisplay() + " §7upgrade",
                                    "§7to use this slot.").build())
                            .build());
                }
            }
        }

        // Insert inventory button
        attachItem(GUIItem.builder(38)
                .item(ItemStackCreator.getStack("§aInsert inventory", Material.CHEST, 1,
                        "§7Inserts your inventory items into",
                        "§7your sacks.",
                        "",
                        "§eClick to put items in!").build())
                .onClick((ctx, item) -> {
                    int slot = 0;
                    for (ItemStack itemStack : ctx.player().getInventory().getItemStacks()) {
                        SkyBlockItem sackItem = new SkyBlockItem(itemStack);
                        ItemType type = sackItem.getAttributeHandler().getPotentialType();
                        if (ctx.player().canInsertItemIntoSacks(type)) {
                            ctx.player().getSackItems().increase(type, sackItem.getAmount());
                            ctx.player().getInventory().setItemStack(slot, ItemStack.AIR);
                        }
                        slot++;
                    }
                    return true;
                })
                .build());
    }

    private int getTotalSlots(SkyBlockPlayer player) {
        int totalSlots = 0;
        for (CustomCollectionAward entry : SLOTS_PER_UPGRADE.keySet()) {
            if (player.hasCustomCollectionAward(entry)) {
                totalSlots = Math.max(totalSlots, SLOTS_PER_UPGRADE.get(entry));
            }
        }
        return totalSlots;
    }

    private CustomCollectionAward getUpgradeNeededForSlotIndex(int slotIndex) {
        for (CustomCollectionAward entry : SLOTS_PER_UPGRADE.keySet()) {
            if (slotIndex < SLOTS_PER_UPGRADE.get(entry)) {
                return entry;
            }
        }
        return null;
    }

    private void save(SkyBlockPlayer player) {
        DatapointSackOfSacks.PlayerSackOfSacks sackOfSacks = player.getDataHandler()
                .get(DataHandler.Data.SACK_OF_SACKS, DatapointSackOfSacks.class).getValue();

        for (int i = 0; i < this.slotToSaveUpTo; i++) {
            int slot = i + ((page - 1) * 45);
            SkyBlockItem item = new SkyBlockItem(getItemStack(i));
            if (item.isNA() || item.getMaterial() == Material.AIR) {
                sackOfSacks.removeFromSlot(slot);
            } else {
                sackOfSacks.setInSlot(slot, item);
            }
        }
    }

    private boolean isItemAllowed(SkyBlockItem item) {
        if (item.isNA()) return true;
        if (item.getMaterial().equals(Material.AIR)) return true;
        return item.hasComponent(SackComponent.class);
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        save((SkyBlockPlayer) event.getPlayer());
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        SkyBlockItem cursorItem = new SkyBlockItem(event.getCursorItem());
        SkyBlockItem clickedItem = new SkyBlockItem(event.getClickedItem());

        if (isItemAllowed(cursorItem) && isItemAllowed(clickedItem)) {
            save((SkyBlockPlayer) event.getPlayer());
            return;
        }

        event.setCancelled(true);
        ((SkyBlockPlayer) event.getPlayer()).sendMessage("§cYou cannot put this item in the Sack of Sacks!");
        save((SkyBlockPlayer) event.getPlayer());
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        save(player);
    }
}