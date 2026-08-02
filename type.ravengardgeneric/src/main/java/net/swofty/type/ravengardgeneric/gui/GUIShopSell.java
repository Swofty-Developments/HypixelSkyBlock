package net.swofty.type.ravengardgeneric.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.ravengardgeneric.item.RavengardItem;
import net.swofty.type.ravengardgeneric.item.RavengardItemRegistry;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;
import net.swofty.type.ravengardgeneric.profile.RavengardProfiles;
import net.swofty.type.ravengardgeneric.shop.RavengardShop;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The sell side of a shop: the grid lists the player's sellable items at their crown value, one
 * click sells a single item and the sell items button clears the lot.
 */
public class GUIShopSell extends RavengardView {
    private static final int PANEL_ICON = 0xE23C;
    private static final int SLOT_TEXT_SELL = 50;
    private static final int[] GRID = {4, 5, 6, 7, 8, 13, 14, 15, 16, 17,
            22, 23, 24, 25, 26, 31, 32, 33, 34, 35};

    private final RavengardShop shop;

    public GUIShopSell(RavengardShop shop) {
        this.shop = shop;
    }

    @Override
    protected String title() {
        return shop.title();
    }

    @Override
    protected int panelIcon() {
        return PANEL_ICON;
    }

    @Override
    protected void content(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        place(layout, GUIShop.SLOT_BANNER, RavengardItems.button(GUIShop.banner(shop))
                .label(shop.title())
                .lore("§7Buy and sell items to help you on.",
                        "§7your adventures!"));

        interactive(layout, GUIShop.SLOT_BUY, RavengardItems.button(RavengardButton.BUY)
                        .label("Buy")
                        .lore("§7Purchase items from this shop to",
                                "§7help you on your adventures!")
                        .blankLine()
                        .lore("§eClick to buy!"),
                (click, viewContext) -> ViewNavigator.get(viewContext.player()).push(new GUIShop(shop)));

        place(layout, GUIShop.SLOT_SELL, RavengardItems.button(RavengardButton.SELL)
                .label("Sell")
                .lore("§7Sell items from your inventory for",
                        "§7some extra cash!")
                .blankLine()
                .lore("§aYou are here!"));

        if (!(ctx.player() instanceof RavengardPlayer player)) {
            return;
        }

        List<Integer> sellable = sellableSlots(player);
        for (int index = 0; index < GRID.length && index < sellable.size(); index++) {
            placeSellable(layout, player, GRID[index], sellable.get(index));
        }

        interactive(layout, SLOT_TEXT_SELL, RavengardItems.button(RavengardButton.TEXT_SELL)
                        .label("Sell Items")
                        .blankLine()
                        .lore("§eClick to sell everything shown!"),
                (click, viewContext) -> {
                    if (!(viewContext.player() instanceof RavengardPlayer target)) {
                        return;
                    }
                    int total = 0;
                    for (int slot : sellableSlots(target)) {
                        RavengardItemType type = typeOf(target.getInventory().getItemStack(slot));
                        if (type != null) {
                            total += (int) type.statistic("value");
                            target.getInventory().setItemStack(slot, ItemStack.AIR);
                        }
                    }
                    if (total > 0) {
                        RavengardProfiles.addCrowns(target, total);
                        target.sendMessage(Component.text("You sold your items for ")
                                .color(NamedTextColor.GREEN)
                                .append(RavengardItem.crowns(total, ""))
                                .append(Component.text("!").color(NamedTextColor.GREEN)));
                    }
                    ViewNavigator.get(target).push(new GUIShopSell(shop));
                });
    }

    private void placeSellable(ViewLayout<DefaultState> layout, RavengardPlayer player,
                               int gridSlot, int inventorySlot) {
        ItemStack stack = player.getInventory().getItemStack(inventorySlot);
        RavengardItemType type = typeOf(stack);
        if (type == null) {
            return;
        }
        int value = (int) type.statistic("value");

        List<Component> lore = new ArrayList<>(stack.get(DataComponents.LORE, List.of()));
        lore.add(Component.empty());
        lore.add(Component.text("Click to sell for ").color(NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text("\uD83D\uDC51").color(NamedTextColor.WHITE))
                .append(Component.text(String.valueOf(value)).color(TextColor.color(0xFFCE47)))
                .append(Component.text("!").color(NamedTextColor.YELLOW)));

        ItemStack.Builder display = RavengardItem.displayBuilder(type);
        display.set(DataComponents.LORE, lore);

        layout.slot(gridSlot, display, (click, viewContext) -> {
            if (!(viewContext.player() instanceof RavengardPlayer target)) {
                return;
            }
            RavengardItemType current = typeOf(target.getInventory().getItemStack(inventorySlot));
            if (current == null) {
                return;
            }
            target.getInventory().setItemStack(inventorySlot, ItemStack.AIR);
            RavengardProfiles.addCrowns(target, value);
            target.sendMessage(Component.text("You sold ").color(NamedTextColor.GREEN)
                    .append(Component.text(current.getDisplayName()).color(NamedTextColor.WHITE))
                    .append(Component.text(" for ").color(NamedTextColor.GREEN))
                    .append(RavengardItem.crowns(value, ""))
                    .append(Component.text("!").color(NamedTextColor.GREEN)));
            ViewNavigator.get(target).push(new GUIShopSell(shop));
        });
    }

    /** Inventory slots holding a registry item with a crown value, skipping displays and panes. */
    private static List<Integer> sellableSlots(RavengardPlayer player) {
        List<Integer> slots = new ArrayList<>();
        for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
            RavengardItemType type = typeOf(player.getInventory().getItemStack(slot));
            if (type != null && type.statistic("value") > 0) {
                slots.add(slot);
            }
        }
        return slots;
    }

    private static RavengardItemType typeOf(ItemStack stack) {
        if (stack.isAir()) {
            return null;
        }
        var data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return null;
        }
        String id = data.nbt().getString("id", "");
        int split = id.indexOf('_');
        return split < 0 ? null : RavengardItemRegistry.get(id.substring(split + 1));
    }
}
