package net.swofty.type.generic.gui.v2;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public interface View<S> {
	InventoryType size();

	Component title(S state, ViewContext ctx);

	void layout(ViewLayout<S> layout, S state, ViewContext ctx);

	default void onOpen(S state, ViewContext ctx) {
	}

	default void onClose(S state, ViewContext ctx, ViewSession.CloseReason reason) {
	}

	default boolean onClick(ClickContext<S> click, ViewContext ctx) {
		return false;
	}

	default void onRefresh(S state, ViewContext ctx) {
	}

	default boolean onBottomClick(ClickContext<S> click, ViewContext ctx) {
		return false;
	}
}
