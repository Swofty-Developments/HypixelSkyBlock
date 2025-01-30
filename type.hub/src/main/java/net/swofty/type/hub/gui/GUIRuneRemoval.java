package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.attribute.attributes.ItemAttributeRuneInfusedWith;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.RuneableComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIRuneRemoval extends SkyBlockAbstractInventory {
    private static final String STATE_NO_ITEM = "no_item";
    private static final String STATE_INVALID_ITEM = "invalid_item";
    private static final String STATE_VALID_ITEM = "valid_item";

    private static final int[] BORDER_SLOTS = {
            0, 8, 9, 17, 18, 26, 27, 35, 36, 44
    };
    private static final int[] BOTTOM_SLOTS = {
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };

    public GUIRuneRemoval() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Rune Removal")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Initial state
        doAction(new AddStateAction(STATE_NO_ITEM));

        // Base GUI setup
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());
        for (int slot : BOTTOM_SLOTS) {
            attachItem(GUIItem.builder(slot)
                    .item(ItemStackCreator.createNamedItemStack(Material.WHITE_STAINED_GLASS_PANE).build())
                    .build());
        }

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
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Runic Pedestal").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIRunicPedestal());
                    return true;
                })
                .build());

        // State-based borders
        borderWithState(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE).build(), STATE_NO_ITEM);
        borderWithState(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE).build(), STATE_INVALID_ITEM);
        borderWithState(ItemStackCreator.createNamedItemStack(Material.GREEN_STAINED_GLASS_PANE).build(), STATE_VALID_ITEM);

        // Item slot
        attachItem(GUIItem.builder(13)
                .item(ItemStack.AIR)
                .onClick((ctx, clickedItem) -> handleItemSlotClick(ctx.player(), clickedItem, ctx.cursorItem()))
                .build());

        // Action button
        setupActionButton();
    }

    private void setupActionButton() {
        // No item state
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§aRune Removal", Material.BARRIER, 1,
                        "§7Place an item with a rune attached to",
                        "§7it in the above slot.").build())
                .requireState(STATE_NO_ITEM)
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cYou must place an item in the above slot!");
                    return true;
                })
                .build());

        // Invalid item state
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§cError!", Material.BARRIER, 1,
                        "§7Place an item with a rune attached to",
                        "§7it in the above slot.").build())
                .requireState(STATE_INVALID_ITEM)
                .build());

        // Valid item state
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack(
                        "§aClick to Remove Rune!", Material.CAULDRON, 1,
                        "§cWARNING: The rune will be lost",
                        "§cforever!").build())
                .requireState(STATE_VALID_ITEM)
                .onClick((ctx, item) -> {
                    SkyBlockItem sbItem = new SkyBlockItem(getItemStack(13));
                    ItemAttributeRuneInfusedWith.RuneData runeData = sbItem.getAttributeHandler().getRuneData();

                    runeData.setRuneType(null);
                    runeData.setLevel(null);

                    ctx.player().sendMessage("§aSuccessfully removed rune!");
                    updateRuneState(sbItem);
                    return true;
                })
                .build());
    }

    private boolean handleItemSlotClick(SkyBlockPlayer player, ItemStack clickedItem, ItemStack cursorItem) {
        if (!clickedItem.isAir()) {
            player.addAndUpdateItem(clickedItem);
            setItemStack(13, ItemStack.AIR);
            updateRuneState(null);
            return true;
        }

        if (cursorItem.get(ItemComponent.CUSTOM_NAME) == null) {
            updateRuneState(null);
            return true;
        }

        SkyBlockItem sbItem = new SkyBlockItem(cursorItem);
        setItemStack(13, PlayerItemUpdater.playerUpdate(player, sbItem.getItemStack()).build());
        updateRuneState(sbItem);
        return true;
    }

    private void updateRuneState(SkyBlockItem item) {
        // Remove all states
        doAction(new RemoveStateAction(STATE_NO_ITEM));
        doAction(new RemoveStateAction(STATE_INVALID_ITEM));
        doAction(new RemoveStateAction(STATE_VALID_ITEM));

        if (item == null) {
            doAction(new AddStateAction(STATE_NO_ITEM));
            return;
        }

        ItemAttributeRuneInfusedWith.RuneData runeData = item.getAttributeHandler().getRuneData();
        if (item.getAmount() > 1 ||
                item.hasComponent(RuneableComponent.class) ||
                runeData == null ||
                !runeData.hasRune()) {
            doAction(new AddStateAction(STATE_INVALID_ITEM));
            return;
        }

        doAction(new AddStateAction(STATE_VALID_ITEM));
    }

    private void borderWithState(ItemStack border, String state) {
        for (int slot : BORDER_SLOTS) {
            attachItem(GUIItem.builder(slot)
                    .item(border)
                    .requireState(state)
                    .build());
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(13)));
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(13)));
    }
}