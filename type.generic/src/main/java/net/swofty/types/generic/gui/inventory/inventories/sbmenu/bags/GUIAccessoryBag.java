package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointAccessoryBag;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.AccessoryComponent;
import net.swofty.types.generic.item.components.TieredTalismanComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GUIAccessoryBag extends SkyBlockAbstractInventory {
    private static final String STATE_SLOT_UNLOCKED = "slot_unlocked_";
    private static final String STATE_SLOT_LOCKED = "slot_locked_";
    private static final String STATE_HAS_PREV_PAGE = "has_prev_page";
    private static final String STATE_HAS_NEXT_PAGE = "has_next_page";

    private static final SortedMap<CustomCollectionAward, Integer> SLOTS_PER_UPGRADE = new TreeMap<>(Map.of(
            CustomCollectionAward.ACCESSORY_BAG, 3,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_1, 9,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_2, 15,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_3, 21,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_4, 27,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_5, 33,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_6, 39,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_7, 45,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_8, 51,
            CustomCollectionAward.ACCESSORY_BAG_UPGRADE_9, 57
    ));

    @Setter
    private int page = 1;
    private int slotToSaveUpTo;

    public GUIAccessoryBag() {
        super(InventoryType.CHEST_6_ROW);
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        int totalSlots = getTotalSlots(player);
        int slotsPerPage = 45;
        int totalPages = (int) Math.ceil((double) totalSlots / slotsPerPage);

        doAction(new SetTitleAction(Component.text("Accessory Bag (" + page + "/" + totalPages + ")")));

        setupNavigationButtons();
        setupBagSlots(player, totalSlots, slotsPerPage);
        setupPaginationStates(totalPages);
    }

    private void setupNavigationButtons() {
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
                        "§7To Your Bags").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIYourBags());
                    return true;
                })
                .build());

        // Previous page button
        attachItem(GUIItem.builder(45)
                .item(ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1).build())
                .requireState(STATE_HAS_PREV_PAGE)
                .onClick((ctx, item) -> {
                    GUIAccessoryBag gui = new GUIAccessoryBag();
                    gui.setPage(page - 1);
                    ctx.player().openInventory(gui);
                    return true;
                })
                .build());

        // Next page button
        attachItem(GUIItem.builder(53)
                .item(ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1).build())
                .requireState(STATE_HAS_NEXT_PAGE)
                .onClick((ctx, item) -> {
                    GUIAccessoryBag gui = new GUIAccessoryBag();
                    gui.setPage(page + 1);
                    ctx.player().openInventory(gui);
                    return true;
                })
                .build());
    }

    private void setupBagSlots(SkyBlockPlayer player, int totalSlots, int slotsPerPage) {
        int startIndex = (page - 1) * slotsPerPage;
        int endSlot = Math.min(totalSlots - startIndex, slotsPerPage);
        this.slotToSaveUpTo = endSlot;

        // Setup unlocked slots
        for (int i = 0; i < endSlot; i++) {
            final int slot = i;
            final int actualSlot = i + startIndex;
            SkyBlockItem item = player.getAccessoryBag().getInSlot(actualSlot);

            doAction(new AddStateAction(STATE_SLOT_UNLOCKED + slot));

            attachItem(GUIItem.builder(slot)
                    .item(() -> item == null ? ItemStack.AIR :
                            PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build())
                    .requireState(STATE_SLOT_UNLOCKED + slot)
                    .onClick((ctx, clickedItem) -> {
                        save(ctx.player());
                        return true;
                    })
                    .onPostClick(this::save)
                    .build());
        }

        // Setup locked slots
        for (int i = endSlot; i < slotsPerPage; i++) {
            final int slotIndex = i + startIndex;
            CustomCollectionAward nextUpgrade = getUpgradeNeededForSlotIndex(slotIndex);
            if (nextUpgrade != null) {
                doAction(new AddStateAction(STATE_SLOT_LOCKED + i));

                attachItem(GUIItem.builder(i)
                        .item(ItemStackCreator.getStack("§cLocked", Material.RED_STAINED_GLASS_PANE,
                                1,
                                "§7You need to unlock the",
                                "§a" + nextUpgrade.getDisplay() + " §7upgrade",
                                "§7to use this slot.").build())
                        .requireState(STATE_SLOT_LOCKED + i)
                        .build());
            }
        }
    }

    private void setupPaginationStates(int totalPages) {
        if (page > 1) {
            doAction(new AddStateAction(STATE_HAS_PREV_PAGE));
        }
        if (page < totalPages) {
            doAction(new AddStateAction(STATE_HAS_NEXT_PAGE));
        }
    }

    private int getTotalSlots(SkyBlockPlayer player) {
        return SLOTS_PER_UPGRADE.entrySet().stream()
                .filter(entry -> player.hasCustomCollectionAward(entry.getKey()))
                .map(Map.Entry::getValue)
                .max(Integer::compareTo)
                .orElse(0);
    }

    private CustomCollectionAward getUpgradeNeededForSlotIndex(int slotIndex) {
        return SLOTS_PER_UPGRADE.entrySet().stream()
                .filter(entry -> slotIndex < entry.getValue())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private void save(SkyBlockPlayer player) {
        DatapointAccessoryBag.PlayerAccessoryBag accessoryBag = player.getAccessoryBag();
        for (int i = 0; i < this.slotToSaveUpTo; i++) {
            int slot = i + ((page - 1) * 45);
            SkyBlockItem item = new SkyBlockItem(getItemStack(i));

            if (item.isNA() || item.getMaterial() == Material.AIR) {
                accessoryBag.removeFromSlot(slot);
            } else {
                accessoryBag.setInSlot(slot, item);
            }
        }

        player.getDataHandler().get(DataHandler.Data.ACCESSORY_BAG, DatapointAccessoryBag.class)
                .setValue(accessoryBag);
    }

    private boolean isItemAllowed(SkyBlockItem item) {
        if (item.isNA() || item.getMaterial().equals(Material.AIR)) return true;

        if (item.hasComponent(AccessoryComponent.class) || item.hasComponent(TieredTalismanComponent.class)) {
            DatapointAccessoryBag.PlayerAccessoryBag accessoryBag = owner.getAccessoryBag();
            accessoryBag.addDiscoveredAccessory(item.getAttributeHandler().getPotentialType());

            owner.getSkyBlockExperience().addExperience(
                    SkyBlockLevelCause.getAccessoryCause(item.getAttributeHandler().getPotentialType())
            );
            return true;
        }
        return false;
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
        ((SkyBlockPlayer) event.getPlayer()).sendMessage("§cYou cannot put this item in the Accessory Bag!");
        save((SkyBlockPlayer) event.getPlayer());
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        save(player);
    }
}