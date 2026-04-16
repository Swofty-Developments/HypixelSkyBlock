package net.swofty.type.skyblockgeneric.gui.inventories.shop;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.shop.ShopPrice;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class TradingOptionsView implements View<TradingOptionsView.State> {

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.translatable("gui_shop.trading_options.title", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);

        layout.slot(20, (s, c) -> createTradeItem(s.item, 1, (SkyBlockPlayer) c.player(), s.stackPrice), (_, c) -> attemptBuy(state, 1, c));
        layout.slot(21, (s, c) -> createTradeItem(s.item, 5, (SkyBlockPlayer) c.player(), s.stackPrice), (_, c) -> attemptBuy(state, 5, c));
        layout.slot(22, (s, c) -> createTradeItem(s.item, 10, (SkyBlockPlayer) c.player(), s.stackPrice), (_, c) -> attemptBuy(state, 10, c));
        layout.slot(23, (s, c) -> createTradeItem(s.item, 32, (SkyBlockPlayer) c.player(), s.stackPrice), (_, c) -> attemptBuy(state, 32, c));
        layout.slot(24, (s, c) -> createTradeItem(s.item, 64, (SkyBlockPlayer) c.player(), s.stackPrice), (_, c) -> attemptBuy(state, 64, c));

        Components.backOrClose(layout, 49, ctx);
    }

    private ItemStack.Builder createTradeItem(ShopView.ShopItem item, int amount, SkyBlockPlayer player, ShopPrice perOnePrice) {
        ShopPrice totalPrice = perOnePrice.multiply(amount);

        SkyBlockItem sbItem = item.getItem();
        ItemStack.Builder itemStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();

        List<Component> existingLoreComponents = itemStack.build().get(DataComponents.LORE);
        List<String> lore = new ArrayList<>((existingLoreComponents == null ? List.<Component>of() : existingLoreComponents)
                .stream().map(StringUtility::getTextFromComponent).toList());

        Locale l = player.getLocale();
        lore.add("");
        lore.add(I18n.string("gui_shop.trading_options.cost_label", l));
        lore.addAll(totalPrice.getGUIDisplay());
        lore.add("");
        lore.add(I18n.string("gui_shop.trading_options.stock_label", l));
        lore.add(I18n.string("gui_shop.trading_options.stock_remaining", l, Map.of("count", String.valueOf(player.getShoppingData().getStock(item.getItem().toUnderstandable())))));
        lore.add("");
        lore.add(I18n.string("gui_shop.trading_options.click_to_purchase", l));

        Component baseName = itemStack.build().get(DataComponents.CUSTOM_NAME);
        if (baseName == null) {
            baseName = Component.text(sbItem.getDisplayName());
        }

        String displayName = StringUtility.getTextFromComponent(baseName.append(Component.text(" §8x" + amount)));

        return ItemStackCreator.getStack(displayName, itemStack.build().material(), amount, lore);
    }

    private void attemptBuy(State state, int amount, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();

        Locale l = player.getLocale();
        if (!player.getShoppingData().canPurchase(state.item.getItem().toUnderstandable(), amount)) {
            player.sendMessage(I18n.string("gui_shop.trading_options.max_reached", l));
            return;
        }

        ShopPrice totalPrice = state.stackPrice.multiply(amount);
        if (!totalPrice.canAfford(player)) {
            player.sendMessage(I18n.string("gui_shop.trading_options.not_enough", l, Map.of("currency", state.stackPrice.getNamePlural())));
            return;
        }

        totalPrice.processPurchase(player);

        SkyBlockItem sbItem = state.item.getItem();
        ItemStack.Builder cleanStack = new NonPlayerItemUpdater(sbItem).getUpdatedItem();
        cleanStack.amount(amount);
        player.addAndUpdateItem(cleanStack.build());
        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
        player.getShoppingData().documentPurchase(state.item.getItem().toUnderstandable(), amount);

        ctx.session(Object.class).refresh();
    }

    public record State(ShopView.ShopItem item, ShopPrice stackPrice) {
    }
}
