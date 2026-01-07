package net.swofty.type.generic.gui.v2;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;

public final class Components {

    public static final ItemStack FILLER = item(Material.BLACK_STAINED_GLASS_PANE, " ", true);
    public static final ItemStack CLOSE_BUTTON = item(Material.BARRIER, "§cClose");

    public static <S> void close(GuiLayout<S> layout, int slot) {
        layout.slot(slot, (_, _) -> CLOSE_BUTTON, (_, ctx) -> ctx.player().closeInventory());
    }

    public static <S> void back(GuiLayout<S> layout, int slot, View<?> target, Object targetState) {
        layout.slot(slot, (_, _) -> item(Material.ARROW, "§aGo Back"),
            (_, ctx) -> open(ctx, target, targetState));
    }

    @SuppressWarnings("unchecked")
    public static <T> void open(net.swofty.type.generic.gui.v2.context.GuiContext ctx, View<T> view, Object state) {
        GuiSession.open(view, ctx.player(), (T) state);
    }

    public static ItemStack item(Material material, String name) {
        return ItemStack.builder(material)
            .set(DataComponents.CUSTOM_NAME, Component.text(name))
            .build();
    }

    public static ItemStack item(Material material, String name, boolean hideTooltip) {
        var builder = ItemStack.builder(material).set(DataComponents.CUSTOM_NAME, Component.text(name));
        if (hideTooltip) builder.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.EMPTY);
        return builder.build();
    }
}

