package net.swofty.type.generic.gui.v2.test;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class TestNoStateView extends StatelessView {

	@Override
	public InventoryType size() {
		return InventoryType.CHEST_3_ROW;
	}

	@Override
	public Component title(DefaultState state, ViewContext ctx) {
		return Component.text("Stateless");
	}

	@Override
	public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
		Components.fill(layout);
		Components.close(layout, 22);
	}
}
