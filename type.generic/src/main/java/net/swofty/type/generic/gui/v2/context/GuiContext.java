package net.swofty.type.generic.gui.v2.context;

import net.minestom.server.inventory.Inventory;
import net.swofty.type.generic.gui.v2.GuiSession;
import net.swofty.type.generic.user.HypixelPlayer;

public record GuiContext(
    HypixelPlayer player,
    Inventory inventory,
    GuiSession<?> session
) {
    @SuppressWarnings("unchecked")
    public <S> GuiSession<S> session(Class<S> stateType) {
        return (GuiSession<S>) session;
    }
}
