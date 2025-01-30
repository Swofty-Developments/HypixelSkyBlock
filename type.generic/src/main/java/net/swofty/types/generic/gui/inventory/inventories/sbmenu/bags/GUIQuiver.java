package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.datapoints.DatapointQuiver;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;

public class GUIQuiver extends SkyBlockAbstractInventory {
    private static final String STATE_SLOT_UNLOCKED = "slot_unlocked_";
    private static final String STATE_SLOT_LOCKED = "slot_locked_";

    private static final Map<CustomCollectionAward, Integer> SLOTS_PER_UPGRADE = Map.of(
            CustomCollectionAward.QUIVER, 18,
            CustomCollectionAward.QUIVER_UPGRADE_1, 9,
            CustomCollectionAward.QUIVER_UPGRADE_2, 9
    );

    private int slotToSaveUpTo;

    public GUIQuiver() {
        super(InventoryType.CHEST_5_ROW);
        doAction(new SetTitleAction(Component.text("Quiver")));
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

        setupQuiverSlots(player);
    }

    private void setupQuiverSlots(SkyBlockPlayer player) {
        int amountOfSlots = 0;
        int rawAmountOfSlots = 0;

        // Setup slots based on upgrades
        for (Map.Entry<CustomCollectionAward, Integer> entry : SLOTS_PER_UPGRADE.entrySet()) {
            if (player.hasCustomCollectionAward(entry.getKey())) {
                amountOfSlots += entry.getValue();
                for (int i = 0; i < entry.getValue(); i++) {
                    int slot = i + rawAmountOfSlots;
                    doAction(new AddStateAction(STATE_SLOT_UNLOCKED + slot));
                }
            } else {
                for (int i = 0; i < entry.getValue(); i++) {
                    int slot = i + rawAmountOfSlots;
                    doAction(new AddStateAction(STATE_SLOT_LOCKED + slot));

                    final CustomCollectionAward upgradeKey = entry.getKey();
                    attachItem(GUIItem.builder(slot)
                            .item(ItemStackCreator.getStack("§cLocked", Material.RED_STAINED_GLASS_PANE,
                                    1,
                                    "§7You must have the §a" + upgradeKey.getDisplay() + " §7upgrade",
                                    "§7to unlock this slot.").build())
                            .requireState(STATE_SLOT_LOCKED + slot)
                            .build());
                }
            }
            rawAmountOfSlots += entry.getValue();
        }

        slotToSaveUpTo = amountOfSlots;
        DatapointQuiver.PlayerQuiver quiver = player.getQuiver();

        // Setup unlocked slots with items
        for (int i = 0; i < amountOfSlots; i++) {
            final int slot = i;
            SkyBlockItem item = quiver.getInSlot(i);

            attachItem(GUIItem.builder(slot)
                    .item(() -> item == null ? ItemStack.AIR :
                            PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build())
                    .requireState(STATE_SLOT_UNLOCKED + slot)
                    .onClick((ctx, clickedItem) -> {
                        save(ctx.player(), slotToSaveUpTo);
                        return true;
                    })
                    .onPostClick(plr -> save(plr, slotToSaveUpTo))
                    .build());
        }
    }

    private boolean isItemAllowed(SkyBlockItem item) {
        if (item.isNA()) return true;
        if (item.getMaterial().equals(Material.AIR)) return true;
        return item.getMaterial() == Material.ARROW;
    }

    private void save(SkyBlockPlayer player, int slotToSaveUpTo) {
        DatapointQuiver.PlayerQuiver quiver = player.getQuiver();
        for (int i = 0; i < slotToSaveUpTo; i++) {
            SkyBlockItem item = new SkyBlockItem(getItemStack(i));
            if (item.isNA()) {
                quiver.getQuiverMap().remove(i);
            } else {
                quiver.getQuiverMap().put(i, item);
            }
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        save((SkyBlockPlayer) event.getPlayer(), slotToSaveUpTo);
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        SkyBlockItem cursorItem = new SkyBlockItem(event.getCursorItem());
        SkyBlockItem clickedItem = new SkyBlockItem(event.getClickedItem());

        if (isItemAllowed(cursorItem) && isItemAllowed(clickedItem)) {
            save((SkyBlockPlayer) event.getPlayer(), slotToSaveUpTo);
            return;
        }

        event.setCancelled(true);
        ((SkyBlockPlayer) event.getPlayer()).sendMessage("§cYou cannot put this item in the Quiver!");
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        save(player, slotToSaveUpTo);
    }
}