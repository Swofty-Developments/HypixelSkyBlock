package net.swofty.type.hub.gui.elizabeth.subguis;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.hub.gui.elizabeth.GUIBitsShop;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIBitsConfirmBuy extends SkyBlockAbstractInventory {
    private final SkyBlockItem item;
    private final Integer price;

    public GUIBitsConfirmBuy(SkyBlockItem item, Integer price) {
        super(InventoryType.CHEST_3_ROW);
        this.item = item;
        this.price = price;
        doAction(new SetTitleAction(Component.text("Confirm")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Confirm button
        attachItem(GUIItem.builder(11)
                .item(ItemStackCreator.getStack("§aConfirm", Material.LIME_TERRACOTTA, 1,
                        "§7Buying: " + item.getDisplayName(),
                        "§7Cost: §b" + StringUtility.commaify(price)).build())
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player1 = ctx.player();
                    player1.addAndUpdateItem(item);
                    player1.setBits(player1.getBits() - price);
                    player1.openInventory(new GUIBitsShop());
                    return true;
                })
                .build());

        // Cancel button
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§cCancel", Material.RED_TERRACOTTA, 1).build())
                .onClick((ctx, clickedItem) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}