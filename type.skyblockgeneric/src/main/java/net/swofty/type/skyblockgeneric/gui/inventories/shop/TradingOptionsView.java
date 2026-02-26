package net.swofty.type.skyblockgeneric.gui.inventories.shop;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.shop.ShopPrice;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public final class TradingOptionsView implements View<TradingOptionsView.State> {

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Shop Trading Options", InventoryType.CHEST_6_ROW);
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

        lore.add("");
        lore.add("§7Cost");
        lore.addAll(totalPrice.getGUIDisplay());
        lore.add("");
        lore.add("§7Stock");
        lore.add("§6 " + player.getShoppingData().getStock(item.getItem().toUnderstandable()) + " §7remaining");
        lore.add("");
        lore.add("§eClick to purchase!");

        Component baseName = itemStack.build().get(DataComponents.CUSTOM_NAME);
        if (baseName == null) {
            baseName = Component.text(sbItem.getDisplayName());
        }

        String displayName = StringUtility.getTextFromComponent(baseName.append(Component.text(" §8x" + amount)));

        return ItemStackCreator.getStack(displayName, itemStack.build().material(), amount, lore);
    }

    private void attemptBuy(State state, int amount, ViewContext ctx) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();

        if (!player.getShoppingData().canPurchase(state.item.getItem().toUnderstandable(), amount)) {
            player.sendMessage("§cYou have reached the maximum amount of items you can buy!");
            return;
        }

        ShopPrice totalPrice = state.stackPrice.multiply(amount);
        if (!totalPrice.canAfford(player)) {
            player.sendMessage("§cYou don't have enough " + state.stackPrice.getNamePlural() + "!");
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
