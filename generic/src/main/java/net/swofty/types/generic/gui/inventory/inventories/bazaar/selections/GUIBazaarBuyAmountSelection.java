package net.swofty.types.generic.gui.inventory.inventories.bazaar.selections;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.concurrent.CompletableFuture;

public class GUIBazaarBuyAmountSelection extends SkyBlockInventoryGUI implements RefreshingGUI {
    private CompletableFuture<Double> future = new CompletableFuture<>();

    public GUIBazaarBuyAmountSelection(SkyBlockInventoryGUI previousGUI, ItemType itemType) {
        super("How many do you want?", InventoryType.CHEST_4_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, previousGUI));
    }

    public CompletableFuture<Double> openAmountSelection(SkyBlockPlayer player) {
        future = new CompletableFuture<>();
        open(player);

        return future;
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline(new ProtocolPingSpecification()).join()) {
            player.sendMessage("§cThe Bazaar is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
