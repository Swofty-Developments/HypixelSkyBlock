package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.datapoints.DatapointQuiver;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;

public class GUIQuiver extends SkyBlockInventoryGUI {
    private static final Map<CustomCollectionAward, Integer> SLOTS_PER_UPGRADE = Map.of(
            CustomCollectionAward.QUIVER, 18,
            CustomCollectionAward.QUIVER_UPGRADE_1, 9,
            CustomCollectionAward.QUIVER_UPGRADE_2, 9
    );

    private int slotToSaveUpTo;

    public GUIQuiver() {
        super("Quiver", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(40));
        set(GUIClickableItem.getGoBackItem(39, new GUIYourBags()));

        int amountOfSlots = 0;
        int rawAmountOfSlots = 0;

        for (Map.Entry<CustomCollectionAward, Integer> entry : SLOTS_PER_UPGRADE.entrySet()) {
            if (e.player().hasCustomCollectionAward(entry.getKey())) {
                amountOfSlots += entry.getValue();
            } else {
                for (int i = 0; i < entry.getValue(); i++) {
                    set(new GUIItem(i + rawAmountOfSlots) {
                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                            return ItemStackCreator.getStack("§cLocked", Material.RED_STAINED_GLASS_PANE,
                                    1,
                                    "§7You must have the §a" + entry.getKey().getDisplay() + " §7upgrade",
                                    "§7to unlock this slot.");
                        }
                    });
                }
            }
            rawAmountOfSlots += entry.getValue();
        }

        slotToSaveUpTo = amountOfSlots;

        DatapointQuiver.PlayerQuiver quiver = e.player().getQuiver();

        for (int i = 0; i < amountOfSlots; i++) {
            SkyBlockItem item = quiver.getInSlot(i);
            set(new GUIClickableItem(i) {

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
                    save(player, slotToSaveUpTo);
                }

                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {}
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
            save(getPlayer(), slotToSaveUpTo);
            return;
        }

        e.setCancelled(true);
        getPlayer().sendMessage("§cYou cannot put this item in the Quiver!");
    }

    public boolean isItemAllowed(SkyBlockItem item) {
        return item.isNA() || item.getMaterial() == Material.ARROW;
    }

    public void save(SkyBlockPlayer player, int slotToSaveUpTo) {
        DatapointQuiver.PlayerQuiver quiver = player.getQuiver();
        for (int i = 0; i < slotToSaveUpTo; i++) {
            SkyBlockItem item = new SkyBlockItem(getInventory().getItemStack(i));
            if (item.isNA() || item.getGenericInstance() == null) {
                quiver.getQuiverMap().remove(i);
            } else {
                quiver.getQuiverMap().put(i, item);
            }
        }
    }
}
