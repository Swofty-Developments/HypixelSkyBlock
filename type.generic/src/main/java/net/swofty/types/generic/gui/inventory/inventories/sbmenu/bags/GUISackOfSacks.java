package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import lombok.Setter;
import net.minestom.server.event.inventory.InventoryClickEvent;
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
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.SackComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GUISackOfSacks extends SkyBlockInventoryGUI {
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
        super("Sack of Sacks", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(40));
        set(GUIClickableItem.getGoBackItem(39, new GUIYourBags()));

        int totalSlots = getTotalSlots(e.player());
        int slotsPerPage = 45;
        slotToSaveUpTo = totalSlots;

        for (int i = 0; i < totalSlots; i++) {
            int slot = i;
            SkyBlockItem item = e.player().getSackOfSacks().getInSlot(i);

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (e.getClickType() == ClickType.RIGHT_CLICK) {
                        e.setCancelled(true);
                        SkyBlockItem skyBlockItem = new SkyBlockItem(e.getClickedItem());
                        if (skyBlockItem.isNA() || skyBlockItem.isAir()) return;
                        new GUISack(skyBlockItem.getAttributeHandler().getPotentialType(), false).open(player);
                    }
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (item == null) {
                        return ItemStack.builder(Material.AIR);
                    } else {
                        return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
                    }
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public void runPost(InventoryClickEvent e, SkyBlockPlayer player) {
                    save(player);
                }
            });
        }

        for (int i = totalSlots; i < slotsPerPage; i++) {
            int slotIndex = i;
            CustomCollectionAward nextUpgrade = getUpgradeNeededForSlotIndex(slotIndex);
            if (nextUpgrade != null) {
                set(new GUIItem(i) {
                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§cLocked", Material.RED_STAINED_GLASS_PANE,
                                1,
                                "§7You need to unlock the",
                                "§a" + nextUpgrade.getDisplay() + " §7upgrade",
                                "§7to use this slot.");
                    }
                });
            }
        }

        set(new GUIClickableItem(38) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                int slot = 0;
                for (ItemStack itemStack : player.getInventory().getItemStacks()) {
                    SkyBlockItem item = new SkyBlockItem(itemStack);
                    ItemType type = item.getAttributeHandler().getPotentialType();
                    if (player.canInsertItemIntoSacks(type)) {
                        player.getSackItems().increase(type, item.getAmount());
                        player.getInventory().setItemStack(slot, ItemStack.AIR);
                    }
                    slot++;
                }
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aInsert inventory", Material.CHEST, 1,
                        "§7Inserts your inventory items into",
                        "§7your sacks.",
                        "",
                        "§eClick to put items in!");
            }
        });

        updateItemStacks(e.inventory(), e.player());
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        save(getPlayer());
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        SkyBlockItem cursorItem = new SkyBlockItem(e.getCursorItem());
        SkyBlockItem clickedItem = new SkyBlockItem(e.getClickedItem());

        if (isItemAllowed(cursorItem) && isItemAllowed(clickedItem)) {
            save(getPlayer());
            return;
        }

        e.setCancelled(true);
        getPlayer().sendMessage("§cYou cannot put this item in the Sack of Sacks!");
        save(getPlayer());
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
        DatapointSackOfSacks.PlayerSackOfSacks sackOfSacks = player.getDataHandler().get(DataHandler.Data.SACK_OF_SACKS, DatapointSackOfSacks.class).getValue();
        for (int i = 0; i < this.slotToSaveUpTo; i++) {
            int slot = i + ((page - 1) * 45);
            SkyBlockItem item = new SkyBlockItem(getInventory().getItemStack(i));
            if (item.isNA() || item.getMaterial() == Material.AIR) {
                sackOfSacks.removeFromSlot(slot);
            } else {
                sackOfSacks.setInSlot(slot, item);
            }
        }
    }

    public boolean isItemAllowed(SkyBlockItem item) {
        if (item.isNA()) return true;
        if (item.getMaterial().equals(Material.AIR)) return true;

        return item.hasComponent(SackComponent.class);
    }
}