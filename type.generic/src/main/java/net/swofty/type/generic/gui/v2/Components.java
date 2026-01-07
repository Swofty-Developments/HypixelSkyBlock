package net.swofty.type.generic.gui.v2;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import net.swofty.type.generic.gui.v2.context.ViewContext;

import java.util.Set;

public final class Components {

	public static final ItemStack.Builder FILLER = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
			.set(DataComponents.CUSTOM_NAME, Component.text(" "))
			.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of()));
	public static final ItemStack.Builder CLOSE_BUTTON = ItemStack.builder(Material.BARRIER)
			.set(DataComponents.CUSTOM_NAME, Component.text("§cClose"));
	public static final ItemStack.Builder BACK_BUTTON = ItemStack.builder(Material.ARROW)
			.set(DataComponents.CUSTOM_NAME, Component.text("§aGo Back"));

	public static <S> void close(ViewLayout<S> layout, int slot) {
		layout.slot(slot, (_, _) -> CLOSE_BUTTON, (_, ctx) -> ctx.player().closeInventory());
	}

	public static <S> void back(ViewLayout<S> layout, int slot, View<?> target, Object targetState) {
		layout.slot(slot, (_, _) -> BACK_BUTTON,
				(_, ctx) -> open(ctx, target, targetState));
	}

	@SuppressWarnings("unchecked")
	public static <T> void open(ViewContext ctx, View<T> view, Object state) {
		ViewSession.open(view, ctx.player(), (T) state);
	}

}

