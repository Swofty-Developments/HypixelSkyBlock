package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBackpacks;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Backpack;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;

public class GUIStorage extends SkyBlockInventoryGUI {
    public GUIStorage() {
        super("Storage", InventoryType.CHEST_6_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aEnder Chest", Material.ENDER_CHEST, 1,
                        "§7Store global items you can",
                        "§7access anywhere in your ender",
                        "§7chest.");
            }
        });
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aBackpacks", Material.CHEST, 1,
                        "§7Place backpack items in these slots",
                        "§7to use them as additional storage",
                        "§7that can be accessed anywhere.");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        DatapointStorage.PlayerStorage storage = getPlayer().getDataHandler().get(
                DataHandler.Data.STORAGE, DatapointStorage.class
        ).getValue();

        // Initialize empty storages
        if (!storage.hasPage(1)) {
            storage.addPage(1);
            storage.addPage(2);
        }

        for (int ender_slot = 9; ender_slot < 18; ender_slot++) {
            int page = ender_slot - 8;

            set(new GUIClickableItem(ender_slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (!storage.hasPage(page)) return;

                    if (e.getClickType() == ClickType.RIGHT_CLICK) {
                        new GUIStorageIconSelection(page, GUIStorage.this).open(player);
                    } else {
                        new GUIStoragePage(page).open(player);
                    }
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (!storage.hasPage(page))
                        return ItemStackCreator.getStack("§cLocked Page", Material.RED_STAINED_GLASS_PANE, 1,
                                "§7Unlock more Ender Chest pages in",
                                "§7the community shop!");

                    Material material = storage.getPage(page).display;

                    return ItemStackCreator.getStack("§aEnder Chest Page " + (page), material, page,
                            " ",
                            "§eLeft-click to open!",
                            "§eRight-click to change icon!");
                }
            });
        }

        DatapointBackpacks.PlayerBackpacks backpacks = getPlayer().getDataHandler().get(
                DataHandler.Data.BACKPACKS, DatapointBackpacks.class
        ).getValue();

        Map<Integer, SkyBlockItem> backpackItems = backpacks.getBackpacks();

        for (int backpack_slot = 27; backpack_slot <= 44; backpack_slot++) {
            int slot = backpack_slot - 26;

            if (backpacks.getUnlockedSlots() < slot) {
                set(new GUIItem(backpack_slot) {
                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§cLocked Backpack Slot " + slot,
                                Material.GRAY_DYE, 1,
                                "§7Talk to Tia the Fairy to unlock more",
                                "§7Backpack Slots!");
                    }
                });
                continue;
            }

            if (!backpackItems.containsKey(slot)) {
                set(new GUIClickableItem(backpack_slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        SkyBlockItem item = new SkyBlockItem(e.getCursorItem());

                        if (item.isNA()) return;
                        if (!(item.getGenericInstance() instanceof Backpack)) return;

                        e.setClickedItem(ItemStack.AIR);
                        e.setCancelled(false);
                    }

                    @Override
                    public void runPost(InventoryClickEvent e2, SkyBlockPlayer player) {
                        SkyBlockItem item = new SkyBlockItem(e2.getCursorItem());

                        if (item.isNA()) return;
                        if (!(item.getGenericInstance() instanceof Backpack)) return;

                        backpackItems.put(slot, item);
                        player.getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class).setValue(
                                new DatapointBackpacks.PlayerBackpacks(backpackItems, backpacks.getUnlockedSlots())
                        );

                        player.sendMessage("§ePlacing backpack in slot " + slot + "...");
                        player.sendMessage("§aSuccess!");

                        onOpen(e);
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§eEmpty Backpack Slot " + slot,
                                Material.BROWN_STAINED_GLASS_PANE, slot,
                                " ",
                                "§eLeft-click a backpack item on this",
                                "§eslot to place it!");
                    }
                });
                continue;
            }

            SkyBlockItem item = backpackItems.get(slot);

            set(new GUIClickableItem(backpack_slot) {
                @Override
                public void run(InventoryPreClickEvent e2, SkyBlockPlayer player) {
                    if (e2.getClickType() == ClickType.RIGHT_CLICK) {
                        if (!item.getAttributeHandler().getBackpackData().items().isEmpty() && !item.getAttributeHandler().getBackpackData().items().stream().allMatch(SkyBlockItem::isNA)) {
                            player.sendMessage("§cThe backpack in slot " + slot + " is not empty! Please empty it before removing it.");
                            return;
                        }

                        player.sendMessage("§aRemoved backpack from slot " + slot + "!");
                        e2.setClickedItem(PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build());
                        e2.setCancelled(false);

                        backpackItems.remove(slot);
                        return;
                    }

                    new GUIStorageBackpackPage(slot, item).open(player);
                }

                @Override
                public void runPost(InventoryClickEvent e2, SkyBlockPlayer player) {
                    onOpen(e);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§6Backpack Slot " + slot,
                            ((SkullHead) item.getGenericInstance()).getSkullTexture(player, item), slot,
                            item.getAttributeHandler().getRarity().getColor() +
                                    item.getAttributeHandler().getItemTypeAsType().getDisplayName(item),
                            "§7This backpack has §a" + (((Backpack) item.getGenericInstance()).getRows() * 9) + " §7slots.",
                            " ",
                            "§eLeft-click to open!",
                            "§eRight-click to remove!");
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
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        ItemStack stack = e.getClickedItem();
        SkyBlockItem item = new SkyBlockItem(stack);

        if (item.isNA()) return;

        if (item.getGenericInstance() == null)
            e.setCancelled(true);

        if (!(item.getGenericInstance() instanceof Backpack))
            e.setCancelled(true);
    }
}
