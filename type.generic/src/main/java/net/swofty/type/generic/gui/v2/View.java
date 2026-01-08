package net.swofty.type.generic.gui.v2;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public interface View<S> {
	InventoryType size();

	Object title(S state, ViewContext ctx);

	default Component getTitle(S state, ViewContext ctx) {
		Object title = title(state, ctx);
		if (title instanceof Component component) {
			return component;
		} else if (title instanceof String str) {
			return Component.text(str);
		}
		throw new IllegalStateException("Title must be a Component or String");
	}

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
