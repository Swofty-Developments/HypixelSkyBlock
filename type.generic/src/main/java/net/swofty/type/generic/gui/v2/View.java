package net.swofty.type.generic.gui.v2;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.context.GuiContext;

public interface View<S> {
    InventoryType size();
    Component title(S state, GuiContext ctx);
    void layout(GuiLayout<S> layout, S state, GuiContext ctx);
}
