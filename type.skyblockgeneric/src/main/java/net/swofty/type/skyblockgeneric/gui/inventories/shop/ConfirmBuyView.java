package net.swofty.type.skyblockgeneric.gui.inventories.shop;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Locale;

public final class ConfirmBuyView implements View<ConfirmBuyView.State> {

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.translatable("gui_shop.confirm_buy.title", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);

        layout.slot(12,
            (_, _) -> ItemStackCreator.getStack(
                I18n.t("gui_shop.confirm_buy.confirm_button"),
                Material.LIME_TERRACOTTA,
                1,
                I18n.iterable("gui_shop.confirm_buy.confirm_button.lore", Component.text(state.item.getDisplayName()), Component.text(StringUtility.commaify(state.price)))
            ),
            (click, c) -> {
                if (!(click.click() instanceof Click.Left || click.click() instanceof Click.Right)) return;

                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                Locale l = player.getLocale();
                if (player.getCoins() >= state.price) {
                    player.addAndUpdateItem(state.item);
                    player.removeCoins(state.price);
                    player.sendMessage(I18n.string("gui_shop.confirm_buy.bought_message", l, Component.text(state.item.getDisplayName()), Component.text(String.valueOf(state.price))));
                } else {
                    player.sendMessage(I18n.string("gui_shop.confirm_buy.not_enough_coins", l));
                }
                player.closeInventory();
            }
        );

        layout.slot(16,
            (_, _) -> TranslatableItemStackCreator.getStack("gui_shop.confirm_buy.cancel_button", Material.RED_TERRACOTTA, 1),
            (_, c) -> c.player().closeInventory()
        );
    }

    public record State(SkyBlockItem item, int price) {
    }
}
