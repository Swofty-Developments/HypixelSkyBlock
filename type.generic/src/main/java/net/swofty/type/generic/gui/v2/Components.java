package net.swofty.type.generic.gui.v2;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;

import java.util.Set;

public final class Components {

	public static final ItemStack FILLER = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
			.set(DataComponents.CUSTOM_NAME, Component.text(" "))
			.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of()))
			.build();
	public static final ItemStack CLOSE_BUTTON = ItemStack.builder(Material.BARRIER)
			.set(DataComponents.CUSTOM_NAME, Component.text("§cClose"))
			.build();
	public static final ItemStack BACK_BUTTON = ItemStack.builder(Material.ARROW)
			.set(DataComponents.CUSTOM_NAME, Component.text("§aGo Back"))
			.build();

	public static <S> void close(GuiLayout<S> layout, int slot) {
		layout.slot(slot, (_, _) -> CLOSE_BUTTON, (_, ctx) -> ctx.player().closeInventory());
	}

	public static <S> void back(GuiLayout<S> layout, int slot, View<?> target, Object targetState) {
		layout.slot(slot, (_, _) -> BACK_BUTTON,
				(_, ctx) -> open(ctx, target, targetState));
	}

	@SuppressWarnings("unchecked")
	public static <T> void open(net.swofty.type.generic.gui.v2.context.GuiContext ctx, View<T> view, Object state) {
		GuiSession.open(view, ctx.player(), (T) state);
	}

}

