package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointAccessoryBag;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Accessory;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GUIAccessoryBag extends SkyBlockInventoryGUI {
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
        super("Accessory Bag", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUIYourBags()));

        int totalSlots = getTotalSlots(e.player());
        int slotsPerPage = 45;
        int totalPages = (int) Math.ceil((double) totalSlots / slotsPerPage);

        getInventory().setTitle(Component.text("Accessory Bag (" + page + "/" + totalPages + ")"));

        int startIndex = (page - 1) * slotsPerPage;
        int endSlot = Math.min(totalSlots - startIndex, slotsPerPage);
        this.slotToSaveUpTo = endSlot;

        for (int i = 0; i < endSlot; i++) {
            int slot = i;
            SkyBlockItem item = e.player().getAccessoryBag().getInSlot(i + startIndex);

            set(new GUIClickableItem(slot) {
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

                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                }
            });
        }

        for (int i = endSlot; i < slotsPerPage; i++) {
            int slotIndex = i + startIndex;
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

        if (page > 1) {
            set(new GUIClickableItem(45) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1);
                }

                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    GUIAccessoryBag gui = new GUIAccessoryBag();
                    gui.setPage(page - 1);
                    gui.open(player);
                }
            });
        }

        if (page < totalPages) {
            set(new GUIClickableItem(53) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1);
                }

                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    GUIAccessoryBag gui = new GUIAccessoryBag();
                    gui.setPage(page + 1);
                    gui.open(player);
                }
            });
        }

        updateItemStacks(e.inventory(), e.player());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
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
        getPlayer().sendMessage("§cYou cannot put this item in the Accessory Bag!");
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
        DatapointAccessoryBag.PlayerAccessoryBag accessoryBag = player.getAccessoryBag();
        for (int i = 0; i < this.slotToSaveUpTo; i++) {
            int slot = i + ((page - 1) * 45);
            SkyBlockItem item = new SkyBlockItem(getInventory().getItemStack(i));
            if (item.isNA() || item.getMaterial() == Material.AIR) {
                accessoryBag.removeFromSlot(slot);
            } else {
                accessoryBag.setInSlot(slot, item);
            }
        }

        player.getDataHandler().get(DataHandler.Data.ACCESSORY_BAG, DatapointAccessoryBag.class).setValue(
                accessoryBag
        );
    }

    public boolean isItemAllowed(SkyBlockItem item) {
        if (item.isNA()) return true;
        if (item.getMaterial().equals(Material.AIR)) return true;
        if (item.getGenericInstance() == null) return false;

        if (item.getGenericInstance() instanceof Accessory) {
            DatapointAccessoryBag.PlayerAccessoryBag accessoryBag = getPlayer().getAccessoryBag();
            accessoryBag.addDiscoveredAccessory(item.getAttributeHandler().getItemTypeAsType());

            getPlayer().getSkyBlockExperience().addExperience(
                    SkyBlockLevelCause.getAccessoryCause(item.getAttributeHandler().getItemTypeAsType())
            );

            return true;
        } else {
            return false;
        }
    }
}