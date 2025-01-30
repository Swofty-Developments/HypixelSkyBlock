package net.swofty.types.generic.gui.inventory.inventories.bazaar.selections;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.item.ItemType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.concurrent.CompletableFuture;

public class GUIBazaarBuyAmountSelection extends SkyBlockAbstractInventory {
    private CompletableFuture<Double> future = new CompletableFuture<>();
    private final ItemType itemTypeLinker;
    private final SkyBlockAbstractInventory previousGUI;

    public GUIBazaarBuyAmountSelection(SkyBlockAbstractInventory previousGUI, ItemType itemTypeLinker) {
        super(InventoryType.CHEST_4_ROW);
        this.previousGUI = previousGUI;
        this.itemTypeLinker = itemTypeLinker;

        doAction(new SetTitleAction(Component.text("How many do you want?")));

        startLoop("refresh", 10, () -> refreshItems(owner));
    }

    public CompletableFuture<Double> openAmountSelection(SkyBlockPlayer player) {
        future = new CompletableFuture<>();
        addViewer(player);
        return future;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + previousGUI.getTitle()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());
    }

    private void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline().join()) {
            player.sendMessage("§cThe Bazaar is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        future.complete(0D);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        future.complete(0D);
    }
}