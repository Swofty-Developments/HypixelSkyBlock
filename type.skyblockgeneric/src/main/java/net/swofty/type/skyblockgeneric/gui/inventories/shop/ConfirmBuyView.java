package net.swofty.type.skyblockgeneric.gui.inventories.shop;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;

public final class ConfirmBuyView implements View<ConfirmBuyView.State> {

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>(Component.text("Confirm"), InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);

        layout.slot(12,
                (_, __) -> {
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§7Buying: " + state.item.getDisplayItem());
                    lore.add("§7Cost: §6" + StringUtility.commaify(state.price) + " Coins");
                    return ItemStackCreator.getStack("§aConfirm", Material.LIME_TERRACOTTA, 1, lore);
                },
                (click, c) -> {
                    if (!(click.click() instanceof Click.Left || click.click() instanceof Click.Right)) return;

                    SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                    if (player.getCoins() >= state.price) {
                        player.addAndUpdateItem(state.item);
                        player.removeCoins(state.price);
                        player.sendMessage("§aYou bought " + state.item.getDisplayName() + " §afor §6" + state.price + " Coins§a!");
                    } else {
                        player.sendMessage("§4You don't have enough coins!");
                    }
                    player.closeInventory();
                }
        );

        layout.slot(16,
                (_, __) -> ItemStackCreator.getStack("§4Cancel", Material.RED_TERRACOTTA, 1),
                (_, c) -> c.player().closeInventory()
        );
    }

    public record State(SkyBlockItem item, int price) {
    }
}
