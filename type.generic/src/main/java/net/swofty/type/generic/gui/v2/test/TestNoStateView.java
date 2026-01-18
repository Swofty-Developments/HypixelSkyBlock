package net.swofty.type.generic.gui.v2.test;

import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class TestNoStateView extends StatelessView {

	@Override
	public ViewConfiguration<DefaultState> configuration() {
		return new ViewConfiguration<>("Stateless", InventoryType.CHEST_3_ROW);
	}

	@Override
	public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
		Components.fill(layout);
		Components.close(layout, 22);
	}
}
