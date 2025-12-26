package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.StringUtility;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.shop.ShopManager;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIQuickBuyEditor extends HypixelInventoryGUI {
    private final ShopManager shopService = TypeBedWarsGameLoader.shopManager;
    private final ShopItem shopItem;
    private final Game game;

    public GUIQuickBuyEditor(Game game, ShopItem shopItem) {
        super("Adding to Quick Buy...", InventoryType.CHEST_6_ROW);
        this.shopItem = shopItem;
        this.game = game;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add(
                        "§7Cost: " + shopItem.getCurrency().getColor() + shopItem.getPrice().apply(game.getBedwarsGameType()) + " " + shopItem.getCurrency().getName()
                );
                lore.add(" ");
                if (shopItem.getDescription() != null && !shopItem.getDescription().isEmpty()) {
                    lore.addAll(StringUtility.splitByNewLine(shopItem.getDescription(), "§7"));
                    lore.add(" ");
                }
                lore.add("§eAdding item to Quick Slot!");

                return ItemStackCreator.updateLore(
                        shopItem.getDisplay().builder().set(DataComponents.CUSTOM_NAME, Component.text("§a" + shopItem.getName())),
                        lore
                );
            }
        });
        GUIItemShop.populateShopItems(this, shopService, game, null, shopItem, null);
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
