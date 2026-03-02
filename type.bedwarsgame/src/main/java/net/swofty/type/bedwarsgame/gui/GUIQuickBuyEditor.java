package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.swofty.commons.StringUtility;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.shop.ShopManager;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.ArrayList;
import java.util.List;

public class GUIQuickBuyEditor extends StatelessView {
    private final ShopManager shopService = TypeBedWarsGameLoader.shopManager;
    private final ShopItem shopItem;
    private final BedWarsGame game;

    public GUIQuickBuyEditor(BedWarsGame game, ShopItem shopItem) {
        this.shopItem = shopItem;
        this.game = game;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Adding to Quick Buy...", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(4, (s, c) -> {
            List<String> lore = new ArrayList<>();
            lore.add("§7Cost: " + shopItem.getCurrency().getColor() + shopItem.getPrice().apply(game.getGameType()) + " " + shopItem.getCurrency().getName());
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
        });

        GUIItemShop.populateShopItems(layout, shopService, game, null, shopItem, _ -> {
        });
    }

    @Override
    public boolean onBottomClick(ClickContext<DefaultState> click, ViewContext ctx) {
        return false;
    }
}
