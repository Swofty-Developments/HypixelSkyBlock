package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBackpacks;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Backpack;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;
import net.swofty.types.generic.utility.StringUtility;

import java.util.concurrent.atomic.AtomicInteger;

public class GUIStorageBackpackPage extends SkyBlockInventoryGUI {
    public int page;
    public int slots;
    public SkyBlockItem item;

    public GUIStorageBackpackPage(int page, SkyBlockItem item) {
        super(StringUtility.getTextFromComponent(new NonPlayerItemUpdater(item).getUpdatedItem().build().getDisplayName())
                + " (Slot #" + page + ")",
                MathUtility.getFromSize(9 + ((Backpack) item.getGenericInstance()).getRows() * 9));

        this.slots = ((Backpack) item.getGenericInstance()).getRows() * 9;
        this.page = page;
        this.item = item;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = getPlayer();
        DatapointBackpacks.PlayerBackpacks data = player.getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class).getValue();

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 8);

        set(GUIClickableItem.getCloseItem(0));
        set(GUIClickableItem.getGoBackItem(1, new GUIStorage()));

        if (page != data.getHighestBackpackSlot()) {
            set(new GUIClickableItem(8) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIStorageBackpackPage(data.getHighestBackpackSlot(),
                            data.getBackpacks().get(data.getHighestBackpackSlot())).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§eLast Page >>",
                            "1ceb50d0d79b9fb790a7392660bc296b7ad2f856c5cbe1c566d99cfec191e668");
                }
            });

            if (data.getBackpacks().containsKey(page + 1))
                set(new GUIClickableItem(7) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        new GUIStorageBackpackPage(page + 1, data.getBackpacks().get(page + 1)).open(player);
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStackHead("§aNext Page >>",
                                "848ca732a6e35dafd15e795ebc10efedd9ef58ff2df9b17af6e3d807bdc0708b");
                    }
                });
        }
        if (page != data.getLowestBackpackSlot()) {
            set(new GUIClickableItem(5) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIStorageBackpackPage(data.getLowestBackpackSlot(), data.getBackpacks().get(data.getLowestBackpackSlot())).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§e< First Page",
                            "8af22a97292de001079a5d98a0ae3a82c427172eabc370ed6d4a31c7e3a0024f");
                }
            });

            if (data.getBackpacks().containsKey(page - 1))
                set(new GUIClickableItem(6) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        new GUIStorageBackpackPage(page - 1, data.getBackpacks().get(page - 1)).open(player);
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStackHead("§a< Previous Page",
                                "9c042597eda9f061794fe11dacf78926d247f9eea8ddef39dfbe6022989b8395");
                    }
                });
        }

        AtomicInteger slot = new AtomicInteger(9);
        item.getAttributeHandler().getBackpackData().items().forEach(item -> {
            set(new GUIItem(slot.getAndIncrement()) {
                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (item == null || item.isNA())
                        return ItemStackCreator.createNamedItemStack(Material.AIR);
                    return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
                }
            });
        });

        updateItemStacks(getInventory(), player);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        item.getAttributeHandler().getBackpackData().items().clear();

        for (int i = 9; i < slots + 9; i++) {
            item.getAttributeHandler().getBackpackData().items().add(new SkyBlockItem(getInventory().getItemStack(i)));
        }

        getPlayer().getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class).getValue().getBackpacks().put(page, item);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
        item.getAttributeHandler().getBackpackData().items().clear();

        for (int i = 9; i < slots + 9; i++) {
            item.getAttributeHandler().getBackpackData().items().add(new SkyBlockItem(getInventory().getItemStack(i)));
        }

        getPlayer().getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class).getValue().getBackpacks().put(page, item);
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
