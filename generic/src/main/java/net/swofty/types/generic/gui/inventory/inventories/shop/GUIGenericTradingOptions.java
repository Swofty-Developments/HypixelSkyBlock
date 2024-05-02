package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.shop.ShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public final class GUIGenericTradingOptions extends SkyBlockInventoryGUI {
    private final SkyBlockShopGUI.ShopItem item;
    private final SkyBlockShopGUI retPointer;
    private final ShopPrice stackPrice;

    public GUIGenericTradingOptions(SkyBlockShopGUI.ShopItem item, SkyBlockShopGUI retPoint, ShopPrice stackPrice) {
        super("Shop Trading Options", InventoryType.CHEST_6_ROW);
        this.item = item;
        this.retPointer = retPoint;
        this.stackPrice = stackPrice;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        set(createTradeItem(item, 20, 1, getPlayer(), stackPrice));
        set(createTradeItem(item, 21, 5, getPlayer(), stackPrice));
        set(createTradeItem(item, 22, 10, getPlayer(), stackPrice));
        set(createTradeItem(item, 23, 32, getPlayer(), stackPrice));
        set(createTradeItem(item, 24, 64, getPlayer(), stackPrice));

        set(GUIClickableItem.getGoBackItem(49, retPointer));

        updateItemStacks(e.inventory(), getPlayer());
    }

    private GUIClickableItem createTradeItem(SkyBlockShopGUI.ShopItem item, int slot, int amount, SkyBlockPlayer player, ShopPrice stackprice) {
        stackprice = stackprice.multiply(amount);

        SkyBlockItem sbItem = item.getItem();
        ItemStack.Builder itemStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();

        List<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());

        lore.add("");
        lore.add("§7Cost");
        lore.addAll(stackprice.getGUIDisplay());
        lore.add("");
        lore.add("§7Stock");
        lore.add("§6 " + player.getShoppingData().getStock(item.getItem()) + " §7remaining");
        lore.add("");
        lore.add("§eClick to purchase!");

        ShopPrice finalStackprice = stackprice;
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (!player.getShoppingData().canPurchase(item.getItem(), amount)) {
                    player.sendMessage("§cYou have reached the maximum amount of items you can buy!");
                    return;
                }

                if (!finalStackprice.canAfford(player)) {
                    player.sendMessage("§cYou don't have enough " + stackPrice.getNamePlural() + "!");
                    return;
                }

                finalStackprice.processPurchase(player);

                ItemStack.Builder cleanStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();
                cleanStack.amount(amount);
                player.addAndUpdateItem(cleanStack.build());
                player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
                player.getShoppingData().documentPurchase(item.getItem(), amount);
                updateThis(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                String displayName = StringUtility.getTextFromComponent(itemStack.build().getDisplayName().append(Component.text(" §8x" + amount)));
                return ItemStackCreator.getStack(displayName, itemStack.build().material(), 0, amount, lore);
            }
        };
    }

    private void updateThis(SkyBlockPlayer player) {
        this.open(player);
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

    }
}
